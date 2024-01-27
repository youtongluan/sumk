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
package org.yx.db.enums;

/**
 * 无论是数据库主键，还是redis主键，都不允许为null
 * 
 * @author 游夏
 *
 */
public enum ColumnType {
	NORMAL(false, false, 100),
	/**
	 * 数据库主键。不允许为null。在更新的时候，如果没有显式设置where条件，主键字段将不会被更新。<BR>
	 */
	ID_DB(true, false, 1),
	/**
	 * redis 主键，不允许为null
	 */
	ID_CACHE(false, true, 2),
	/**
	 * 既是数据库主键，也是redis主键。不允许为null，不会被更新
	 */
	ID_BOTH(true, true, 1);

	private final boolean dbID;
	private final boolean cacheID;
	private final int order;

	public int order() {
		return this.order;
	}

	public boolean isDbID() {
		return dbID;
	}

	public boolean isCacheID() {
		return cacheID;
	}

	private ColumnType(boolean dbId, boolean cacheId, int order) {
		this.dbID = dbId;
		this.cacheID = cacheId;
		this.order = order;
	}
}
