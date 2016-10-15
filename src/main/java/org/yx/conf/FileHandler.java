package org.yx.conf;

import java.io.InputStream;
import java.net.URL;

/**
 * 监听文件的变更，并且映射文件的内容
 * 
 * @author youtl
 *
 */
public interface FileHandler {
	/**
	 * 列出需要监听的文件。 如果是监听某个文件夹下的某一类型文件，可以在内部进行list并过滤
	 * 
	 * @return
	 */
	URL[] listFile();

	void deal(InputStream in) throws Exception;
}
