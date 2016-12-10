package org.yx.conf;

import java.io.InputStream;
import java.util.Map;

/**
 * 根据一定的规则，获取批量的输入流
 * 
 * @author 游夏
 *
 */
public interface MultiResourceFactory {

	/**
	 * 
	 * @param dbName
	 * @return key一般是资源名，比如文件路径
	 * @throws Exception
	 */
	Map<String, InputStream> openInputs(String dbName) throws Exception;
}
