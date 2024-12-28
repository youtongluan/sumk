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

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.yx.annotation.Exclude;
import org.yx.bean.ParallelBootWatcher;
import org.yx.common.util.S;
import org.yx.db.spec.DBSpecs;
import org.yx.db.spec.TableSpec;
import org.yx.log.Logs;

public class TableBootWatcher extends ParallelBootWatcher {

	public TableBootWatcher() {
		DBSettings.init();
	}

	@Override
	protected void handle(Class<?> clz) throws Exception {
		TableSpec spec = DBSpecs.extractTable(clz);
		if (spec == null) {
			return;
		}
		List<ColumnMeta> columns = this.extractColumns(clz);
		PojoMetaHolder.register(clz, spec, columns);
	}

	public List<ColumnMeta> extractColumns(Class<?> pojoClz) {
		Field[] fs = S.bean().getFields(pojoClz);
		Map<String, Field> map = new LinkedHashMap<>();
		for (Field f : fs) {
			if (f.isAnnotationPresent(Exclude.class)) {
				continue;
			}
			map.putIfAbsent(f.getName(), f);
		}
		Collection<Field> set = map.values();
		List<ColumnMeta> list = new ArrayList<>(set.size());
		for (Field f : set) {
			if (!f.isAccessible()) {
				f.setAccessible(true);
			}
			list.add(new ColumnMeta(f, DBSpecs.extractColumn(pojoClz, f)));
		}
		if (list.isEmpty()) {
			Logs.db().debug("{}'s column is empty", pojoClz.getName());
			return Collections.emptyList();
		}
		Collections.sort(list);
		return list;
	}

	@Override
	public int order() {
		return 2000;
	}

}
