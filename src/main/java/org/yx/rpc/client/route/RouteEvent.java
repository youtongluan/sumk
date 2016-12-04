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