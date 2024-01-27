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
package org.yx.db.spec;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.yx.annotation.db.Box;
import org.yx.annotation.db.Column;
import org.yx.annotation.db.Table;
import org.yx.annotation.spec.Specs;

public class DBSpecs extends Specs {

	private static Function<Class<?>, TableSpec> tableParser = clz -> {
		Table table = clz.getAnnotation(Table.class);
		if (table == null) {
			return null;
		}
		return new TableSpec(table.value(), table.duration(), table.preInCache(), table.maxHit(), table.cacheType());
	};
	private static BiFunction<Class<?>, Field, ColumnSpec> columnParser = (clz, f) -> {
		Column c = f.getAnnotation(Column.class);
		if (c == null) {
			return null;
		}
		return new ColumnSpec(c.value(), c.type(), c.order());
	};

	private static Function<Method, BoxSpec> boxParser = m -> {
		Box c = m.getAnnotation(Box.class);
		if (c == null) {
			return null;
		}
		return new BoxSpec(c.value(), c.dbType(), c.transaction());
	};

	public static TableSpec extractTable(Class<?> clz) {
		return parse(clz, tableParser);
	}

	public static ColumnSpec extractColumn(Class<?> clz, Field f) {
		return parse(clz, f, columnParser);
	}

	public static BoxSpec extractBox(Method m) {
		return parse(m, boxParser);
	}

	public static void setBoxParser(Function<Method, BoxSpec> boxParser) {
		DBSpecs.boxParser = Objects.requireNonNull(boxParser);
	}

	public static void setColumnParser(BiFunction<Class<?>, Field, ColumnSpec> columnParser) {
		DBSpecs.columnParser = Objects.requireNonNull(columnParser);
	}

	public static void setTableParser(Function<Class<?>, TableSpec> tableParser) {
		DBSpecs.tableParser = Objects.requireNonNull(tableParser);
	}

	public static Function<Class<?>, TableSpec> getTableParser() {
		return tableParser;
	}

	public static BiFunction<Class<?>, Field, ColumnSpec> getColumnParser() {
		return columnParser;
	}

	public static Function<Method, BoxSpec> getBoxParser() {
		return boxParser;
	}

}
