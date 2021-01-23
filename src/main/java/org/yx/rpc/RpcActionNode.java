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

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.yx.asm.ArgPojo;
import org.yx.bean.Loader;
import org.yx.common.context.CalleeNode;
import org.yx.exception.BizException;
import org.yx.exception.SumkException;
import org.yx.log.Logs;
import org.yx.rpc.codec.Request;

public final class RpcActionNode extends CalleeNode {
	private final boolean publish;

	private final Field[] fields;

	public RpcActionNode(Object obj, Method method, Class<? extends ArgPojo> argClz, String[] argNames, int toplimit,
			boolean publish) {
		super(obj, method, argClz, argNames, toplimit);
		this.publish = publish;
		if (argNames.length > 0) {
			this.fields = new Field[argNames.length];
			try {
				for (int i = 0; i < this.fields.length; i++) {
					Field f = this.argClz.getDeclaredField(argNames[i]);
					f.setAccessible(true);
					this.fields[i] = f;
				}
			} catch (Exception e) {
				throw new SumkException(235345, e.getMessage());
			}
		} else {
			this.fields = null;
		}
	}

	public boolean publish() {
		return this.publish;
	}

	public ArgPojo createJsonArgPojo(Request req) throws Throwable {
		if (this.isEmptyArgument()) {
			return this.getEmptyArgObj();
		}
		String args = req.getJsonedParam();
		ArgPojo argObj = RpcJson.server().fromJson(args, argClz);
		return argObj == null ? this.getEmptyArgObj() : argObj;
	}

	public ArgPojo createOrderArgPojo(Request req) throws Throwable {
		if (this.isEmptyArgument()) {
			return this.getEmptyArgObj();
		}
		String[] args = req.getParamArray();
		if (args == null) {
			throw new SumkException(12012, method.getName() + "的参数不能为空");
		}
		if (args.length != argNames.length) {
			Logs.rpc().debug("{}需要传递{}个参数，实际传递{}个", method.getName(), argNames.length, args.length);
		}

		ArgPojo pojo = Loader.newInstance(this.argClz);
		for (int i = 0; i < fields.length; i++) {
			if (i >= args.length || args[i] == null) {
				continue;
			}
			Field f = fields[i];
			f.set(pojo, RpcJson.server().fromJson(args[i], f.getGenericType()));
		}
		return pojo;
	}

	public static void checkNode(String api, CalleeNode node) {
		if (node == null) {
			throw new SumkException(123546, "[" + api + "] is not a valid interface");
		}
		if (node.overflowThreshold()) {
			throw BizException.create(RpcErrorCode.THREAD_THRESHOLD_OVER, "微服务限流降级");
		}
	}

}
