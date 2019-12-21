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

import java.util.List;

import org.yx.bean.IOC;
import org.yx.rpc.RpcActionNode;
import org.yx.rpc.server.RpcFilter;
import org.yx.rpc.server.RpcVisitor;

public final class RpcHandler {

	private static RpcFilter filter;
	private static final RpcFilter LAST = new RpcFilter() {
		@Override
		public Object doFilter(RpcActionNode node, RpcVisitor visitor) throws Throwable {
			return visitor.visit(node);
		}

	};

	public static synchronized void init() {
		if (filter != null) {
			return;
		}
		List<RpcFilter> list = IOC.getBeans(RpcFilter.class);
		if (list == null || list.isEmpty()) {
			filter = LAST;
			return;
		}
		final int size = list.size();
		for (int i = 0; i < size; i++) {
			RpcFilter current = list.get(i);
			if (i == size - 1) {
				current.setNext(LAST);
				break;
			}
			current.setNext(list.get(i + 1));
		}
		filter = list.get(0);
	}

	public static Object handle(RpcActionNode node, RpcVisitor visitor) throws Throwable {
		return filter.doFilter(node, visitor);
	}

}
