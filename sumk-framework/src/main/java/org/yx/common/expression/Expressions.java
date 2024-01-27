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

import java.util.Collection;
import java.util.Map;
import java.util.function.Predicate;

import org.yx.exception.SumkException;

public class Expressions {
	public static SimpleExpression createSimpleExpression(String key, String matchType) {
		if (key == null || (key = key.trim()).isEmpty()) {
			throw new SumkException(-345565, "参数有误:" + key);
		}
		switch (matchType) {
		case MatchType.FALSE_BY_NULL:
			return new NotNullExpression(key);
		case MatchType.FALSE_BY_NOKEY:
			return new HasKeyExpression(key);
		case MatchType.FALSE_BY_EMPTY:
			return new NotEmptyExpression(key);
		default:
			throw new SumkException(-3455651, matchType + "类型不正确");
		}
	}

	public static Predicate<Map<String, Object>> booleanExpression(Collection<Predicate<Map<String, Object>>> exps,
			boolean and) {
		if (exps == null || exps.isEmpty()) {
			return null;
		}
		if (exps.size() == 1) {
			return exps.iterator().next();
		}
		return and ? new AndExpression(exps) : new OrExpression(exps);
	}

	public static Predicate<Map<String, Object>> and(Collection<Predicate<Map<String, Object>>> exps) {
		return booleanExpression(exps, true);
	}

	public static Predicate<Map<String, Object>> or(Collection<Predicate<Map<String, Object>>> exps) {
		return booleanExpression(exps, false);
	}
}
