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
package org.yx.db.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import org.yx.common.expression.Expressions;
import org.yx.common.expression.SimpleExpression;
import org.yx.exception.SumkException;
import org.yx.util.StringUtil;

public final class SqlParsers {
	private static final String SPLIT_AND = ",";
	private static final String SPLIT_OR = "\\|";

	public static Predicate<Map<String, Object>> createParamExpression(String test, String matchType) {
		if (test == null || test.isEmpty()) {
			throw new SumkException(-34645465, "if中的test不能为空");
		}
		test = StringUtil.toLatin(test.trim());
		boolean and = test.contains(SPLIT_AND);
		boolean or = test.contains("|");
		if (and && or) {
			throw new SumkException(-34645465, "if的test不能同时出现,和|");
		}
		return createParamExpression(test, matchType, !or);
	}

	private static Predicate<Map<String, Object>> createParamExpression(String test, String matchType, boolean and) {
		String[] ss = and ? test.split(SPLIT_AND) : test.split(SPLIT_OR);
		List<Predicate<Map<String, Object>>> list = new ArrayList<>(ss.length);
		for (String s : ss) {
			s = s.trim();
			if (s.isEmpty()) {
				continue;
			}
			SimpleExpression exp = Expressions.createSimpleExpression(s, matchType);
			if (!list.contains(exp)) {
				list.add(exp);
			}
		}
		return Expressions.booleanExpression(list, and);
	}
}
