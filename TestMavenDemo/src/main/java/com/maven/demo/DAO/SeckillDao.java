package com.maven.demo.DAO;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.maven.demo.entity.Seckill;

public interface SeckillDao {
	
	/**
	 * 减库存
	 * @param seckillId
	 * @param killTime
	 * @return 
	 */
	int reduceNumber(@Param("seckillId") long seckillId, @Param("killTime") Date killTime);
	
	/**
	 * 根据ID查询秒杀对象
	 * @param seckillId
	 * @return
	 */
	Seckill queryById(long seckillId);
	
	/**
	 * 根据偏移量查询秒杀商品列表
	 * @param offet
	 * @param limit
	 * @return
	 */
	List<Seckill> queryAll(@Param("offet") int offet, @Param("limit") int limit);
	
	void killByProcedure(Map<String, Object> paramMap);
	
	List<Seckill> getPageList(@Param("startIndex") int startIndex, @Param("pageLength") int pageLength, @Param("killName") String killName);
	
	long getTotalCount(@Param("killName") String killName);

}
