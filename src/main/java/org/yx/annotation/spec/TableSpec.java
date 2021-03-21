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
package org.yx.annotation.spec;

import java.util.Objects;

import org.yx.db.enums.CacheType;

public class TableSpec {
	private final String value;
	private final int duration;
	private final String preInCache;
	private final int maxHit;
	private final CacheType cacheType;

	public TableSpec(String value, int duration, String preInCache, int maxHit, CacheType cacheType) {
		this.value = value;
		this.duration = duration;
		this.preInCache = preInCache;
		this.maxHit = maxHit;
		this.cacheType = Objects.requireNonNull(cacheType);
	}

	/**
	 * @return 表名。为空时，就是表名，支持#或者?作为通配符
	 */
	public String value() {
		return value;
	}

	/**
	 * @return 在缓存中保留的时间,单位秒。0表示使用全局设置，小于0表示不过期
	 */
	public int duration() {
		return duration;
	}

	/**
	 * 如果使用cluster，同一张表的DB缓存会在一个slot上，这是为了防止mget、mset出问题
	 * 
	 * @return 为空使用表名，一般使用默认就好。支持#或者?作为通配符
	 */
	public String preInCache() {
		return preInCache;
	}

	/**
	 * @return 访问多少次之后刷新缓存，0表示使用全局默认，小于0表示不刷新
	 */
	public int maxHit() {
		return maxHit;
	}

	/**
	 * @return 主键缓存都是SINGLE，外键缓存一般用LIST
	 */
	public CacheType cacheType() {
		return cacheType;
	}

}
