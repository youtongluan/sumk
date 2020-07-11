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
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

import org.yx.db.sql.MapedSql;
import org.yx.db.sql.MapedSqlBuilder;
import org.yx.exception.SumkException;
import org.yx.log.Logs;

public class ForeachParser implements SqlParser {

	private final JoinerFactory joinFactory;
	private final String itemName;
	private final String collecitonName;
	/**
	 * #{@} 是列表中值的占位符
	 */
	private final String template;

	public static ForeachParser create(String collection, String itemName, String template, JoinerFactory joinFactory) {
		if (template == null) {
			return null;
		}
		if (itemName != null) {
			itemName = itemName.trim();
			if (itemName.isEmpty()) {
				itemName = null;
			}
		}
		return new ForeachParser(collection, itemName, template, joinFactory);
	}

	protected ForeachParser(String collecitonName, String itemName, String template, JoinerFactory joinFactory) {
		this.collecitonName = Objects.requireNonNull(collecitonName);
		this.itemName = itemName;
		this.template = Objects.requireNonNull(template).trim();
		this.joinFactory = Objects.requireNonNull(joinFactory);
	}

	@SuppressWarnings("unchecked")
	@Override
	public MapedSql toMapedSql(Map<String, Object> param) throws Exception {
		Object obj = param.get(collecitonName);
		if (obj == null) {
			if (Logs.db().isTraceEnabled()) {
				Logs.db().trace("{} is null", collecitonName);
			}
			return null;
		}
		if (Collection.class.isInstance(obj)) {
			Collection<?> list = (Collection<?>) obj;
			List<MapedSql> mapeds = new ArrayList<>(list.size());

			MapedSqlBuilder builder;
			for (Object v : list) {
				if (this.itemName == null) {
					builder = new MapedSqlBuilder(template, param);
				} else if (Map.class.isInstance(v)) {
					builder = new MapedSqlBuilder(template,
							new ComposeMapHandler(param, (Map<String, Object>) v, this.itemName + "."));
				} else {
					builder = new MapedSqlBuilder(template, new ComposeValueHandler(param, v, this.itemName));
				}
				MapedSql maped = builder.toMapedSql();
				if (maped != null) {
					mapeds.add(maped);
				}
			}
			if (mapeds.isEmpty()) {
				return null;
			}
			return MapedSql.merge(mapeds, joinFactory.create());
		}
		throw new SumkException(235345346, "field " + collecitonName + " is not a Collection instance,it's type is "
				+ obj.getClass().getSimpleName());
	}

	@Override
	public String toString() {
		return "Foreach [colleciton=" + collecitonName + ", item=" + itemName + ", joinFactory=" + joinFactory + " : "
				+ template + "]";
	}

	private static class ComposeMapHandler implements Function<String, Object> {

		private final Map<String, Object> globalMap;
		private final Map<String, Object> itemMap;
		private final String prefix;

		public ComposeMapHandler(Map<String, Object> globalMap, Map<String, Object> itemMap, String prefix) {
			this.globalMap = globalMap;
			this.itemMap = itemMap;
			this.prefix = prefix;
		}

		@Override
		public Object apply(String key) {
			if (key.startsWith(this.prefix)) {
				return this.itemMap.get(key.substring(this.prefix.length()));
			}
			return this.globalMap.get(key);
		}

	}

	private static class ComposeValueHandler implements Function<String, Object> {

		private final Map<String, Object> globalMap;
		private final Object item;
		private final String itemName;

		public ComposeValueHandler(Map<String, Object> globalMap, Object item, String itemName) {
			this.globalMap = globalMap;
			this.item = item;
			this.itemName = itemName;
		}

		@Override
		public Object apply(String key) {
			if (key.equals(itemName)) {
				return item;
			}
			return this.globalMap.get(key);
		}

	}
}
