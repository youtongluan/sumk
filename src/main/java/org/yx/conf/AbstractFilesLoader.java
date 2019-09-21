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
package org.yx.conf;

import java.io.File;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import org.yx.main.SumkThreadPool;

public abstract class AbstractFilesLoader implements MultiResourceLoader, Runnable {
	protected Consumer<MultiResourceLoader> consumer;
	protected FileModifyTime[] times;
	protected final String rootUri;
	protected final String subfix;

	public AbstractFilesLoader(String rootUri, String subfix) {
		this.rootUri = Objects.requireNonNull(rootUri);
		this.subfix = "".equals(subfix) ? null : subfix;
	}

	@Override
	public synchronized boolean startListen(Consumer<MultiResourceLoader> consumer) {
		if (this.consumer != null) {
			return false;
		}
		this.consumer = consumer;
		SumkThreadPool.scheduledExecutor().scheduleWithFixedDelay(this, 60, AppInfo.getLong("sumk.db.sdb.delay", 60),
				TimeUnit.SECONDS);
		return true;
	}

	@Override
	public void run() {
		FileModifyTime[] times = this.times;
		if (times == null || times.length == 0) {
			return;
		}
		boolean modified = false;
		for (FileModifyTime ft : times) {
			File f = new File(ft.file);
			if (f.lastModified() > ft.lastModify) {
				modified = true;
				break;
			}
		}
		if (modified) {
			consumer.accept(this);
		}
	}

}