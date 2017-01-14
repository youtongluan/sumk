/**
 * Copyright (C) 2016 - 2017 youtongluan.
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
package org.yx.bean.watcher;

import org.yx.bean.Bean;
import org.yx.bean.Plugin;
import org.yx.conf.AppInfo;
import org.yx.redis.Redis;
import org.yx.redis.RedisCounter;
import org.yx.redis.RedisPool;
import org.yx.util.SeqUtil;

@Bean
public class SeqCounterBuilder implements Plugin {

	@Override
	public void start() {
		Redis counter = RedisPool.getRedisExactly(AppInfo.get("sumk.counter.name", "counter"));
		if (counter == null) {
			counter = RedisPool.getRedisExactly("session");
		}
		if (counter != null) {
			SeqUtil.setCounter(new RedisCounter(counter));
		}
	}

	@Override
	public void stop() {

	}

}
