package org.yx.conf;

import java.io.InputStream;

/**
 * 根据特定的路径，获取唯一的资源信息
 * 
 * @author 游夏
 *
 */
public interface SingleResourceFactory {
	/**
	 * 如果资源不存在，就返回null。用完要记得关闭
	 * 
	 * @param dbName
	 * @return
	 * @throws Exception
	 */
	InputStream openInput(String dbName) throws Exception;
}
