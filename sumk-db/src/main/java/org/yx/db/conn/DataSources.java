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
package org.yx.db.conn;

import java.util.Objects;

public final class DataSources {

	private static DataSourceManagerSelector managerSelector = new DefaultManagerSelector();

	public static void setManagerSelector(DataSourceManagerSelector selector) {
		DataSources.managerSelector = Objects.requireNonNull(selector);
	}

	public static DataSourceManagerSelector getManagerSelector() {
		return managerSelector;
	}

	public static SumkDataSource writeDataSource(String dbName) {
		return getManager(dbName).writeDataSource();
	}

	public static SumkDataSource readDataSource(String dbName) {
		return getManager(dbName).readDataSource();
	}

	public static DataSourceManager getManager(String dbName) {
		return managerSelector.select(dbName);
	}

}
