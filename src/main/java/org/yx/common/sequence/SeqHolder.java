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

import java.util.Objects;

import org.yx.conf.AppInfo;
import org.yx.log.ConsoleLog;

public class SeqHolder {
	private static Seq inst = new SeqImpl();
	static {
		int snow = AppInfo.getInt("sumk.counter.snow", Integer.MIN_VALUE);
		if (snow != Integer.MIN_VALUE) {
			inst.setCounter(new SnowflakeCounter(snow));
			ConsoleLog.get("sumk.seq").debug("use snow counter");
		}
	}

	public static Seq inst() {
		return inst;
	}

	public static void setSeq(Seq seq) {
		SeqHolder.inst = Objects.requireNonNull(seq);
	}

}
