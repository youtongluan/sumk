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
import org.yx.conf.AppInfo;

public abstract class AbstractParamInfo implements ParameterInfo {
	private final ParamSpec param;
	private final boolean required;
	private final boolean complex;
	private final boolean maybeCheck;

	public AbstractParamInfo(ParamSpec param, Class<?> type) {
		this.param = Objects.requireNonNull(param);
		this.required = param.required() && AppInfo.getBoolean("sumk.param.required.enable", true);
		this.complex = Validators.supportComplex(Objects.requireNonNull(type)) && param.complex();

		this.maybeCheck = this.required || this.complex || param.max() >= 0 || param.min() >= 0
				|| param.custom() != null;
	}

	@Override
	public Object custom() {
		return this.param.custom();
	}

	@Override
	public String example() {
		return this.param.example();
	}

	@Override
	public String comment() {
		return this.param.comment();
	}

	public ParamSpec getParam() {
		return param;
	}

	public boolean isRequired() {
		return required;
	}

	public int getMax() {
		return param.max();
	}

	public int getMin() {
		return param.min();
	}

	@Override
	public String getCnName() {
		return this.param.value();
	}

	public boolean isComplex() {
		return complex;
	}

	public boolean maybeCheck() {
		return maybeCheck;
	}
}
