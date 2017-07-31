package com.maven.demo.service;

import java.util.List;

import com.maven.demo.dto.Exposer;
import com.maven.demo.dto.SeckillExecution;
import com.maven.demo.entity.Seckill;
import com.maven.demo.exception.RepeatKillException;
import com.maven.demo.exception.SeckillCloseException;
import com.maven.demo.exception.SeckillException;

public interface SeckillService {
	
	List<Seckill> getSeckillList();
	
	Seckill getById(long seckillId);
	
	Exposer exportSeckillUrl(long seckillId);
	
	SeckillExecution executeSeckill(long seckillId, long userPhone, String md5)
	throws SeckillException, SeckillCloseException, RepeatKillException;
	
	SeckillExecution executeSeckillProcedure(long seckillId, long userPhone, String md5);
	
	List<Seckill> getPageList(int startIndex, int pageLength, String killName);
	
	long getTotalCount(String killName);

}
