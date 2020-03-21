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
package org.yx.rpc.client.route;

import java.util.Collection;
import java.util.Objects;
import java.util.function.BiFunction;

import org.yx.common.Host;
import org.yx.common.route.Router;
import org.yx.common.route.Routes;
import org.yx.common.route.WeightedServer;

public final class RouteHolder {

	private static BiFunction<String, Collection<WeightedServer<Host>>, Router<Host>> routerFactory = (api,
			servers) -> Routes.createWeightedRouter(servers);

	public static void set(BiFunction<String, Collection<WeightedServer<Host>>, Router<Host>> factory) {
		RouteHolder.routerFactory = Objects.requireNonNull(factory);
	}

	public static BiFunction<String, Collection<WeightedServer<Host>>, Router<Host>> get() {
		return RouteHolder.routerFactory;
	}

	public static Router<Host> createRouter(String api, Collection<WeightedServer<Host>> servers) {
		return routerFactory.apply(api, servers);
	}
}
