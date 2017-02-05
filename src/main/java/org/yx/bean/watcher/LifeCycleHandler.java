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
package org.yx.bean.watcher;

import java.io.InputStream;
import java.util.Collections;
import java.util.List;

import org.yx.bean.IOC;
import org.yx.bean.Loader;
import org.yx.bean.Plugin;
import org.yx.log.Log;
import org.yx.util.CollectionUtils;
import org.yx.util.StringUtils;

public class LifeCycleHandler {

	public static final LifeCycleHandler instance = new LifeCycleHandler();

	private void runFromFile() {
		try {
			InputStream in = Loader.getResourceAsStream("sumk-exec");
			if (in == null) {
				return;
			}
			List<String> list = CollectionUtils.loadList(in);
			for (String key : list) {
				if (StringUtils.isEmpty(key)) {
					continue;
				}
				Class<?> clz = Loader.loadClass(key);
				if (!Runnable.class.isAssignableFrom(clz)) {
					Log.get("SYS").info("{} should implements Runnable", clz.getSimpleName());
					continue;
				}
				Runnable r = (Runnable) clz.newInstance();
				r.run();
			}
		} catch (Exception e) {
			Log.printStack(e);
			System.exit(-1);
		}
	}

	public void start() {
		startBeans();
		runFromFile();
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			destory();
		}));
	}

	private void startBeans() {
		List<Plugin> lifes = IOC.getBeans(Plugin.class);
		if (lifes == null || lifes.isEmpty()) {
			return;
		}
		lifes.forEach(Plugin::start);
	}

	private boolean destoryed = false;

	public synchronized void destory() {
		if (destoryed) {
			return;
		}
		destoryed = true;
		List<Plugin> lifes = IOC.getBeans(Plugin.class);
		if (lifes == null || lifes.isEmpty()) {
			return;
		}
		Collections.reverse(lifes);
		lifes.forEach(b -> {
			try {
				b.stop();
			} catch (Exception e) {
				Log.printStack(e);
			}
		});
		Log.get("sumk.SYS").info("sumk server stoped!!!");
	}

}
