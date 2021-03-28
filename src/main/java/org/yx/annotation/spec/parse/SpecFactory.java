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
package org.yx.annotation.spec.parse;

import org.yx.annotation.Param;
import org.yx.annotation.http.SumkFilter;
import org.yx.annotation.http.SumkServlet;
import org.yx.annotation.http.Web;
import org.yx.annotation.rpc.Soa;
import org.yx.annotation.spec.ParamSpec;
import org.yx.annotation.spec.SoaSpec;
import org.yx.annotation.spec.SumkFilterSpec;
import org.yx.annotation.spec.SumkServletSpec;
import org.yx.annotation.spec.WebSpec;

public final class SpecFactory {

	public static SumkFilterSpec create(SumkFilter f) {
		return new SumkFilterSpec(f.name(), f.path(), f.dispatcherType(), f.isMatchAfter(), f.asyncSupported());
	}

	public static SumkServletSpec create(SumkServlet f) {
		return new SumkServletSpec(f.name(), f.path(), f.loadOnStartup(), f.asyncSupported(), f.appKey());
	}

	public static ParamSpec create(Param p) {
		return new ParamSpec(p.value(), p.required(), p.max(), p.min(), p.example(), p.comment(), p.complex(), null);
	}

	public static SoaSpec create(Soa soa) {
		return new SoaSpec(soa.value(), soa.cnName(), soa.appIdPrefix(), soa.toplimit(), soa.publish());
	}

	public static WebSpec create(Web web) {
		return new WebSpec(web.value(), web.cnName(), web.requireLogin(), web.requestType(), web.sign(),
				web.responseType(), web.tags(), web.toplimit(), web.method());
	}
}
