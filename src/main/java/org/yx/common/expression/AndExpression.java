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
import java.util.function.Predicate;

import org.yx.exception.SumkException;

public class AndExpression implements Predicate<Map<String, Object>> {

	private final Predicate<Map<String, Object>>[] exps;

	@SuppressWarnings("unchecked")
	public AndExpression(Collection<Predicate<Map<String, Object>>> exps) {
		if (exps == null || exps.isEmpty()) {
			throw new SumkException(-3455635, "ParamExpression列表不能为空");
		}
		this.exps = exps.toArray(new Predicate[exps.size()]);
	}

	@Override
	public boolean test(Map<String, Object> map) {
		for (Predicate<Map<String, Object>> exp : exps) {
			if (!exp.test(map)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public String toString() {

		return "AND" + Arrays.toString(exps);
	}

}
