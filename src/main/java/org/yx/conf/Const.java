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
package org.yx.conf;

import java.lang.reflect.Modifier;

public final class Const {
	public static final int DEFAULT_ORDER = 100;

	public static final String DEFAULT_DB_NAME = "sumk";

	public static final int SUMK_VERSION = 0x300;

	public static String sumkVersion() {
		return new StringBuilder(10).append((Const.SUMK_VERSION >> 8) & 0x0F).append('.')
				.append((Const.SUMK_VERSION >> 4) & 0x0F).append('.').append(Const.SUMK_VERSION & 0x0F).toString();
	}

	public static final String ZK_URL = "sumk.zkurl";

	public static final String KEY_STORE_PATH = "sumk.webserver.ssl.keyStore";

	public static final int NOT_PARSE_BEAN_FIELD = Modifier.STATIC | Modifier.TRANSIENT | Modifier.FINAL;

	/**
	 * 分号 ;
	 */
	public static final String SEMICOLON = ";";
	/**
	 * 逗号 ,
	 */
	public static final String COMMA = ",";

	public static final String CONFIG_NEW_LINE = "\t\\\n\t";

	public static final String LN = "\n";

	public static final int DEFAULT_TOPLIMIT = 50000;

	public static final String LISTENER_DB_MODIFY = "LISTENER_DB_MODIFY";
	public static final String LISTENER_DB_QUERY = "LISTENER_DB_QUERY";
}
