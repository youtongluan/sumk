package org.yx.http;

public interface ErrorCode {
	/**
	 * 登陆失败
	 */
	int LOGINFAILED = 1001;
	/**
	 * session过期或冲突
	 */
	int SESSION_ERROR = 1002;
}
