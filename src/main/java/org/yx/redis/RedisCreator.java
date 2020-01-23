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

import java.lang.reflect.Constructor;
import java.util.Objects;
import java.util.function.Function;

import org.yx.conf.AppInfo;
import org.yx.exception.SumkException;
import org.yx.log.Log;

public class RedisCreator implements Function<RedisConfig, Redis> {

	private Constructor<?> constructor;

	public Redis apply(RedisConfig config) {
		Objects.requireNonNull(config);
		if (config.getTryCount() < 1 || config.getTryCount() > 100) {
			throw new SumkException(54354354, "tryCount必须介于0和100之间,但它的实际值是" + config.getTryCount());
		}
		try {
			if (constructor == null) {
				constructor = getConstructor();
			}
			return (Redis) constructor.newInstance(config);
		} catch (Throwable e) {
			Log.printStack("sumk.redis", e);
			throw new SumkException(2345345, "create redis [" + config + "] failed!!");
		}
	}

	private Constructor<?> getConstructor() throws NoSuchMethodException, SecurityException, ClassNotFoundException {
		String clzName = AppInfo.get("sumk.redis.impl", "org.yx.redis.RedisImpl");
		Class<?> clz = Class.forName(clzName);
		Constructor<?> c = clz.getConstructor(RedisConfig.class);
		c.setAccessible(true);
		return c;
	}
}
