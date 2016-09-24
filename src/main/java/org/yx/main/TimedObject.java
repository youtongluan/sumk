package org.yx.main;

public class TimedObject {

	private long evictTime;
	private Object target;

	public long getEvictTime() {
		return evictTime;
	}

	public void setEvictTime(long evictTime) {
		this.evictTime = evictTime;
	}

	public Object getTarget() {
		return target;
	}

	public void setTarget(Object target) {
		this.target = target;
	}

}
