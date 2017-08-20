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
package org.yx.common;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ActStatis {

	private Map<String, Statis> actStatis = new ConcurrentHashMap<>();

	public void visit(String name, long time, boolean success) {
		Statis statis = actStatis.get(name);
		if (statis == null) {
			statis = new Statis(name);
			Statis tmp = actStatis.putIfAbsent(name, statis);
			if (tmp != null) {
				statis = tmp;
			}
		}
		if (success) {
			statis.successVisit(time);
		} else {
			statis.failedVisit(time);
		}
	}

	public Map<String, Statis> getAndreset() {
		Map<String, Statis> tmp = this.actStatis;
		actStatis = new ConcurrentHashMap<>();
		return tmp;
	}

	public Map<String, Statis> getAll() {
		return this.actStatis;
	}
}
