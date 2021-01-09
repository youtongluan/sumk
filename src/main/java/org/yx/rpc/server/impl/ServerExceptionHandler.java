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
package org.yx.rpc.server.impl;

import org.yx.exception.BizException;
import org.yx.exception.SoaException;
import org.yx.log.Log;
import org.yx.rpc.RpcErrorCode;
import org.yx.rpc.codec.Request;
import org.yx.rpc.server.Response;

public final class ServerExceptionHandler {

	public static void handle(Request req, Response resp, Throwable e) {
		resp.json(null);
		resp.exception(new SoaException(e, RpcErrorCode.SERVER_HANDLE_ERROR, e.getMessage()));
		if (!(e instanceof BizException)) {
			Log.get("sumk.rpc.log.server.exception").error(e.toString(), e);
		}
	}
}
