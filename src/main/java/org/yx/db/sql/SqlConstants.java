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
package org.yx.db.sql;

import org.yx.conf.AppInfo;
import org.yx.log.Log;

public class SqlConstants {

	/**
	 * 该属性表示用map做参数，但是map中的参数不能跟pojo中一一对应上
	 */
	static boolean FAIL_IF_PROPERTY_NOT_MAPPED;
	static {
		AppInfo.addObserver((a, b) -> {
			try {
				FAIL_IF_PROPERTY_NOT_MAPPED = AppInfo.getBoolean("sumk.sql.failIfPropertyNotMapped", true);
			} catch (Exception e) {
				Log.get("sumk.appInfo").info(e.getMessage(), e);
			}
		});
	}

}
