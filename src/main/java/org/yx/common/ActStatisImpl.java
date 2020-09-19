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
package org.yx.common;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class ActStatisImpl implements ActStatis {

	private Map<String, StatisItem> actStatis;

	public ActStatisImpl(Map<String, StatisItem> actStatis) {
		this.actStatis = actStatis;
	}

	public ActStatisImpl() {
		this.actStatis = new ConcurrentHashMap<>();
	}

	public void setActStatis(Map<String, StatisItem> actStatis) {
		this.actStatis = Objects.requireNonNull(actStatis);
	}

	public void visit(String name, long time, boolean success) {
		StatisItem statis = actStatis.get(name);
		if (statis == null) {
			statis = new StatisItem(name);
			StatisItem tmp = actStatis.putIfAbsent(name, statis);
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

	public Map<String, StatisItem> getAndReset() {
		Map<String, StatisItem> tmp = this.actStatis;
		actStatis = new ConcurrentHashMap<>();
		return tmp;
	}

	public Map<String, StatisItem> getAll() {
		return this.actStatis;
	}
}
