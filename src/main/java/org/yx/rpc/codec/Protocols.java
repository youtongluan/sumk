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
package org.yx.rpc.codec;

public class Protocols {
	/**
	 * 协议的前缀
	 */
	public final static int ONE = 0x01;
	public final static int TWO = 0x02;
	public final static int FOUR = 0x04;

	public final static int FORMAT_JSON = 0x010000;

	/**
	 * 将Response对象序列化为json
	 */
	public final static int RESPONSE_JSON = 0x1000;

	/**
	 * 以JSON方式传递参数
	 */
	public final static int REQ_PARAM_JSON = 0x0100;
	/**
	 * 按顺序传递参数
	 */
	public final static int REQ_PARAM_ORDER = 0x0200;

	/**
	 * 获取所有的特性集合
	 */
	public static int profile() {
		return ONE | TWO | FOUR | FORMAT_JSON | REQ_PARAM_JSON | REQ_PARAM_ORDER | RESPONSE_JSON;
	}

	/**
	 * 判断protocol是否含有feature
	 * 
	 * @param protocol
	 *            原始协议
	 * @param feature
	 *            某个特性
	 * @return
	 */
	public static boolean hasFeature(int protocol, int feature) {
		return (protocol & feature) != 0;
	}

	public final static int MAX_LENGTH = 0x3FFFFFFF;

	public final static int MAX_ONE = 0xF0;
	public final static int MAX_TWO = 0xFF00;

	public final static String LINE_SPLIT = "\n";

	/**
	 * 魔术字符，仅作用与int的首字节
	 */
	public final static int MAGIC = 0x8F000000;
}
