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
package org.yx.db.annotation;

public enum UpdateType {
	/**
	 * 根据用户传入的值
	 */
	CUSTOM,
	/**
	 * 不能更新
	 */
	NONE,
	/**
	 * 仅用于自增长，这个选项仅针对数字类型才有效
	 */
	INCR,
	/**
	 * 将当前时间填入数据库，该选项仅针对日期类型
	 */
	CURRENT_TIME
}
