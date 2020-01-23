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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.yx.annotation.Bean;
import org.yx.bean.IOC;
import org.yx.bean.Plugin;
import org.yx.conf.AppInfo;
import org.yx.conf.MultiResourceLoader;
import org.yx.db.conn.DataSources;
import org.yx.db.event.DBEventPublisher;
import org.yx.db.listener.DBEventListener;
import org.yx.db.sql.DBSettings;
import org.yx.exception.SumkException;
import org.yx.log.Logs;
import org.yx.main.SumkServer;
import org.yx.util.SumkDate;

@Bean
public class SumkDBPlugin implements Plugin {

	@Override
	public int order() {
		return 1;
	}

	@Override
	public void startAsync() {
		DBSettings.register();
		buildDBListeners();
		loadSDBResources();
		SumkServer.setDbStarted(true);

		preHotDataSource();
	}

	protected void preHotDataSource() {
		if (AppInfo.getBoolean("sumk.db.pool.prehot.disable", false)) {
			return;
		}
		Map<String, String> map = AppInfo.subMap("s.db.");
		if (map == null || map.isEmpty()) {
			return;
		}
		Set<String> names = new HashSet<>();
		for (String key : map.keySet()) {
			int index = key.indexOf(".");
			if (index > 0) {
				key = key.substring(0, index);
			}
			names.add(key);
		}
		for (String name : names) {
			Logs.db().debug("{} begin preHot...", name);
			DataSources.getManager(name);
		}

	}

	protected void buildDBListeners() {
		List<DBEventListener> listeners = IOC.getBeans(DBEventListener.class);
		DBEventPublisher.group().setListener(listeners.toArray(new DBEventListener[0]));
	}

	protected void loadSDBResources() {
		try {
			MultiResourceLoader loader = SqlHolder.resourceLoader().get();
			loadSql(loader);
			startListen(loader);
		} catch (Throwable e) {
			SumkException.throwException(2351343, "sdb加载本地sql文件失败", e);
		}
	}

	private void startListen(MultiResourceLoader loader) {
		loader.startListen(load -> {
			try {
				Logs.db().info("local sql changed at {}", SumkDate.now().to_yyyy_MM_dd_HH_mm_ss());
				loadSql(load);
			} catch (Exception e) {
				Logs.printStack(e);
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
			Logs.db().error(e.getLocalizedMessage(), e);
			return;
		}
		SqlHolder.setSQLS(sqlMap);
	}
}
