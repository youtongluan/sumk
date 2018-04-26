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
import org.yx.common.BizExcutor;
import org.yx.common.CalleeNode;
import org.yx.exception.SumkException;
import org.yx.util.GsonUtil;
import org.yx.validate.Param;

public final class RpcActionNode extends CalleeNode {
	public final Soa action;

	public RpcActionNode(Object obj, Method method, Class<? extends ArgPojo> argClz, String[] argNames,
			Class<?>[] argTypes, Param[] params, Soa action) {
		super(obj, method, argClz, argNames, argTypes, params);
		this.action = action;
	}

	public Object invokeByJsonArg(String args) throws Throwable {
		if (argTypes == null || argTypes.length == 0) {
			return BizExcutor.exec(method, obj, null, null);
		}
		if (argClz == null) {
			SumkException.throwException(54214657, method.getName() + " args parse error");
		}
		ArgPojo argObj = GsonUtil.fromJson(args, argClz);
		Object[] params = argObj.params();
		return BizExcutor.exec(method, obj, params, this.paramInfos);
	}

	public Object invokeByOrder(String... args) throws Throwable {
		if (argTypes == null || argTypes.length == 0) {
			return BizExcutor.exec(method, obj, null, null);
		}
		Object[] params = new Object[argTypes.length];
		if (argClz == null) {
			SumkException.throwException(54214657, method.getName() + " args parse error");
		}
		if (args == null || args.length == 0) {
			SumkException.throwException(12012, method.getName() + "的参数不能为空");
		}
		if (args.length != argTypes.length) {
			SumkException.throwException(12013,
					method.getName() + "需要传递的参数有" + argTypes.length + "个，实际传递的是" + args.length + "个");
		}
		for (int i = 0, k = 0; i < params.length; i++) {

			if (args[k] == null) {
				params[i] = null;
				continue;
			}
			Field f = fields[k];
			params[i] = GsonUtil.fromJson(args[i], f.getGenericType());
			k++;
		}
		return BizExcutor.exec(method, obj, params, this.paramInfos);
	}

}
