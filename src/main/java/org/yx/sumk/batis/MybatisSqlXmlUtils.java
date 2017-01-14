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
package org.yx.sumk.batis;

import java.io.InputStream;
import java.util.Map;

import org.yx.bean.Loader;
import org.yx.conf.AppInfo;
import org.yx.conf.MultiResourceFactory;
import org.yx.util.Assert;

/**
 * 需要[]来分段<BR>
 * 以#注释
 * 
 * @author 游夏
 *
 */
public final class MybatisSqlXmlUtils {

	public static Map<String, InputStream> openInputs(String db) throws Exception {
		String resourceFactory = AppInfo.get("sumk.db.batis.factory." + db, LocalSqlXmlFactory.class.getName());
		Class<?> factoryClz = Loader.loadClass(resourceFactory);
		Assert.isTrue(MultiResourceFactory.class.isAssignableFrom(factoryClz),
				resourceFactory + " should extend from MultiResourceFactory");
		MultiResourceFactory factory = (MultiResourceFactory) factoryClz.newInstance();
		return factory.openInputs(db);
	}

}
