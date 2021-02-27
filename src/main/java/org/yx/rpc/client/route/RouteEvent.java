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

import org.yx.rpc.data.RouteInfo;

public class RouteEvent {
	private final RouteEventType type;
	private final String nodeName;
	private final RouteInfo route;

	public String getNodeName() {
		return nodeName;
	}

	public RouteInfo getRoute() {
		return route;
	}

	public RouteEventType getType() {
		return type;
	}

	private RouteEvent(RouteEventType type, String nodeName, RouteInfo route) {
		this.type = type;
		this.nodeName = nodeName;
		this.route = route;
	}

	public static RouteEvent createEvent(String nodeName, RouteInfo info) {
		return new RouteEvent(RouteEventType.CREATE, nodeName, info);
	}

	public static RouteEvent deleteEvent(String nodeName) {
		return new RouteEvent(RouteEventType.DELETE, nodeName, null);
	}

	public static RouteEvent modifyEvent(String nodeName, RouteInfo info) {
		return new RouteEvent(RouteEventType.MODIFY, nodeName, info);
	}
}

enum RouteEventType {
	CREATE, DELETE, MODIFY
}