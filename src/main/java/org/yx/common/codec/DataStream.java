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
package org.yx.common.codec;

public interface DataStream {
	/**
	 * 用于writePrefixedString和readPrefixedString做null的处理
	 */
	public static final String NULL = new String(new char[] { 0x00, '\n', 0x03, 'Y', 0x06, 0x00, 0x00, '6' });

	void position(int pos);

	int position();

	void writeInt(int k, int bytes);

	/**
	 * 内部实现推荐用本接口的NULL常量来表示null，read的时候也要做响应的转义。 这个逻辑对使用者透明
	 * 
	 * @param s           可以为null
	 * @param lengthBytes 用于存储s序列化后的长度，只能是1 2 4其中的一个
	 * @throws Exception 异常信息
	 */
	void writePrefixedString(CharSequence s, int lengthBytes) throws Exception;

	void read(byte[] dst, int offset, int length);

	void write(byte[] src, int offset, int length);

	int readInt(int bytes);

	String readPrefixedString(int lengthBytes) throws Exception;

}
