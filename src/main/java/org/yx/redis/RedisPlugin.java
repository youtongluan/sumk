/**
 * Copyright (C) 2016 - 2030 youtongluan.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 		http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.yx.redis;

import java.util.concurrent.TimeUnit;

import org.yx.annotation.Bean;
import org.yx.bean.Plugin;
import org.yx.conf.AppInfo;
import org.yx.log.ConsoleLog;
import org.yx.log.Log;
import org.yx.main.SumkThreadPool;
import org.yx.util.SeqUtil;

@Bean
public class RedisPlugin implements Plugin {

	@Override
	public void startAsync() {
		try {
			if (AppInfo.subMap("s.redis.").isEmpty()) {
				return;
			}
			Class.forName("redis.clients.jedis.Jedis");
		} catch (Throwable e) {
			Log.get("sumk.redis").warn("Jedis is not in use because of " + e.getMessage());
			return;
		}
		try {
			ConsoleLog.get("sumk.SYS").debug("redis pool init");
			RedisLoader.init();
			initSeqUtilCounter();
			SumkThreadPool.scheduledExecutor.scheduleWithFixedDelay(RedisChecker.get(), 5,
					AppInfo.getInt("sumk.redis.check.period", 5), TimeUnit.SECONDS);
		} catch (Exception e) {
			Log.get("sumk.redis").error(e.getMessage(), e);
			System.exit(-1);
		}
	}

	private static void initSeqUtilCounter() {
		if (SeqUtil.getCounter() != null) {
			return;
		}
		Redis redis = RedisPool.getRedisExactly(AppInfo.get("sumk.counter.name", "counter"));
		if (redis == null) {
			redis = RedisPool.getRedisExactly("session");
		}
		if (redis != null) {
			ConsoleLog.get("sumk.redis").debug("use redis counter");
			SeqUtil.setCounter(new RedisCounter(redis));
		}
	}

	@Override
	public void stop() {

	}

}
