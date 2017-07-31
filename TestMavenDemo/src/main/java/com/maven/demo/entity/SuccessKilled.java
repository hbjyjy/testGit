package com.maven.demo.entity;

import java.io.Serializable;
import java.util.Date;

public class SuccessKilled implements Serializable {

	private static final long serialVersionUID = 5299622126598622508L;
	
	private long seckillId;
	
	private long userPhone;
	
	private short state;
	
	private Date createTime;
	
	private Seckill seckill;

	public SuccessKilled() {
		super();
		// TODO Auto-generated constructor stub
	}

	public SuccessKilled(long seckillId, long userPhone, short state,
			Date createTime, Seckill seckill) {
		super();
		this.seckillId = seckillId;
		this.userPhone = userPhone;
		this.state = state;
		this.createTime = createTime;
		this.seckill = seckill;
	}

	public long getSeckillId() {
		return seckillId;
	}

	public void setSeckillId(long seckillId) {
		this.seckillId = seckillId;
	}

	public long getUserPhone() {
		return userPhone;
	}

	public void setUserPhone(long userPhone) {
		this.userPhone = userPhone;
	}

	public short getState() {
		return state;
	}

	public void setState(short state) {
		this.state = state;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	public Seckill getSeckill() {
		return seckill;
	}

	public void setSeckill(Seckill seckill) {
		this.seckill = seckill;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((createTime == null) ? 0 : createTime.hashCode());
		result = prime * result + ((seckill == null) ? 0 : seckill.hashCode());
		result = prime * result + (int) (seckillId ^ (seckillId >>> 32));
		result = prime * result + state;
		result = prime * result + (int) (userPhone ^ (userPhone >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SuccessKilled other = (SuccessKilled) obj;
		if (createTime == null) {
			if (other.createTime != null)
				return false;
		} else if (!createTime.equals(other.createTime))
			return false;
		if (seckill == null) {
			if (other.seckill != null)
				return false;
		} else if (!seckill.equals(other.seckill))
			return false;
		if (seckillId != other.seckillId)
			return false;
		if (state != other.state)
			return false;
		if (userPhone != other.userPhone)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "SuccessKilled [seckillId=" + seckillId + ", userPhone="
				+ userPhone + ", state=" + state + ", createTime=" + createTime
				+ ", seckill=" + seckill + "]";
	}

}
