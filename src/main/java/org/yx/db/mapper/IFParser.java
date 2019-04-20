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

import java.util.Map;
import java.util.Objects;

import org.yx.common.expression.ParamExpression;
import org.yx.db.sql.MapedSql;

public class IFParser implements SqlParser {

	private final SqlParser parser;
	private final ParamExpression expression;

	public static IFParser create(ParamExpression expression, SqlParser parser) {
		if (parser == null) {
			return null;
		}
		return new IFParser(Objects.requireNonNull(expression), parser);
	}

	private IFParser(ParamExpression expression, SqlParser parser) {
		this.expression = expression;
		this.parser = parser;
	}

	@Override
	public MapedSql toMapedSql(Map<String, Object> map) throws Exception {
		return expression.test(map) ? parser.toMapedSql(map) : null;
	}

	@Override
	public String toString() {
		return "IF(" + expression + "): " + this.parser;
	}

}
