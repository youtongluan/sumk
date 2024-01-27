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
package org.yx.annotation.spec;

import java.util.Objects;

public class ParamSpec {

	private final String value;
	private final boolean required;
	private final int max;
	private final int min;
	private final String example;
	private final String comment;
	private final boolean complex;
	private final Object custom;

	public ParamSpec(String value, boolean required, int max, int min, String example, String comment, boolean complex,
			Object custom) {
		this.value = Objects.requireNonNull(value);
		this.required = required;
		this.max = max;
		this.min = min;
		this.example = example;
		this.comment = comment;
		this.complex = complex;
		this.custom = custom;
	}

	/**
	 * @return 中文名称
	 * 
	 */
	public String value() {
		return this.value;
	}

	public boolean required() {
		return this.required;
	}

	public int max() {
		return this.max;
	}

	public int min() {
		return this.min;
	}

	public String example() {
		return this.example;
	}

	public String comment() {
		return this.comment;
	}

	public boolean complex() {
		return this.complex;
	}

	/**
	 * 这个属性是留给开发者自己扩展用
	 * 
	 * @return 这个属性尽量不要返回空字符串，应用null代替空字符串
	 */
	public Object custom() {
		return this.custom;
	}
}
