package org.yx.util;

public interface SeqCounter {
	/**
	 * 
	 * @param name
	 * @return 如果返回值<0,也表示请求出错
	 */
	
	int count(String name) throws Exception;
}
