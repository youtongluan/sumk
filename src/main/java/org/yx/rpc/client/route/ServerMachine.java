package org.yx.rpc.client.route;

import org.yx.rpc.Url;

public class ServerMachine {

	public Url getUrl() {
		return url;
	}

	public Url url;
	public int weight;
	/**
	 * RPC的超时时间
	 */
	public Integer timeout;
	/**
	 * 客户端应该初始化的最大实例个数。
	 */
	public Integer serverCount;

	public ServerMachine(Url url, int weight) {

		this.url = url;
		this.weight = weight;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((url == null) ? 0 : url.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ServerMachine other = (ServerMachine) obj;
		if (url == null) {
			if (other.url != null)
				return false;
		} else if (!url.equals(other.url))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ServerMachine [url=" + url + ", weight=" + weight + "]";
	}

}
