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
package org.yx.http;

import org.yx.conf.AppInfo;

public interface HttpHeader {
	/**
	 * 用户的sessionId<br>
	 * sid
	 */
	String SESSIONID = AppInfo.get("http.header.sid", "sid");
	/**
	 * 客户端用来传userid或者其它唯一识别用户的标识<br>
	 * stoken
	 */
	String TOKEN = AppInfo.get("http.header.stoken", "stoken");

	/**
	 * http请求的类型，要类型一致才能访问
	 */
	String TYPE = AppInfo.get("http.header.stype", "stype");
}
