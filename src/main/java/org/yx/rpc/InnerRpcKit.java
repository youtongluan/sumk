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
package org.yx.rpc;

import org.yx.common.context.ActionContext;
import org.yx.rpc.client.Req;
import org.yx.util.StringUtil;

public final class InnerRpcKit {

	public static ActionContext rpcContext(Req req, boolean isTest) {
		String traceId = StringUtil.isEmpty(req.getTraceId()) ? null : req.getTraceId();
		return ActionContext.rpcContext(req.getApi(), traceId, req.getSpanId(), req.getUserId(), isTest,
				req.getAttachments());
	}

	public static String parseClassName2Prefix(String name, int partCount) {
		String[] names = name.split("\\.");
		if (names.length <= partCount) {
			return name + ".";
		}
		StringBuilder sb = new StringBuilder(name.length());
		for (int i = names.length - partCount; i < names.length; i++) {
			sb.append(StringUtil.uncapitalize(names[i])).append('.');
		}
		return sb.toString();
	}
}
