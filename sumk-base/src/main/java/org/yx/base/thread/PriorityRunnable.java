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
package org.yx.base.thread;

import java.util.Objects;

import org.yx.log.Log;

public class PriorityRunnable implements Runnable {

	private int priority;
	private final Runnable target;
	private final int threshold;

	public PriorityRunnable(Runnable target, int priority, int threshold) {
		this.target = Objects.requireNonNull(target, "target is null");
		this.priority = priority;
		this.threshold = threshold;
	}

	@Override
	public void run() {
		if (this.priority < threshold) {
			String msg = new StringBuilder().append("Task ").append(toString()).append(" discarded, because of ")
					.append(priority).append(" lower than ").append(threshold).toString();
			Log.get("sumk.thread").warn(msg);
			return;
		}
		target.run();
	}

	int priority() {
		return priority;
	}

}
