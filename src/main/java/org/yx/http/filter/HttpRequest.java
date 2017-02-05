/**
 * Copyright (C) 2016 - 2017 youtongluan.
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
package org.yx.http.filter;

import java.util.Map;

import org.yx.common.ThreadContext;
import org.yx.http.HttpHolder;
import org.yx.http.handler.HttpInfo;

public class HttpRequest {
	private Object[] params;

	public HttpRequest(Object[] args) {
		super();
		this.params = args;
	}

	/**
	 * 一个空属性，便于开发人员进行各种传值
	 */
	public Map<String, Object> custom;

	public HttpInfo info() {
		return HttpHolder.getHttpInfo(getAct());
	}

	public String getAct() {
		return ThreadContext.get().getAct();
	}

	/**
	 * http调用的参数
	 * 
	 * @return
	 */
	public Object[] getParams() {
		return params;
	}

}
