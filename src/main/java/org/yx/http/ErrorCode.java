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

/**
 * 1000-2000之间的错误码，为系统所保留。 应用系统的错误码，要避开这个区间
 */
public interface ErrorCode {
	/**
	 * 登陆失败
	 */
	int LOGINFAILED = 1001;
	/**
	 * session过期或冲突
	 */
	int SESSION_ERROR = 1002;

	/**
	 * 没有访问权限。一般是指该角色的权限不足
	 */
	int AUTHORITY_ERROR = 1003;

	/**
	 * 用户在其它地方登录了
	 */
	int LOGIN_AGAIN = 1004;

	/**
	 * 熔断
	 */
	int FUSING = 1005;

	/**
	 * 参数验证错误
	 */
	int VALIDATE_ERROR = AppInfo.getInt("http.errorcode.validate", 1010);

	/**
	 * type类型错误
	 */
	int TYPE_ERROR = AppInfo.getInt("http.errorcode.type", 1020);

	/**
	 * http.upload.enable被设置为false，<code>@upload</code>注解被禁用
	 */
	int UPLOAD_DISABLED = 1030;
}
