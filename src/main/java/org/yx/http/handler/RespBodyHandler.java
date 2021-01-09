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

import org.yx.annotation.Bean;

@Bean
public class RespBodyHandler implements HttpHandler {

	@Override
	public int order() {
		return 2600;
	}

	@Override
	public void handle(WebContext ctx) throws Throwable {
		byte[] data = (byte[]) ctx.result();
		if (data != null && data.length > 0) {
			ctx.httpResponse().getOutputStream().write(data);
		}
	}

}
