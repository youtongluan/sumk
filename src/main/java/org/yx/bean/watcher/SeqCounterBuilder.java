package org.yx.bean.watcher;

import org.yx.conf.AppInfo;
import org.yx.redis.Redis;
import org.yx.redis.RedisCounter;
import org.yx.redis.RedisPool;
import org.yx.util.SeqUtil;

public class SeqCounterBuilder implements Runnable {

	@Override
	public void run() {
		Redis counter = RedisPool.getRedisExactly(AppInfo.get("sumk.counter.name", "counter"));
		if (counter == null) {
			counter = RedisPool.getRedisExactly("session");
		}
		if (counter != null) {
			SeqUtil.setCounter(new RedisCounter(counter));
		}
	}

}
