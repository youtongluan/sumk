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
package org.yx.rpc.client.intf;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.yx.common.json.JsonTypes;
import org.yx.common.util.S;
import org.yx.rpc.client.Client;
import org.yx.rpc.client.Rpc;
import org.yx.rpc.spec.RpcSpecs;
import org.yx.rpc.spec.SoaClientConfigSpec;
import org.yx.util.ExceptionUtil;

public class IntfClientHandler implements InvocationHandler {

	protected final String prefix;
	protected final Map<String, SoaClientConfigSpec> map;

	public IntfClientHandler(String prefix, Class<?> intf) {
		this.prefix = prefix;
		this.map = buildClientMap(intf);
	}

	protected Map<String, SoaClientConfigSpec> buildClientMap(Class<?> intf) {
		Map<String, SoaClientConfigSpec> tmp = new HashMap<>();
		Method[] ms = intf.getMethods();
		for (Method m : ms) {
			SoaClientConfigSpec sc = RpcSpecs.extractSoaClientConfig(m);
			if (sc != null) {
				tmp.put(m.getName(), sc);
			}
		}
		return tmp.isEmpty() ? Collections.emptyMap() : tmp;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) {

		if ("toString".equals(method.getName()) && (args == null || args.length == 0)) {
			return this.getApi(method);
		}
		try {
			return this.onInvoke(proxy, method, args);
		} catch (Throwable e) {
			throw ExceptionUtil.toRuntimeException(e);
		}
	}

	protected String getApi(Method method) {
		return prefix + method.getName();
	}

	protected Object onInvoke(Object proxy, Method method, Object[] args) throws Exception {
		SoaClientConfigSpec sc = map.get(method.getName());
		Client client = Rpc.create(getApi(method));
		client.paramInArray(args);
		if (sc != null) {
			if (sc.timeout() > 0) {
				client.timeout(sc.timeout());
			}
			if (sc.tryCount() > 0) {
				client.tryCount(sc.tryCount());
			}
		}
		String json = client.execute().getOrException();
		if (json == null || method.getReturnType() == Void.TYPE) {
			return null;
		}
		String gen = method.getGenericReturnType().getTypeName();
		Type type = JsonTypes.get(gen);
		if (type == null) {
			return S.json().fromJson(json, method.getReturnType());
		}
		return S.json().fromJson(json, type);
	}
}
