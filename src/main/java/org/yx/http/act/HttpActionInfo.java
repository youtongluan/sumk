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
package org.yx.http.act;

public interface HttpActionInfo extends Comparable<HttpActionInfo> {

	/**
	 * 原始路径
	 * 
	 * @return <code>@Web</code>里定义的原始路径
	 */
	String rawAct();

	HttpActionNode node();

	/**
	 * @return 解析后正式的名字
	 */
	String formalName();

	/**
	 * 是否接受当前请求。有些实现类为了性能考虑，不对act做校验。
	 * 
	 * @param act
	 *            格式化后的接口名,不为null
	 * @param method
	 *            请求的http方法，不为null
	 * @return true表示接受当前请求
	 */
	boolean match(String act, String method);

	default int compareTo(HttpActionInfo o) {
		return this.formalName().compareTo(o.formalName());
	}

}
