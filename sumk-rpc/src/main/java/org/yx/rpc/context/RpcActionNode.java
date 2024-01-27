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
package org.yx.rpc.context;

import java.lang.reflect.Method;
import java.lang.reflect.Type;

import org.yx.bean.aop.asm.MethodPojo;
import org.yx.bean.aop.asm.ParamPojo;
import org.yx.bean.aop.context.CalleeNode;
import org.yx.exception.BizException;
import org.yx.exception.SumkException;
import org.yx.log.Logs;
import org.yx.rpc.RpcErrorCode;
import org.yx.rpc.RpcJson;
import org.yx.rpc.codec.ReqParamType;
import org.yx.rpc.codec.Request;

public final class RpcActionNode extends CalleeNode {
	private final boolean publish;

	public RpcActionNode(Object obj, Method method, MethodPojo argClzInfo, int toplimit, boolean publish) {
		super(obj, method, argClzInfo, toplimit);
		this.publish = publish;
	}

	public boolean publish() {
		return this.publish;
	}

	public ParamPojo createJsonParamPojo(Request req) throws Throwable {
		if (this.params.paramLength() == 0) {
			return this.createEmptyParamObj();
		}
		String args = req.getJsonedParam();
		ParamPojo argObj = RpcJson.server().fromJson(args, this.params.paramClz());
		return argObj == null ? this.createEmptyParamObj() : argObj;
	}

	public ParamPojo createOrderParamPojo(Request req) throws Throwable {
		int paramLength = params.paramLength();
		ParamPojo pojo = this.createEmptyParamObj();
		if (paramLength == 0) {
			return pojo;
		}
		String[] args = req.getParamArray();
		if (args == null) {
			if (req.hasFeature(ReqParamType.REQ_PARAM_ORDER)) {
				Logs.rpc().debug("{}需要传递{}个参数，实际却是null", method.getName(), paramLength);
				return pojo;
			}
			throw new SumkException(12012, method.getName() + "的参数类型不对，不是order类型");
		}
		if (args.length != paramLength) {
			Logs.rpc().debug("{}需要传递{}个参数，实际传递{}个", method.getName(), paramLength, args.length);
		}

		Object[] objs = new Object[paramLength];
		for (int i = 0; i < paramLength; i++) {
			if (i >= args.length || args[i] == null) {
				continue;
			}
			Type paramType = this.params.getParamType(i);
			if (paramType == String.class) {
				objs[i] = args[i];
			} else {
				objs[i] = RpcJson.server().fromJson(args[i], paramType);
			}
		}
		pojo.setParams(objs);
		return pojo;
	}

	public static void checkNode(String api, CalleeNode node) {
		if (node == null) {
			throw new SumkException(123546, "[" + api + "] is not found in this server");
		}
		if (node.overflowThreshold()) {
			throw BizException.create(RpcErrorCode.THREAD_THRESHOLD_OVER, "微服务限流降级");
		}
	}

}
