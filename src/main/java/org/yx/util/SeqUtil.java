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
package org.yx.util;

import org.yx.common.Seq;
import org.yx.common.SeqCounter;

public class SeqUtil {
	static Seq inst = new Seq();

	public static long next() {
		return inst.next();
	}

	public static long next(String name) {
		return inst.next(name);
	}

	public static long getDate(long seq) {
		return Seq.getDate(seq);
	}

	public static void setCounter(SeqCounter counter) {
		inst.setCounter(counter);
	}
}
