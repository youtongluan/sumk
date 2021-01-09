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

import java.util.function.Consumer;

import org.yx.common.StartOnceLifecycle;
import org.yx.log.RawLog;

public abstract class AbstractRefreshableSystemConfig extends StartOnceLifecycle implements RefreshableSystemConfig {
	protected static final String LOG_NAME = "sumk.conf";
	protected Consumer<RefreshableSystemConfig> observer;

	@Override
	public void setConsumer(Consumer<RefreshableSystemConfig> observer) {
		this.observer = observer;
	}

	@Override
	public void onRefresh() {
		notifyListener();
		AppInfo.notifyUpdate();
	}

	protected final void notifyListener() {
		Consumer<RefreshableSystemConfig> ob = this.observer;
		if (ob != null) {
			ob.accept(this);
		}
	}

	@Override
	protected final void onStart() {
		this.init();
		RawLog.setLogger(RawLog.SLF4J_LOG);
		this.notifyListener();
	}

	protected abstract void init();
}
