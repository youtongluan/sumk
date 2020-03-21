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
package org.yx.common.route;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class SingleRouter<T> implements Router<T> {

	private final WeightedServer<T> server;

	public SingleRouter(WeightedServer<T> server) {
		this.server = Objects.requireNonNull(server);
	}

	@Override
	public T select() {
		if (!this.server.isEnable()) {
			return null;
		}
		return this.server.getSource();
	}

	@Override
	public List<T> allSources() {
		return Collections.singletonList(this.server.getSource());
	}

	@Override
	public List<T> aliveSources() {
		return server.isEnable() ? Collections.singletonList(this.server.getSource()) : Collections.emptyList();
	}

}
