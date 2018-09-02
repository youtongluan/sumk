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
package org.yx.db.exec;

import org.yx.db.DBType;

public class DBExec {

	private static void exec(ResultContainer container, DBExecutor executor, DBType dbType) throws Exception {

		DBSessionProxy.create(container, dbType).exec(executor);
	}

	public static void exec(ResultContainer container, DBExecutor executor) throws Exception {
		exec(container, executor, DBType.WRITE);
	}

	public static void execInAutoCommit(ResultContainer container, DBExecutor executor) throws Exception {
		DBSessionProxy.create(container, DBType.WRITE).execInAutoCommit(executor);
	}

	public static void execRWSplit(ResultContainer container, DBExecutor executor) throws Exception {
		exec(container, executor, DBType.ANY);
	}

	public static void query(ResultContainer container, DBExecutor executor) throws Exception {
		exec(container, executor, DBType.READ);
	}
}
