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

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

import org.yx.exception.SumkException;

public class OrExpression implements ParamExpression {

	private final ParamExpression[] exps;

	public OrExpression(Collection<ParamExpression> exps) {
		if (exps == null || exps.isEmpty()) {
			throw new SumkException(-34554565, "ParamExpression列表不能为空");
		}
		this.exps = exps.toArray(new ParamExpression[exps.size()]);
	}

	@Override
	public boolean test(Map<String, Object> map) {
		for (ParamExpression exp : exps) {
			if (exp.test(map)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String toString() {
		return "OR" + Arrays.toString(exps);
	}

}
