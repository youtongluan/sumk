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

import java.util.Objects;

import org.yx.annotation.spec.ParamSpec;

public class ParamInfo extends AbstractParamInfo {

	private final String paramName;

	private final Class<?> paramType;

	private final boolean complex;

	public ParamInfo(ParamSpec param, String paramName, Class<?> type) {
		super(param);
		this.paramName = Objects.requireNonNull(paramName);
		this.paramType = Objects.requireNonNull(type);
		this.complex = param.complex();
	}

	public String getParamName() {
		return paramName;
	}

	public Class<?> getParamType() {
		return paramType;
	}

	public boolean isComplex() {
		return complex;
	}

}
