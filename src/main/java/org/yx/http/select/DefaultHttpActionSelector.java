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
package org.yx.http.select;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.yx.common.matcher.Matchers;
import org.yx.http.act.HttpActionInfo;

public class DefaultHttpActionSelector implements HttpActionSelector {

	private Map<String, HttpActionInfo> actMap = Collections.emptyMap();

	@Override
	public HttpActionInfo getHttpInfo(String act) {
		HttpActionInfo info = actMap.get(act);
		if (info != null) {
			return info;
		}
		return actMap.get(Matchers.WILDCARD);
	}

	@Override
	public List<String> actNames() {
		List<String> acts = new ArrayList<>(actMap.keySet());
		acts.sort(null);
		return acts;
	}

	@Override
	public void init(Map<String, HttpActionInfo> acts) {
		this.actMap = new HashMap<>(acts);
	}

}
