package org.yx.rpc.server;

public interface MinaHandler {
	/**
	 * 
	 * @param type
	 *            消息分类，比如product或者merchant等
	 * @return true表示支持本分类
	 *
	 * @author youxia
	 */
	boolean accept(String type);

	/**
	 * 
	 * 
	 * @param message
	 *            消息主题
	 * @return 返回的消息。
	 * @throws Exception
	 *
	 * @author youxia
	 */
	String received(String message) throws Exception;
}
