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
package org.yx.conf;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import org.yx.rpc.codec.Protocols;

public class Profile {
	public final static Charset CHARSET_DEFAULT = StandardCharsets.UTF_8;
	public final static int version = 0x160;

	public static long feature() {
		long v = version;
		v <<= 32;
		v |= Protocols.profile();
		return v;
	}

	public static String featureInHex() {
		return Long.toHexString(feature());
	}

}
