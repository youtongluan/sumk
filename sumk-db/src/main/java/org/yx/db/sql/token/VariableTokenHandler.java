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
package org.yx.db.sql.token;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * 大小写不敏感
 * 
 * @author 游夏
 *
 */
public class VariableTokenHandler implements MapedSqlTokenParser.TokenHandler {

	private final Function<String, Object> valueHandler;

	public static VariableTokenHandler createByMap(Map<String, Object> map) {
		return new VariableTokenHandler(new MapValueHandler(map));
	}

	public static VariableTokenHandler create(Function<String, Object> valueHandler) {
		return new VariableTokenHandler(valueHandler);
	}

	public VariableTokenHandler(Function<String, Object> valueHandler) {
		this.valueHandler = valueHandler;
	}

	@Override
	public String handleToken(String content, List<Object> paramters) {
		paramters.add(valueHandler.apply(content));
		return "?";
	}
}