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

import java.util.Objects;

import org.slf4j.Logger;
import org.yx.common.CalleeNode;
import org.yx.common.CalleeNode.Visitor;
import org.yx.log.Log;
import org.yx.rpc.RpcActionNode;
import org.yx.rpc.codec.Request;
import org.yx.rpc.server.RpcFilter;

public class ProxyRpcVisitor implements Visitor {

	private AbstractRpcVisitor visitor;
	private static RpcFilter[] filters;

	public static void setFilters(RpcFilter[] filters) {
		ProxyRpcVisitor.filters = Objects.requireNonNull(filters);
		if (filters.length > 0) {
			Logger log = Log.get("sumk.rpc");
			if (log.isDebugEnabled()) {
				StringBuilder sb = new StringBuilder("rpc filter:");
				for (RpcFilter f : filters) {
					sb.append("  ").append(f.getClass().getSimpleName());
				}
				log.debug(sb.toString());
			}
		}
	}

	public static ProxyRpcVisitor proxy(AbstractRpcVisitor visitor) {
		return new ProxyRpcVisitor(visitor);
	}

	private ProxyRpcVisitor(AbstractRpcVisitor visitor) {
		this.visitor = visitor;
	}

	@Override
	public Object visit(CalleeNode info) throws Throwable {
		if (filters.length == 0) {
			return this.visitor.visit(info);
		}
		RpcActionNode node = (RpcActionNode) info;
		try {
			for (RpcFilter f : filters) {
				f.beforeInvoke(node, visitor.req);
			}
			Object ret = this.visitor.visit(info);
			for (RpcFilter f : filters) {
				f.afterInvoke(node, visitor.req, ret);
			}
			return ret;
		} catch (Throwable e) {
			for (RpcFilter f : filters) {
				Throwable e2 = f.error(node, visitor.req, e);
				if (e2 != null) {
					e = e2;
				}
			}
			throw e;
		}
	}

	public static abstract class AbstractRpcVisitor implements Visitor {

		public AbstractRpcVisitor(Request req) {
			this.req = req;
		}

		protected final Request req;
	}

}
