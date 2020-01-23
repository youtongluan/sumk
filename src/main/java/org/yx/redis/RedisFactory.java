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

import java.util.function.Function;

public class RedisFactory {

	private static Function<RedisConfig, Redis> creator;

	public static Function<RedisConfig, Redis> getCreator() {
		return creator;
	}

	public static void setCreator(Function<RedisConfig, Redis> creator) {
		RedisFactory.creator = creator;
	}

	public static Redis create(RedisConfig conf) {
		Function<RedisConfig, Redis> factory = creator;
		if (factory == null) {
			factory = new RedisCreator();
			RedisFactory.creator = factory;
		}
		return factory.apply(conf);
	}

}
