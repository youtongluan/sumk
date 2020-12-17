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

import java.lang.reflect.Field;
import java.util.Objects;

import org.yx.annotation.Param;

public class FieldParameterInfo extends AbstractParamInfo {

	protected final Field field;

	private final boolean complex;

	public FieldParameterInfo(Param param, Field field) {
		super(param);
		this.field = Objects.requireNonNull(field);
		this.complex = param.complex() && !field.getType().isPrimitive();
		this.field.setAccessible(true);
	}

	public String getParamName() {
		return field.getName();
	}

	public Class<?> getParamType() {
		return field.getType();
	}

	public boolean isComplex() {
		return complex;
	}

	public Field getField() {
		return field;
	}

}
