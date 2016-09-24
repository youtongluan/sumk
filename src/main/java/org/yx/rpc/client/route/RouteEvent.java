package org.yx.rpc.client.route;

import org.yx.rpc.Url;
import org.yx.rpc.ZkData;

public class RouteEvent {
	private RouteEventType type;
	private ZkData zkData;
	private Url url;

	public Url getUrl() {
		return url;
	}

	public RouteEventType getType() {
		return type;
	}

	public ZkData getZkData() {
		return zkData;
	}

	private RouteEvent(RouteEventType type, Url url, ZkData zkData) {
		super();
		this.type = type;
		this.zkData = zkData;
		this.url = url;
	}

	public static RouteEvent create(Url url, ZkData data) {
		return new RouteEvent(RouteEventType.Create, url, data);
	}

	public static RouteEvent delete(Url url) {
		return new RouteEvent(RouteEventType.Delete, url, null);
	}

	public static RouteEvent modify(Url url, ZkData data) {
		return new RouteEvent(RouteEventType.Modify, url, data);
	}
}

enum RouteEventType {
	Create, Delete, Modify
}