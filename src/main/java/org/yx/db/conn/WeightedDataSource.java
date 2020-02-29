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

import org.yx.common.route.WeightedServer;
import org.yx.exception.SumkException;

public class WeightedDataSource implements WeightedServer {

	private final SumkDataSource ds;
	private int weight;

	public WeightedDataSource(SumkDataSource ds) {
		this.ds = ds;
	}

	public SumkDataSource getDdataSource() {
		return ds;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		if (weight < 0) {
			SumkException.throwException(2454335, "db weight must big than or equals 0,but exact is " + weight);
		}
		this.weight = weight;
	}

	public boolean enable() {
		return ds.isEnable();
	}
}
