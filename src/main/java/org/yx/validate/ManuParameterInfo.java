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

public class ManuParameterInfo implements ParameterInfo {

	private String paramName;
	private String cnName;
	private boolean required;
	private Integer max;
	private Integer min;
	private Class<?> paramType;
	private boolean complex;
	private String custom;
	private String example;
	private String comment;

	public boolean isComplex() {
		return complex;
	}

	public void setComplex(boolean complex) {
		this.complex = complex;
	}

	public String getParamName() {
		return paramName;
	}

	public void setParamName(String paramName) {
		this.paramName = paramName;
	}

	public String getCnName() {
		return cnName;
	}

	public void setCnName(String cnName) {
		this.cnName = cnName;
	}

	public boolean isRequired() {
		return required;
	}

	public void setRequired(boolean required) {
		this.required = required;
	}

	public int getMax() {
		return max == null ? -1 : max;
	}

	public void setMax(Integer max) {
		this.max = max;
	}

	public int getMin() {
		return min == null ? -1 : min;
	}

	public void setMin(Integer min) {
		this.min = min;
	}

	public Class<?> getParamType() {
		return paramType;
	}

	public void setParamType(Class<?> paramType) {
		this.paramType = paramType;
	}

	public String custom() {
		return custom;
	}

	public void setCustom(String custom) {
		this.custom = custom;
	}

	public String example() {
		return example;
	}

	public void setExample(String example) {
		this.example = example;
	}

	public String comment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
}
