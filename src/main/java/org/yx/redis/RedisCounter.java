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

import org.yx.common.SeqCounter;

public final class RedisCounter implements SeqCounter {

	private final Redis redis;

	@Override
	public int count(String name) throws Exception {
		return redis.incr("SEQ_GLOBAL_FOR_ALL").intValue();
	}

	public RedisCounter(Redis redis) {
		this.redis = redis;
	}

}
