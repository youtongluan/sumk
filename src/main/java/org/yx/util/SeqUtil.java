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
package org.yx.util;

import org.yx.common.sequence.SeqHolder;

public final class SeqUtil {

	public static long next() {
		return SeqHolder.inst().next();
	}

	public static long next(String name) {
		return SeqHolder.inst().next(name);
	}

	public static String nextString() {
		return Long.toString(next(), Character.MAX_RADIX);
	}

	public static String nextString(String name) {
		return Long.toString(next(name), Character.MAX_RADIX);
	}

	public static long getTimeMillis(long seq) {
		return SeqHolder.inst().getTimeMillis(seq);
	}

	public static SumkDate toSumkDate(long seq) {
		return SumkDate.of(getTimeMillis(seq));
	}

	public static long from(SumkDate date) {
		return SeqHolder.inst().low(date);
	}

	public static long to(SumkDate date) {
		return SeqHolder.inst().high(date);
	}
}
