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
import java.util.Objects;
import java.util.function.Function;

import org.yx.rpc.InnerRpcKit;

public final class InvocationHandlerFactory {
	private static Function<Class<?>, InvocationHandler> factory = intf -> {
		String prefix = InnerRpcKit.parseRpcIntfPrefix(intf);
		return new IntfClientHandler(prefix, intf);
	};

	public static Function<Class<?>, InvocationHandler> getFactory() {
		return factory;
	}

	public static void setFactory(Function<Class<?>, InvocationHandler> factory) {
		InvocationHandlerFactory.factory = Objects.requireNonNull(factory);
	}

	public static InvocationHandler create(Class<?> clz) {
		return factory.apply(clz);
	}

}
