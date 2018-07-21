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
package org.yx.rpc;

import java.net.InetSocketAddress;

public final class Host implements Comparable<Host> {
	private final String ip;
	private final int port;

	private Host(String ip, int port) {
		super();
		this.ip = ip;
		this.port = port;
	}

	public static Host create(String addr) {
		int index = addr.lastIndexOf(':');
		return new Host(addr.substring(0, index), Integer.valueOf(addr.substring(index + 1)));
	}

	public static Host create(String ip, int port) {
		return new Host(ip, port);
	}

	public String ip() {
		return ip;
	}

	public int port() {
		return port;
	}

	public InetSocketAddress toInetSocketAddress() {
		return new InetSocketAddress(ip, port);
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
		Host other = (Host) obj;
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

	@Override
	public int compareTo(Host h) {
		int t = ip.compareTo(h.ip);
		if (t != 0) {
			return t;
		}
		return port - h.port;
	}

}
