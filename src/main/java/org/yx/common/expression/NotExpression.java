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
package org.yx.common.expression;

import java.util.Map;
import java.util.Objects;

public class NotExpression implements ParamExpression {

	private final ParamExpression expression;

	public NotExpression(ParamExpression expression) {
		this.expression = Objects.requireNonNull(expression);
	}

	@Override
	public boolean test(Map<String, Object> param) {
		return !expression.test(param);
	}

	@Override
	public String toString() {
		return "NOT[" + expression.toString() + "]";
	}
}
