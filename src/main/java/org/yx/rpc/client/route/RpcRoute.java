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
package org.yx.rpc.client.route;

import java.util.Collection;

import org.yx.rpc.Host;
import org.yx.util.WeightedRoute;

public class RpcRoute extends WeightedRoute<ServerMachine> {

	public RpcRoute(Collection<ServerMachine> servers) {
		super(servers.toArray(new ServerMachine[servers.size()]));
	}

	@Override
	protected boolean isDowned(ServerMachine server) {
		return HostChecker.get().isDowned(server.url);
	}

	public Host getUrl() {
		ServerMachine server = this.getServer();
		return server == null ? null : server.url;
	}

}
