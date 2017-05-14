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
package org.yx.http.handler;

import java.lang.reflect.Method;

import org.yx.common.CalleeNode;
import org.yx.http.Upload;
import org.yx.http.Web;
import org.yx.validate.Param;

public final class HttpNode extends CalleeNode {

	public final Web action;
	public final Upload upload;
	private final String[] types;

	/**
	 * 判断是否接受该类型的访问
	 * 
	 * @param type
	 * @return
	 */
	public boolean acceptType(String type) {
		if (type == null) {
			type = "";
		}
		if (types == null) {
			return type.isEmpty();
		}
		for (String t : types) {
			if (type.equals(t)) {
				return true;
			}
		}
		return false;
	}

	public HttpNode(Object obj, Method m, Class<?> argClz, String[] argNames, Class<?>[] argTypes, Param[] params,
			Web action, Upload upload) {
		super(obj, m, argClz, argNames, argTypes, params);
		this.action = action;
		this.upload = upload;

		String[] _types = action != null ? action.type() : upload.type();
		this.types = _types.length == 0 || (_types.length == 1 && _types[0].isEmpty()) ? null : _types;
	}

}
