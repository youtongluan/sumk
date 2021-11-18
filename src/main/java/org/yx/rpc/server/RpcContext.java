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
package org.yx.rpc.server;

import org.yx.annotation.doc.NotNull;
import org.yx.common.context.NodeContext;
import org.yx.rpc.RpcActionNode;
import org.yx.rpc.codec.Request;

public class RpcContext extends NodeContext<RpcActionNode> {
	@NotNull
	private final RpcActionNode node;
	@NotNull
	protected final Request req;

	public RpcContext(RpcActionNode node, Request req) {
		this.node = node;
		this.req = req;
	}

	public Request req() {
		return req;
	}

	@Override
	public RpcActionNode node() {
		return this.node;
	}
}
