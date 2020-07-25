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
package org.yx.http.user;

import java.util.Objects;

public class LoginObject {

	private final String errorMsg;

	private final String responseData;

	private final SessionObject sessionObj;

	public SessionObject getSessionObject() {
		return sessionObj;
	}

	public LoginObject(String responseData, SessionObject sessionObj, String errorMsg) {
		this.responseData = responseData;
		this.sessionObj = sessionObj;
		this.errorMsg = errorMsg;
	}

	public String getResponseData() {
		return responseData;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public static LoginObject fail(String errorMsg) {
		return new LoginObject(null, null, errorMsg);
	}

	public static LoginObject success(String responseData, SessionObject sessionObj) {
		return new LoginObject(responseData,
				Objects.requireNonNull(sessionObj, "sessionObject cannot be null when login successed"), null);
	}

}
