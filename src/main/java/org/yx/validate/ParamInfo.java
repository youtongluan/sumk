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
package org.yx.validate;

import org.yx.annotation.Param;

public class ParamInfo {
	final Param param;

	final String paramName;

	final Class<?> paramType;

	public ParamInfo(Param param, String paramName, Class<?> type) {
		this.param = param;
		this.paramName = paramName;
		this.paramType = type;
	}

	public String getParamName() {
		return paramName;
	}

	public Param getParam() {
		return param;
	}

	public Class<?> getType() {
		return paramType;
	}
}
