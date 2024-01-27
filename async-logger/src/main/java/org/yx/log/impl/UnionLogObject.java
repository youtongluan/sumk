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
package org.yx.log.impl;

import org.yx.annotation.doc.NotNull;
import org.yx.util.SumkDate;

/**
 * 调用toLog()方法，可以获取到json化后的日志体。<BR>
 * 如果要减少中间态String的生成数量，可以调用getLogContext()获取CharSequence类型的日志体。
 */
public class UnionLogObject {
	@NotNull
	protected final String name;

	@NotNull
	protected final SumkDate date;

	@NotNull
	protected final String log;

	public UnionLogObject(@NotNull String name, @NotNull SumkDate date, @NotNull String log) {
		this.name = name;
		this.date = date;
		this.log = log;
	}

	/**
	 * @return 日志名称
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return 日志打印时的时间
	 */
	public SumkDate getDate() {
		return date;
	}

	/**
	 * 一般是json结构，并且里面字段的顺序一般也是固定的。 默认第一个前2个是name和date
	 * 
	 * @return 格式化后的日志
	 */
	public String getLog() {
		return this.log;
	}
}
