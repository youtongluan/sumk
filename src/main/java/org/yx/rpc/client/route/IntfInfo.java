package org.yx.rpc.client.route;

public class IntfInfo {
	private String intf;
	int weight;
	long timeout;
	int clientCount;

	public String getIntf() {
		return intf;
	}

	public void setIntf(String intf) {
		this.intf = intf;
	}
}
