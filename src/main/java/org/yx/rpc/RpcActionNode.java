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

import org.yx.annotation.Param;
import org.yx.annotation.rpc.Soa;
import org.yx.asm.ArgPojo;
import org.yx.bean.Loader;
import org.yx.common.BizExcutor;
import org.yx.common.CalleeNode;
import org.yx.conf.AppInfo;
import org.yx.exception.SumkException;
import org.yx.log.Logs;
import org.yx.util.S;

public final class RpcActionNode extends CalleeNode {
	public final Soa action;

	public final Field[] fields;

	public RpcActionNode(Object obj, Method method, Class<? extends ArgPojo> argClz, String[] argNames, Param[] params,
			Soa action) {
		super(obj, method, argClz, argNames, params,
				action.toplimit() > 0 ? action.toplimit() : AppInfo.getInt("sumk.rpc.thread.priority.default", 100000));
		this.action = action;
		if (argNames.length > 0) {
			this.fields = new Field[argNames.length];
			try {
				for (int i = 0; i < this.fields.length; i++) {
					Field f = this.argClz.getDeclaredField(argNames[i]);
					f.setAccessible(true);
					this.fields[i] = f;
				}
			} catch (Exception e) {
				SumkException.throwException(235345, e.getMessage());
			}
		} else {
			this.fields = null;
		}
	}

	public Object invokeByJsonArg(String args) throws Throwable {
		if (argNames.length == 0) {
			return BizExcutor.exec(this.getEmptyArgObj(), this.owner, new Object[0], this.paramInfos);
		}

		ArgPojo argObj = S.json.fromJson(args, argClz);
		Object[] params = argObj.params();
		return BizExcutor.exec(argObj, owner, params, this.paramInfos);
	}

	public Object invokeByOrder(String... args) throws Throwable {
		if (argNames.length == 0) {
			return BizExcutor.exec(this.getEmptyArgObj(), this.owner, new Object[0], this.paramInfos);
		}
		if (args == null) {
			SumkException.throwException(12012, method.getName() + "的参数不能为空");
		}
		if (args.length != argNames.length) {
			Logs.rpc().debug(method.getName() + "需要传递" + argNames.length + "个参数，实际传递" + args.length + "个");
		}

		ArgPojo pojo = Loader.newInstance(this.argClz);
		for (int i = 0; i < fields.length; i++) {
			if (i >= args.length || args[i] == null) {
				continue;
			}
			Field f = fields[i];
			f.set(pojo, RpcGson.fromJson(args[i], f.getGenericType()));
		}
		return BizExcutor.exec(pojo, this.owner, pojo.params(), this.paramInfos);
	}

}
