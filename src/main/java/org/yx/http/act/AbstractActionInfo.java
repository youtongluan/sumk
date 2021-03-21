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

import java.util.Objects;

public abstract class AbstractActionInfo implements HttpActionInfo {
	protected final String rawAct;
	protected final HttpActionNode node;
	/**
	 * 如果要支持url中的参数，可以考虑从这里入手
	 */
	protected final String formalName;

	public AbstractActionInfo(String rawAct, HttpActionNode node, String formatedName) {
		this.rawAct = Objects.requireNonNull(rawAct);
		this.node = Objects.requireNonNull(node);
		this.formalName = Objects.requireNonNull(formatedName);
	}

	public String rawAct() {
		return rawAct;
	}

	public HttpActionNode node() {
		return node;
	}

	/**
	 * @return 解析后正式的名字
	 */
	public String formalName() {
		return formalName;
	}
}
