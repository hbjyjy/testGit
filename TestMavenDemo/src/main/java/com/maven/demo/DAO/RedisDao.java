package com.maven.demo.DAO;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import com.maven.demo.entity.Seckill;

public class RedisDao {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private RuntimeSchema<Seckill> schema = RuntimeSchema.createFrom(Seckill.class);
	
	private final JedisPool jedisPool;
	
	public RedisDao(String ip, int port) {
		jedisPool = new JedisPool(ip, port);
	}
	
	public Seckill getSeckill(long seckillId) {
		try{
			Jedis jedis = jedisPool.getResource();
			try {
				String key = "seckill:" + seckillId;
				byte[] bytes = jedis.get(key.getBytes());
				if(bytes != null) {
					Seckill seckill = schema.newMessage();
					ProtostuffIOUtil.mergeFrom(bytes, seckill, schema);
					return seckill;
				} else {
					return null;
				}
			} catch(Exception e) {
				e.printStackTrace();
				return null;
			} finally {
				jedis.close();
			}
		} catch(Exception e) {
			logger.error(e.getMessage(), e);
			return null;
		}
	}
	
	public String putSeckill(Seckill seckill) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			String key = "seckill:" + seckill.getSeckillId();
			byte[] bytes = ProtostuffIOUtil.toByteArray(seckill, schema,
					LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));
			int seconds = 60*60;
			String result = jedis.setex(key.getBytes(), seconds, bytes);
			return result;
		} catch(Exception e) {
			logger.error(e.getMessage(), e);
			return null;
		} finally {
			if(jedis != null) {
				jedis.close();
			}
		}
	}

}
