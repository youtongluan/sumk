package org.yx.redis;

import org.yx.common.SeqCounter;

public class RedisCounter implements SeqCounter {

	private final Redis redis;

	@Override
	public int count(String name) throws Exception {
		int v = redis.incr("SEQ_GLOBAL_FOR_ALL").intValue();
		return v;
	}

	public RedisCounter(Redis redis) {
		super();
		this.redis = redis;
	}

}
