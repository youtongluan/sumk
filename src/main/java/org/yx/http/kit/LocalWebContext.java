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
package org.yx.http.kit;

import org.yx.http.handler.WebContext;

public final class LocalWebContext {
	private static final ThreadLocal<WebContext> CTX = new ThreadLocal<>();

	public static WebContext getCtx() {
		return CTX.get();
	}

	public static void setCtx(WebContext ctx) {
		CTX.set(ctx);
	}

	public static void remove() {
		CTX.remove();
	}
}
