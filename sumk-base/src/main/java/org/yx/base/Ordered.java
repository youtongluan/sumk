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
package org.yx.base;

public interface Ordered extends Comparable<Ordered> {

	public static int DEFAULT_ORDER = 100;

	/**
	 * 升序，值越大，优先级越低。一般不采用负数
	 * 
	 * @return 索引值
	 */
	default int order() {
		return DEFAULT_ORDER;
	}

	@Override
	default int compareTo(Ordered o) {
		return Integer.compare(this.order(), o.order());
	}

}
