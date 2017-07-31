package com.maven.demo.dto;

import com.maven.demo.entity.SuccessKilled;
import com.maven.demo.enums.SeckillStatEnum;

public class SeckillExecution {
	
	private long seckillId;
	
	private int state;
	
	private String stateInfo;
	
	private SuccessKilled successKilled;

	public SeckillExecution() {
		super();
		// TODO Auto-generated constructor stub
	}

	public SeckillExecution(long seckillId, SeckillStatEnum statEnum,
			SuccessKilled successKilled) {
		super();
		this.seckillId = seckillId;
		this.state = statEnum.getState();
		this.stateInfo = statEnum.getStateInfo();
		this.successKilled = successKilled;
	}

	public SeckillExecution(long seckillId, SeckillStatEnum statEnum) {
		super();
		this.seckillId = seckillId;
		this.state = statEnum.getState();
		this.stateInfo = statEnum.getStateInfo();
	}

	public long getSeckillId() {
		return seckillId;
	}

	public void setSeckillId(long seckillId) {
		this.seckillId = seckillId;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getStateInfo() {
		return stateInfo;
	}

	public void setStateInfo(String stateInfo) {
		this.stateInfo = stateInfo;
	}

	public SuccessKilled getSuccessKilled() {
		return successKilled;
	}

	public void setSuccessKilled(SuccessKilled successKilled) {
		this.successKilled = successKilled;
	}

}
