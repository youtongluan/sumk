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
package org.yx.util;

import org.yx.common.ThreadContext;
import org.yx.rpc.RpcAttachment;

public final class RpcUtil {

	public static String userId() {
		return ThreadContext.get().userId();
	}

	public static void setUserId(String userId) {
		ThreadContext.get().userId(userId);
	}

	public static boolean setUserIdIfEmpty(String userId) {
		ThreadContext tc = ThreadContext.get();
		if (tc.userId() == null) {
			tc.userId(userId);
			return true;
		}
		return false;
	}

	public static RpcAttachment attachments() {
		return RpcAttachment.get();
	}
}
