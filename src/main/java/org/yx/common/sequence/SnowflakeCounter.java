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
package org.yx.common.sequence;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

public class SnowflakeCounter implements SeqCounter {

	private final int snow;
	private final AtomicInteger seq;

	@Override
	public int count(String name) throws Exception {
		int random = seq.incrementAndGet() & 0xFFFF;
		random <<= 8;
		return snow | random;
	}

	public SnowflakeCounter(int snow) {
		this.snow = snow & 0xFF;
		seq = new AtomicInteger(ThreadLocalRandom.current().nextInt());
	}

}
