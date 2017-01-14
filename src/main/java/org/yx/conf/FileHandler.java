/**
 * Copyright (C) 2016 - 2017 youtongluan.
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
package org.yx.conf;

import java.io.InputStream;
import java.net.URL;

/**
 * 监听文件的变更，并且映射文件的内容
 * 
 * @author 游夏
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
