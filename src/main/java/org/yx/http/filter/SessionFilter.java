package org.yx.http.filter;

public interface SessionFilter {
	/**
	 * 获取当前的session的加密key
	 * @param sid
	 * @return null表示未登陆
	 */
	byte[] getkey(String sid);
	
	
}
