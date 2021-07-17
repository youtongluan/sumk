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

public class BeanSpec {
	private final String value;
	private final String conditionOnProperty;
	/**
	 * conditionOnProperty不为空的时候，本属性才有意义
	 */
	private final boolean onProperty;

	public BeanSpec(String value, String conditionOnProperty, boolean onProperty) {
		this.value = value;
		this.conditionOnProperty = conditionOnProperty;
		this.onProperty = onProperty;
	}

	public String value() {
		return this.value;
	}

	public String conditionOnProperty() {
		return this.conditionOnProperty;
	}

	public boolean onProperty() {
		return onProperty;
	}

}
