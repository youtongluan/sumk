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

import java.util.Collection;

public final class Routes {

	public static <T> Router<T> createWeightedRouter(Collection<WeightedServer<T>> servers) {
		if (servers == null || servers.isEmpty()) {
			return new EmptyRouter<>();
		}
		if (servers.size() == 1) {
			return new SingleRouter<>(servers.iterator().next());
		}
		return new WeightedRouter<>(servers);
	}
}
