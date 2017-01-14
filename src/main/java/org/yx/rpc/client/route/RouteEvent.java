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
package org.yx.rpc.client.route;

import org.yx.rpc.Host;

public class RouteEvent {
	private RouteEventType type;
	private ZkData zkData;
	private Host url;

	public Host getUrl() {
		return url;
	}

	public RouteEventType getType() {
		return type;
	}

	public ZkData getZkData() {
		return zkData;
	}

	private RouteEvent(RouteEventType type, Host url, ZkData zkData) {
		super();
		this.type = type;
		this.zkData = zkData;
		this.url = url;
	}

	public static RouteEvent create(Host url, ZkData data) {
		return new RouteEvent(RouteEventType.Create, url, data);
	}

	public static RouteEvent delete(Host url) {
		return new RouteEvent(RouteEventType.Delete, url, null);
	}

	public static RouteEvent modify(Host url, ZkData data) {
		return new RouteEvent(RouteEventType.Modify, url, data);
	}
}

enum RouteEventType {
	Create, Delete, Modify
}