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

import java.io.IOException;
import java.nio.channels.ServerSocketChannel;
import java.util.concurrent.Executor;

import org.eclipse.jetty.io.ByteBufferPool;
import org.eclipse.jetty.server.ConnectionFactory;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.util.thread.Scheduler;
import org.yx.conf.AppInfo;
import org.yx.exception.SumkException;
import org.yx.log.Logs;

public class JettyServerConnector extends ServerConnector {

	public JettyServerConnector(Server server, Executor executor, Scheduler scheduler, ByteBufferPool bufferPool,
			int acceptors, int selectors, ConnectionFactory[] factories) {
		super(server, executor, scheduler, bufferPool, acceptors, selectors, factories);
	}

	@Override
	protected ServerSocketChannel openAcceptChannel() throws IOException {
		IOException ex = null;
		try {
			return super.openAcceptChannel();
		} catch (IOException e) {
			ex = e;
		}

		for (int i = 0; i < AppInfo.getInt("sumk.webserver.bind.retry", 100); i++) {
			try {
				Thread.sleep(AppInfo.getLong("sumk.webserver.bind.sleepTime", 2000));
			} catch (InterruptedException e1) {
				Logs.http().error("showdown because of InterruptedException");
				Thread.currentThread().interrupt();
				throw new SumkException(34534560, "收到中断." + e1);
			}
			Logs.http().warn("{} was occupied({}),begin retry {}", this.getPort(), ex.getMessage(), i);
			try {
				return super.openAcceptChannel();
			} catch (IOException e) {
				if (isInheritChannel()) {
					throw e;
				}
				ex = e;
			}
		}
		throw ex;
	}
}
