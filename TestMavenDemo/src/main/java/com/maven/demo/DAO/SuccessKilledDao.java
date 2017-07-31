package com.maven.demo.DAO;

import org.apache.ibatis.annotations.Param;

import com.maven.demo.entity.SuccessKilled;

public interface SuccessKilledDao {
	
	/**
	 * 插入购买明细，可过滤重复
	 * @param seckillId
	 * @param userphone
	 * @return
	 */
	int insertSuccessKilled(@Param("seckillId") long seckillId, @Param("userphone") long userphone);
	
	/**
	 * 根据ID查询SuccessKilled并携带秒杀产品对象实体
	 * @param seckillId
	 * @return
	 */
	SuccessKilled queryByIdWithSeckill(@Param("seckillId") long seckillId, @Param("userPhone") long userPhone);

}
