package org.yx.common;

public interface SeqCounter {
	/**
	 * 返回值可以是负数
	 * 
	 * @param name
	 */
	int count(String name) throws Exception;
}
