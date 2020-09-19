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
package org.yx.util.secury;

public interface Base64 {

	/**
	 * 解码，是否含有\r\n都能解码
	 * 
	 * @param src
	 *            密文
	 * @return 明文
	 */
	byte[] decode(byte[] src);

	/**
	 * 解码，是否含有\r\n都能解码
	 * 
	 * @param src
	 *            密文
	 * @return 明文
	 */
	byte[] decode(String src);

	/**
	 * 使用标准方式进行编码,不含有换行符
	 * 
	 * @param src
	 *            原文
	 * @return 编码后的
	 */
	byte[] encode(byte[] src);

	String encodeToString(byte[] src);

}