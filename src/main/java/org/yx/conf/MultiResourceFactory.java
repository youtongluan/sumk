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
