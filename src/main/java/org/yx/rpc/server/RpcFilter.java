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

import java.util.Objects;

import org.yx.common.Ordered;
import org.yx.exception.SumkException;
import org.yx.rpc.RpcActionNode;

public abstract class RpcFilter implements Ordered {

	private RpcFilter next;

	public final void setNext(RpcFilter next) {
		if (this.next != null) {
			throw new SumkException(23431, "next已经赋值了，它是" + this.next);
		}
		this.next = Objects.requireNonNull(next);
	}

	protected final Object callNextFilter(RpcActionNode node, RpcVisitor visitor) throws Throwable {
		return this.next.doFilter(node, visitor);
	}

	public abstract Object doFilter(RpcActionNode node, RpcVisitor visitor) throws Throwable;

}
