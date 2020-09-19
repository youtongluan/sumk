/**
 * Copyright (C) 2016 - 2030 youtongluan.
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
package org.yx.http.select;

import java.util.List;
import java.util.Map;

import org.yx.http.act.HttpActionInfo;

public interface HttpActionSelector {

	/**
	 * 用于初始化，在sumk启动的时候调用一次。
	 * 
	 * @param acts
	 *            里面的key是解析后的act，跟@Web中定义的格式可能不同
	 */
	void init(Map<String, HttpActionInfo> acts);

	/**
	 * 根据真正的act获取HttpActionInfo
	 * 
	 * @param act
	 *            解析后的act
	 * @return 如果没找到，可以返回默认servlet，也可以返回null
	 */
	HttpActionInfo getHttpInfo(String act);

	/**
	 * @return 返回所有的接口列表（解析后的接口)
	 */
	List<String> actNames();
}
