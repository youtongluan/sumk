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
package org.yx.http.start;

import java.io.File;
import java.net.URISyntaxException;

import org.eclipse.jetty.server.ConnectionFactory;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.SslConnectionFactory;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.yx.conf.AppInfo;
import org.yx.exception.SumkException;
import org.yx.http.HttpPlugin;
import org.yx.log.Log;
import org.yx.util.FileUtil;

public class JettyHttpsServer extends JettyServer {

	public JettyHttpsServer(int port) {
		super(port);
	}

	@Override
	protected ConnectionFactory[] getConnectionFactorys() throws URISyntaxException {
		SslContextFactory sslContextFactory = new SslContextFactory();
		String path = get(HttpPlugin.KEY_STORE_PATH);
		File keystoreFile = FileUtil.file(path);
		if (!keystoreFile.exists()) {
			String msg = path + " is not exist";
			Log.get("sumk.http").error(msg);
			SumkException.throwException(-2345345, msg);
		}
		sslContextFactory.setKeyStorePath(keystoreFile.getAbsolutePath());
		sslContextFactory.setKeyStorePassword(get("sumk.http.ssl.storePassword"));
		sslContextFactory.setKeyManagerPassword(get("sumk.http.ssl.managerPassword"));
		sslContextFactory.setCertAlias(get("sumk.http.ssl.alias"));

		String v = AppInfo.get("sumk.http.ssl.storeType", null);
		if (v != null) {
			sslContextFactory.setKeyStoreType(v);
		}

		sslContextFactory.setTrustAll(AppInfo.getBoolean("sumk.http.ssl.trustAll", false));

		Log.get("sumk.http").info("using https");
		return new ConnectionFactory[] { new SslConnectionFactory(sslContextFactory, "http/1.1"),
				new HttpConnectionFactory() };
	}

	/**
	 * @param string
	 * @return
	 */
	private String get(String name) {
		String v = AppInfo.get(name, null);
		if (v == null) {
			SumkException.throwException(name + " is null!!! please set it");
		}
		return v;
	}

	@Override
	protected ServerConnector createConnector() throws Exception {
		ServerConnector connector = super.createConnector();
		connector.setDefaultProtocol(AppInfo.get("sumk.http.ssl.protocol", "SSL"));
		return connector;
	}

}
