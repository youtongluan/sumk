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
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.yx.db.sql.MapedSql;

public class ItemsParser implements SqlParser {

	private final JoinerFactory joinFactory;
	private final SqlParser[] parsers;

	public static ItemsParser create(List<SqlParser> list, JoinerFactory joinFactory) {
		if (list == null || list.isEmpty()) {
			return null;
		}
		return new ItemsParser(list.toArray(new SqlParser[list.size()]), joinFactory);
	}

	protected ItemsParser(SqlParser[] parsers, JoinerFactory joinFactory) {
		this.parsers = Objects.requireNonNull(parsers);
		this.joinFactory = Objects.requireNonNull(joinFactory);
	}

	@Override
	public MapedSql toMapedSql(Map<String, Object> param) throws Exception {
		List<MapedSql> list = new ArrayList<>(this.parsers.length);
		for (SqlParser p : this.parsers) {
			MapedSql ms = p.toMapedSql(param);
			if (ms == null) {
				continue;
			}
			list.add(ms);
		}
		if (list.isEmpty()) {
			return null;
		}
		return MapedSql.merge(list, this.joinFactory.create());
	}

	@Override
	public String toString() {
		return "ItemsParser [joinFactory=" + joinFactory + ", parsers=" + Arrays.toString(parsers) + "]";
	}

}
