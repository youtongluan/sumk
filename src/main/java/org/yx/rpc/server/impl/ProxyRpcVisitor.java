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

import org.yx.common.CalleeNode;
import org.yx.common.CalleeNode.Visitor;
import org.yx.rpc.codec.Request;

class ProxyRpcVisitor implements Visitor {

	private AbstractRpcVisitor visitor;

	public static ProxyRpcVisitor proxy(AbstractRpcVisitor visitor, Request req) {
		ProxyRpcVisitor v = new ProxyRpcVisitor();
		v.visitor = visitor;
		visitor.req = req;
		return v;
	}

	private ProxyRpcVisitor() {
	}

	@Override
	public Object visit(CalleeNode info) throws Throwable {

		return this.visitor.visit(info);
	}

	static abstract class AbstractRpcVisitor implements Visitor {
		protected Request req;
	}

}
