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
package org.yx.db.mapper;

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.Map;

import org.yx.annotation.Bean;
import org.yx.bean.Plugin;
import org.yx.common.SumkLogs;
import org.yx.conf.MultiResourceLoader;
import org.yx.exception.SumkException;
import org.yx.log.Log;
import org.yx.util.SumkDate;

@Bean
public class ResourcePlugin implements Plugin {

	private void startListen(MultiResourceLoader loader) {
		loader.startListen(load -> {
			try {
				Log.get("sumk.db.sql").info("local sql changed at {}", SumkDate.now().to_yyyy_MM_dd_HH_mm_ss());
				loadSql(load);
			} catch (Exception e) {
				Log.printStack(SumkLogs.SQL_ERROR, e);
			}
		});
	}

	private void loadSql(MultiResourceLoader loader) throws Exception {
		Map<String, byte[]> inputMap = loader.openResources(null);
		if (inputMap == null || inputMap.isEmpty()) {
			return;
		}
		Map<String, SqlParser> sqlMap = new HashMap<>();
		try {
			for (Map.Entry<String, byte[]> entry : inputMap.entrySet()) {
				String fileName = entry.getKey();
				byte[] bs = entry.getValue();
				if (bs == null || bs.length == 0) {
					continue;
				}

				SqlXmlParser.parseXml(sqlMap, SqlHolder.documentBuilderFactory().create(), fileName,
						new ByteArrayInputStream(bs));
			}
		} catch (Exception e) {
			Log.get("sumk.db.sql").error(e.toString(), e);
			return;
		}
		SqlHolder.setSQLS(sqlMap);
	}

	@Override
	public void startAsync() {
		try {
			MultiResourceLoader loader = SqlHolder.resourceLoader().get();
			loadSql(loader);
			startListen(loader);
		} catch (Throwable e) {
			SumkException.throwException(2351343, SumkLogs.SQL_ERROR, e);
		}
	}

}
