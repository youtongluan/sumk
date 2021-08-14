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
package org.yx.db.sql;

import org.yx.conf.AppInfo;

public class EstimateVisitCounter implements VisitCounter {

	private int visitCount;

	private int cacheMeet;

	private int modifyCount;

	private final int interval;

	private int queryCount;

	private int willVisit;

	public EstimateVisitCounter(int interval) {
		this.interval = interval > 0 ? interval : AppInfo.getInt("sumk.cache.count", 500);
		this.willVisit = this.interval;
	}

	@Override
	public long getCacheKeyVisits() {
		return Integer.toUnsignedLong(visitCount);
	}

	@Override
	public long getCacheKeyHits() {
		return Integer.toUnsignedLong(cacheMeet);
	}

	@Override
	public boolean willVisitCache(int c) {
		if (--willVisit <= 0) {
			this.willVisit = this.interval;
			return false;
		}
		visitCount += c;
		return true;
	}

	@Override
	public void incrCacheHit(int c) {
		cacheMeet += c;
	}

	@Override
	public long getModifyCount() {
		return Integer.toUnsignedLong(this.modifyCount);
	}

	@Override
	public void incrModifyCount() {
		this.modifyCount++;
	}

	@Override
	public long getQueryCount() {
		return Integer.toUnsignedLong(this.queryCount);
	}

	@Override
	public void incrQueryCount() {
		this.queryCount++;
	}

	@Override
	public int getInterval() {
		return this.interval;
	}

}
