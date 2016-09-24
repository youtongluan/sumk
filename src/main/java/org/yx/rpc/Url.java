package org.yx.rpc;

public final class Url {
	private String ip;
	private int port;

	public static Url create(String addr) {
		Url url = new Url();
		String[] hs = addr.split(":");
		url.ip = hs[0];
		url.port = Integer.valueOf(hs[1]);
		return url;
	}

	public static Url create(String ip, int port) {
		Url url = new Url();
		url.ip = ip;
		url.port = port;
		return url;
	}

	public String getIp() {
		return ip;
	}

	public int getPort() {
		return port;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ip == null) ? 0 : ip.hashCode());
		result = prime * result + port;
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
		Url other = (Url) obj;
		if (ip == null) {
			if (other.ip != null)
				return false;
		} else if (!ip.equals(other.ip))
			return false;
		if (port != other.port)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return ip + ":" + port;
	}

}
