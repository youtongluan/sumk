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
package org.yx.common.action;

import java.util.List;

import org.yx.validate.ParameterInfo;

public class ParamDescript {
	private String name;

	private String cnName;

	private Boolean required;

	private Integer max;

	private Integer min;

	private String type;
	private String example;
	private String comment;
	private Object custom;
	private Boolean complex;

	private List<ParamDescript> fields;
	private Boolean array;

	public List<ParamDescript> getComplexFields() {
		return fields;
	}

	public void setComplexFields(List<ParamDescript> list) {
		this.fields = list != null && list.size() > 0 ? list : null;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCnName() {
		return cnName;
	}

	public void setCnName(String cnName) {
		this.cnName = cnName;
	}

	public Boolean getRequired() {
		return required;
	}

	public void setRequired(Boolean required) {
		this.required = required;
	}

	public Integer getMax() {
		return max;
	}

	public void setMax(Integer max) {
		this.max = max;
	}

	public Integer getMin() {
		return min;
	}

	public void setMin(Integer min) {
		this.min = min;
	}

	public String getType() {
		return type;
	}

	public ParamDescript setType(String type) {
		this.type = type;
		return this;
	}

	public ParamDescript setType(Class<?> type) {
		String subfix = "";
		while (type.isArray()) {
			subfix += "[]";
			type = type.getComponentType();
		}
		this.type = type.getName() + subfix;
		return this;
	}

	public String getExample() {
		return example;
	}

	public void setExample(String example) {
		this.example = example;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Object getCustom() {
		return custom;
	}

	public void setCustom(String custom) {
		this.custom = custom;
	}

	public Boolean getComplex() {
		return complex;
	}

	public void setComplex(Boolean complex) {
		this.complex = complex;
	}

	public Boolean getArray() {
		return array;
	}

	public void setArray(Boolean array) {
		this.array = array;
	}

	public ParamDescript copyFrom(ParameterInfo info, boolean supportComplex) {
		if (info == null) {
			return this;
		}
		this.cnName = getValue(info.getCnName());
		this.name = getValue(info.getParamName());
		this.comment = getValue(info.comment());
		this.custom = info.custom();
		this.example = getValue(info.example());
		this.required = supportComplex && info.isRequired() ? true : null;
		this.complex = supportComplex && info.isComplex() ? true : null;
		this.max = supportComplex && info.getMax() >= 0 ? info.getMax() : null;
		this.min = supportComplex && info.getMin() >= 0 ? info.getMin() : null;
		return this;
	}

	private String getValue(String v) {
		return v == null || v.isEmpty() ? null : v;
	}

}
