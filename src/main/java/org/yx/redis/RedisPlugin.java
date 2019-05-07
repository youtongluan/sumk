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

import org.yx.annotation.Bean;
import org.yx.bean.Plugin;
import org.yx.conf.AppInfo;
import org.yx.log.ConsoleLog;
import org.yx.log.Log;

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
		} catch (Exception e) {
			Log.get("sumk.redis").error(e.getMessage(), e);
			System.exit(-1);
		}
	}

	@Override
	public void stop() {

	}

}
