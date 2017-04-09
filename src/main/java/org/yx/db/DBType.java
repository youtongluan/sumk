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
package org.yx.db;

import org.yx.exception.SumkException;

public enum DBType {

	WRITE(true, false),

	READ(false, true),

	READONLY(false, true),

	ANY(true, true);

	public static DBType parseFromConfigFile(String type) {
		String type2 = type.toLowerCase();
		switch (type2) {
		case "w":
		case "write":
			return WRITE;
		case "r":
		case "read":
		case "readonly":
			return READ;
		case "wr":
		case "rw":
		case "any":
			return ANY;
		default:
			SumkException.throwException(2342312, type + " is not correct db type");
			return null;
		}
	}

	private boolean writable;
	private boolean readable;

	private DBType(boolean writable, boolean readable) {
		this.writable = writable;
		this.readable = readable;
	}

	public boolean isWritable() {
		return writable;
	}

	public boolean isReadable() {
		return readable;
	}

}
