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

public enum ValidRecord {
	/**
	 * 在SoftDelete中，跟VALID相同的表示有效，其它都是无效
	 */
	EQUAL_VALID,

	/**
	 * 在SoftDelete中，只要跟INVALID不同都是有效
	 */
	NOT_INVALID
}
