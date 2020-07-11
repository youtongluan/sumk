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
package org.yx.db.sql;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.yx.common.ItemJoiner;
import org.yx.db.event.DBEvent;

public class MapedSql {

	public MapedSql() {
		this.paramters = new ArrayList<>();
	}

	public MapedSql(String sql, List<Object> paramters) {
		this.sql = sql;
		this.paramters = Objects.requireNonNull(paramters);
	}

	String sql;
	private List<Object> paramters;
	DBEvent event;

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public List<Object> getParamters() {
		return paramters;
	}

	public void addParam(Object p) {
		this.paramters.add(p);
	}

	public void addParams(List<Object> ps) {
		if (ps == null || ps.isEmpty()) {
			return;
		}
		this.paramters.addAll(ps);
	}

	public DBEvent getEvent() {
		return event;
	}

	public void setEvent(DBEvent event) {
		this.event = event;
	}

	@Override
	public String toString() {
		return sql + " -- " + paramters;
	}

	public static MapedSql merge(List<MapedSql> mapeds, ItemJoiner joiner) {
		if (mapeds == null || mapeds.isEmpty()) {
			return null;
		}
		List<Object> params = new ArrayList<>();
		for (MapedSql maped : mapeds) {
			joiner.item().append(maped.sql);
			params.addAll(maped.paramters);
		}

		return new MapedSql(joiner.toCharSequence(true).toString(), params);
	}
}
