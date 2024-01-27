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

import java.util.Objects;

public final class DBFactory {
	private static DBSupplier supplier = new DBSupplierImpl();

	public static void setSupplier(DBSupplier supplier) {
		DBFactory.supplier = Objects.requireNonNull(supplier);
	}

	public static Select select() {
		return supplier.select();
	}

	public static Insert insert() {
		return supplier.insert();
	}

	public static Update update() {
		return supplier.update();
	}

	public static Delete delete() {
		return supplier.delete();
	}

}
