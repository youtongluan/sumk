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
package org.yx.db.conn;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.yx.common.route.WeightedRoute;

public class WeightedDataSourceRoute extends WeightedRoute<WeightedDataSource> {

	public WeightedDataSourceRoute(Collection<WeightedDataSource> servers) {
		super(servers.toArray(new WeightedDataSource[servers.size()]));
	}

	public Set<SumkDataSource> allDataSource() {
		Set<SumkDataSource> set = new HashSet<>();
		for (WeightedDataSource ds : this.SERVERS) {
			set.add(ds.getDdataSource());
		}
		return set;
	}

	public SumkDataSource datasource() {
		WeightedDataSource sm = this.getServer();
		if (sm == null) {
			return null;
		}
		return sm.getDdataSource();
	}

	@Override
	protected boolean isDowned(WeightedDataSource server) {
		return !server.enable();
	}

}
