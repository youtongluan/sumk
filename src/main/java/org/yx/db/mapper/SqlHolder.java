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

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

import org.yx.conf.AppInfo;
import org.yx.conf.LocalMultiResourceLoaderSupplier;
import org.yx.conf.MultiResourceLoader;
import org.yx.exception.SumkException;

public class SqlHolder {
	private static Map<String, SqlParser> SQLS = new HashMap<>();

	public static void setSQLS(Map<String, SqlParser> sqls) {
		SQLS = sqls;
	}

	private static XmlBuilderFactory documentBuilderFactory = new XmlBuilderFactoryImpl();

	private static Supplier<MultiResourceLoader> resourceLoader = new LocalMultiResourceLoaderSupplier(
			AppInfo.get("sumk.db.sql.path", AppInfo.CLASSPATH_URL_PREFIX + "sql"));

	public static Supplier<MultiResourceLoader> resourceLoader() {
		return resourceLoader;
	}

	public static void resourceLoader(Supplier<MultiResourceLoader> resourceLoader) {
		SqlHolder.resourceLoader = Objects.requireNonNull(resourceLoader);
	}

	public static XmlBuilderFactory documentBuilderFactory() {
		return documentBuilderFactory;
	}

	public static void documentBuilderFactory(XmlBuilderFactory documentBuilderFactory) {
		SqlHolder.documentBuilderFactory = Objects.requireNonNull(documentBuilderFactory);
	}

	public static SqlParser findSql(String name) {
		SqlParser sql = SQLS.get(name);
		if (sql == null) {
			SumkException.throwException(64342451, "sql [" + name + "] can not found ");
		}
		return sql;
	}
}
