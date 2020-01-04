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
package org.yx.http.handler;

import java.util.List;

import org.slf4j.Logger;
import org.yx.annotation.http.Web;
import org.yx.log.Log;

public class HttpHandlerChain implements HttpHandler {

	private Logger LOG = Log.get("sumk.http.chain");
	private HttpHandler[] handlers;

	public static final HttpHandlerChain inst = new HttpHandlerChain();
	public static final HttpHandlerChain upload = new HttpHandlerChain();

	private HttpHandlerChain() {

	}

	public void setHandlers(List<HttpHandler> handlers) {
		this.handlers = handlers.toArray(new HttpHandler[0]);
	}

	@Override
	public boolean accept(Web web) {
		return true;
	}

	@Override
	public boolean handle(WebContext ctx) throws Throwable {
		try {
			for (HttpHandler h : this.handlers) {
				if (h.accept(ctx.httpNode().action)) {
					if (LOG.isTraceEnabled()) {
						if (String.class.isInstance(ctx.data())) {
							String s = ((String) ctx.data());
							LOG.trace("{} - {} with data:{}", ctx.rawAct(), h.getClass().getSimpleName(), s);
						} else {
							LOG.trace("{} - {}", ctx.rawAct(), h.getClass().getSimpleName());
						}
					}
					if (h.handle(ctx)) {
						break;
					}
				}
			}
		} finally {
			UploadFileHolder.remove();
		}
		return true;
	}
}
