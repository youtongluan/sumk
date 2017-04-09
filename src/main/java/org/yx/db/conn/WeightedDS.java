/**
 * Copyright (C) 2016 - 2017 youtongluan.
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

import javax.sql.DataSource;

import org.yx.exception.SumkException;
import org.yx.util.WeightedRoute;

class WeightedDS implements WeightedRoute.Server {

	public WeightedDS(DataSource ds) {
		super();
		this.ds = ds;
	}

	private DataSource ds;
	private int weight;

	public DataSource getDs() {
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

}
