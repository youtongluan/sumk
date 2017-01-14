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
