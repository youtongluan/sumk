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
package org.yx.rpc.codec;

import static org.yx.rpc.codec.ReqParamType.REQ_PARAM_JSON;
import static org.yx.rpc.codec.ReqParamType.REQ_PARAM_ORDER;

import org.yx.conf.Const;

public final class Protocols {

	public static final int MAGIC = 0x9A_00_00_00;

	public static final int REQUEST = 0x1_00_00;

	public static final int RESPONSE = 0x2_00_00;

	public static final int TEST = 0x10_00;

	public static final int REQUEST_PARAM_TYPES = REQ_PARAM_JSON | REQ_PARAM_ORDER;

	public static int profile() {
		return REQUEST | RESPONSE | TEST | Const.SUMK_VERSION;
	}

	public static boolean hasFeature(int protocol, int feature) {
		return (protocol & feature) == feature;
	}

}
