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

import java.util.Collection;
import java.util.List;
import java.util.function.Function;

import org.yx.common.KeyValuePair;
import org.yx.http.act.HttpActionInfo;
import org.yx.http.act.HttpActionNode;

public interface HttpActionSelector {

	/**
	 * 用于初始化，在sumk启动的时候调用一次。
	 * 
	 * @param infos
	 *            里面的key是解析后的act，跟@Web中定义的格式可能不同
	 * @param nameResolver
	 *            名称解析器
	 */
	void init(List<KeyValuePair<HttpActionNode>> infos, Function<String, String> nameResolver);

	/**
	 * 根据真正的act获取HttpActionInfo。对于url含参数等场景，可以返回HttpActionInfo子类
	 * 
	 * @param act
	 *            解析后的act
	 * @param method
	 *            http请求的方法，比如GET、POST，参加HttpMethod接口
	 * @return 如果没找到，可以返回默认servlet，也可以返回null
	 */
	HttpActionInfo getHttpInfo(String act, String method);

	/**
	 * 里面的接口是不重复的，但可能存在name一样，method不一样的情况。 为了便于查看，建议对里面的顺序做排序
	 * 
	 * @return 返回所有的接口列表，逗号隔开的会被当成多个。
	 */
	Collection<HttpActionInfo> actions();
}
