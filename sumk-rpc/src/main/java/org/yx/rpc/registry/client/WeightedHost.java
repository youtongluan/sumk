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
package org.yx.rpc.registry.client;

import org.yx.common.Host;
import org.yx.common.route.AbstractWeightedServer;
import org.yx.rpc.client.HostChecker;

public class WeightedHost extends AbstractWeightedServer<Host> {

	public WeightedHost(Host source, int weight) {
		super(source);
		this.setWeight(weight);
	}

	@Override
	public boolean isEnable() {
		return !HostChecker.get().isDowned(this.source);
	}

}
