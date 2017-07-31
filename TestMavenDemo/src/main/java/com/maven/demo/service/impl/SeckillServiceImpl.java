package com.maven.demo.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import com.maven.demo.DAO.RedisDao;
import com.maven.demo.DAO.SeckillDao;
import com.maven.demo.DAO.SuccessKilledDao;
import com.maven.demo.dto.Exposer;
import com.maven.demo.dto.SeckillExecution;
import com.maven.demo.entity.Seckill;
import com.maven.demo.entity.SuccessKilled;
import com.maven.demo.enums.SeckillStatEnum;
import com.maven.demo.exception.RepeatKillException;
import com.maven.demo.exception.SeckillCloseException;
import com.maven.demo.exception.SeckillException;
import com.maven.demo.service.SeckillService;

@Service
public class SeckillServiceImpl implements SeckillService {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private final String slat = "$jfdlk#$%sajflkds*";
	
	
	@Autowired
	private SeckillDao seckillDao;
	
	@Autowired
	private SuccessKilledDao successKilledDao;
	
	@Autowired
	private RedisDao redisDao;

	@Override
	public List<Seckill> getSeckillList() {
		return this.seckillDao.queryAll(0, 4);
	}

	@Override
	public Seckill getById(long seckillId) {
		return this.seckillDao.queryById(seckillId);
	}

	@Override
	public Exposer exportSeckillUrl(long seckillId) {
		Seckill seckill = null;
		seckill = this.redisDao.getSeckill(seckillId);
		if(seckill == null) {
			seckill = this.seckillDao.queryById(seckillId);
			if(seckill == null) {
				return new Exposer(false, seckillId);
			}
			this.redisDao.putSeckill(seckill);
		}
		Date startTime = seckill.getStartTime();
		Date endTime = seckill.getEndTime();
		Date nowTime = new Date();
		if(nowTime.getTime() < startTime.getTime() || nowTime.getTime() > endTime.getTime()) {
			return new Exposer(false, seckillId, nowTime.getTime(), startTime.getTime(), endTime.getTime());
		}
		String md5 = getMD5(seckillId);
		return new Exposer(true, md5, seckillId);
	}
	
	private String getMD5(long seckillId) {
		String base = seckillId + "/" + slat;
		String md5 = DigestUtils.md5DigestAsHex(base.getBytes());
		return md5;
	}

	@Override
	@Transactional
	/**
	 * 使用注解控制事务方法的有点：
	 * 1：开发团队打成一致约定，明确标注事务方法的编程风格
	 * 2：保证事务方法的执行时间尽可能的短，不要穿插其他网络操作，比如：RPC/HTTP请求。如果有这些操作，请剥离到事务方法的外部
	 * 3：不是所有的方法都需要事务。比如：只有一条修改操作，只读操作等不需要事务控制
	 */
	public SeckillExecution executeSeckill(long seckillId, long userPhone,
			String md5) throws SeckillException, SeckillCloseException,
			RepeatKillException {
		if(md5 == null || !md5.equals(getMD5(seckillId))) {
			throw new SeckillException("seckill data rewrite");
		}
		Date nowTime = new Date();
		try {
			int updateCt = seckillDao.reduceNumber(seckillId, nowTime);
			if(updateCt <= 0) {
				throw new SeckillCloseException("seckill is closed");
			} else {
				int insertCt = successKilledDao.insertSuccessKilled(seckillId, userPhone);
				if(insertCt <= 0) {
					throw new RepeatKillException("seckill repeated");
				} else {
					SuccessKilled sk = successKilledDao.queryByIdWithSeckill(seckillId, userPhone);
					return new SeckillExecution(seckillId, SeckillStatEnum.SUCCESS, sk);
				}
			}
		} catch(SeckillCloseException e1) {
			throw e1;
		} catch(RepeatKillException e2) {
			throw e2;
		} catch(Exception e) {
			logger.error(e.getMessage(), e);
			throw new SeckillException("seckill inner error:" + e.getMessage());
		}
		
	}

	@Override
	public SeckillExecution executeSeckillProcedure(long seckillId,
			long userPhone, String md5) {
		if(md5 == null || !md5.equals(getMD5(seckillId))) {
			throw new SeckillException("seckill data rewrite");
		}
		Date killTime = new Date();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("seckillId", seckillId);
		map.put("phone", userPhone);
		map.put("killTime", killTime);
		map.put("result", null);
		try {
			this.seckillDao.killByProcedure(map);
			int result = MapUtils.getInteger(map, "result", -2);
			if(result == 1) {
				SuccessKilled sk = this.successKilledDao.queryByIdWithSeckill(seckillId, userPhone);
				return new SeckillExecution(seckillId, SeckillStatEnum.SUCCESS, sk);
			} else {
				return new SeckillExecution(seckillId, SeckillStatEnum.stateOf(result));
			}
		} catch(Exception e) {
			e.printStackTrace();
			return new SeckillExecution(seckillId, SeckillStatEnum.INNER_ERROR);
		}
	}

	@Override
	public List<Seckill> getPageList(int startIndex, int pageLength,
			String killName) {
		List<Seckill> list = seckillDao.getPageList(startIndex, pageLength, killName);
		return list;
	}

	@Override
	public long getTotalCount(String killName) {
		long totalCt = this.seckillDao.getTotalCount(killName);
		return totalCt;
	}

}
