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
 * select的where条件中，对于null的处理
 */
public enum CompareNullPolicy {
	/**
	 * equal和not的null都会被正确解析，其它的null比较会被忽略掉
	 */
	CONTINUE,

	/**
	 * 保持原来的方式，不处理。可能会产生a like null这种表达式
	 */
	AS_IT_IS,

	/**
	 * 因为它会失败，所以就直接抛出异常
	 */
	FAIL
}
