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
package org.yx.rpc.server.impl;

import org.yx.common.ThreadContext;
import org.yx.exception.SoaException;
import org.yx.rpc.ActionHolder;
import org.yx.rpc.ActionInfo;
import org.yx.rpc.codec.Protocols;
import org.yx.rpc.codec.Request;
import org.yx.rpc.server.RequestHandler;
import org.yx.rpc.server.Response;
import org.yx.util.GsonUtil;
import org.yx.util.StringUtils;

public class OrderedParamReqHandler implements RequestHandler {

	@Override
	public boolean accept(Object msg) {
		if (!Request.class.isInstance(msg)) {
			return false;
		}
		Request req = (Request) msg;
		return Protocols.hasFeature(req.protocol(), Protocols.REQ_PARAM_ORDER);
	}

	@Override
	public Object received(Object msg) {
		Request req = (Request) msg;
		long start = System.currentTimeMillis();
		Response resp = new Response(req.getSn());
		try {
			String sn0 = StringUtils.isEmpty(req.getSn0()) ? req.getSn() : req.getSn0();
			String method = req.getMethod();
			ThreadContext.rpcContext(method, sn0);
			ActionInfo minfo = ActionHolder.getActionInfo(method);
			Object ret = minfo.invokeByOrder(req.getParamArray());
			resp.setJson(GsonUtil.toJson(ret));
			resp.setException(null);
			resp.setMs(System.currentTimeMillis() - start);
		} catch (Throwable e) {
			resp.setJson(null);
			resp.setException(new SoaException(1001, e.getMessage(), e));
			resp.setMs(System.currentTimeMillis() - start);
		} finally {
			ThreadContext.remove();
		}
		return resp;
	}

}
