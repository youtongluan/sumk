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
import java.util.Objects;

import org.yx.common.ItemJoiner;
import org.yx.db.sql.MapedSql;

public class ComposeParser implements SqlParser {

	private final SqlParser[] parsers;

	ComposeParser(SqlParser[] parsers) {
		this.parsers = Objects.requireNonNull(parsers);
	}

	@Override
	public MapedSql toMapedSql(Map<String, Object> param) throws Exception {
		ItemJoiner joiner = ItemJoiner.create(" ", null, null);
		List<Object> list = new ArrayList<>();
		for (SqlParser parser : parsers) {
			MapedSql ms = parser.toMapedSql(param);
			if (ms == null) {
				continue;
			}
			joiner.item().append(ms.getSql());
			list.addAll(ms.getParamters());
		}
		return new MapedSql(joiner.toString(), list);
	}

	@Override
	public String toString() {
		ItemJoiner joiner = ItemJoiner.create("\t\n", null, null);
		for (SqlParser p : this.parsers) {
			joiner.item().append(String.valueOf(p));
		}
		return new StringBuilder().append("Compose [").append(joiner.toCharSequence()).append("]").toString();
	}

}
