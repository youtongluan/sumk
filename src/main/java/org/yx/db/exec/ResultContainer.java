/**
 * Copyright (C) 2016 - 2017 youtongluan.
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

public class ResultContainer {
	Object result;
	private Object param;
	private String db;

	public static ResultContainer create(String db) {
		return new ResultContainer(db, null);
	}

	public static ResultContainer create(String db, Object param) {
		return new ResultContainer(db, param);
	}

	public ResultContainer(String db, Object param) {
		super();
		this.db = db;
		this.param = param;
	}

	public String getDb() {
		return db;
	}

	public Object getParam() {
		return param;
	}

	public void setParam(Object param) {
		this.param = param;
	}

	public Object getResult() {
		return result;
	}

}
