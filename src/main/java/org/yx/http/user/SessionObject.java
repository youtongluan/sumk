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

/**
 * 用户session,要有无构造参数的构造函数。
 */
public class SessionObject {

	protected String userId;
	private Long expiredTime;

	/**
	 * 返回用户id
	 * 
	 * @return 用户id，不能为null
	 */
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Long getExpiredTime() {
		return expiredTime;
	}

	/**
	 * 如果设置了过期时间，即使用户一直在操作， 当达到过期时间后，session也会被清理
	 * 
	 * @param expiredTime
	 *            最大的过期时间，精确到毫秒。null表示不自动清理
	 */
	public void setExpiredTime(Long expiredTime) {
		this.expiredTime = expiredTime;
	}
}
