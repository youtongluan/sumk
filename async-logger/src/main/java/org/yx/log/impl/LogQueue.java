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
package org.yx.log.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

import org.yx.base.matcher.BooleanMatcher;
import org.yx.base.matcher.Matchers;
import org.yx.conf.AppInfo;
import org.yx.log.ConsoleLog;
import org.yx.util.SumkThreadPool;

public abstract class LogQueue implements Runnable {

	protected final String name;

	private int interval;

	private long handleLogCount;

	private boolean jobStarted;

	protected final BlockingQueue<LogObject> queue;

	private Predicate<String> matcher = BooleanMatcher.FALSE;

	public LogQueue(String name) {
		this.name = name;
		this.interval = AppInfo.getInt("sumk.log.interval." + name, 1000);
		this.queue = new LinkedBlockingQueue<>(Integer.getInteger("sumk.log.queue." + name, 2_0000));
	}

	protected boolean accept(LogObject logObject) {
		String module = logObject.getLoggerName();
		return this.matcher.test(module);
	}

	protected abstract void flush(boolean idle) throws Exception;

	public void config(Map<String, String> configMap) {
		if (configMap == null) {
			configMap = Collections.emptyMap();
		}
		String patterns = configMap.get(LogAppenders.MODULE);
		if (patterns == null || patterns.isEmpty()) {
			patterns = Matchers.WILDCARD;
		}
		this.matcher = Matchers.includeAndExclude(patterns, configMap.get("exclude"));
		ConsoleLog.defaultLog.debug("{} set matcher ：{}", this.name, this.matcher);
	}

	public final String name() {
		return this.name;
	}

	public boolean offer(LogObject logObject) {
		if (!accept(logObject)) {
			return false;
		}
		return queue.offer(logObject);
	}

	protected abstract void output(List<LogObject> list) throws Exception;

	@Override
	public void run() {
		Thread.currentThread().setName("log-" + this.name);

		while (true) {
			try {
				this.flush(this.consume());
			} catch (Throwable e) {
				ConsoleLog.defaultLog.warn("日志消费失败，" + e.toString(), e);

				if (Thread.currentThread().isInterrupted() || e.getClass() == InterruptedException.class) {
					ConsoleLog.defaultLog.warn("{}日志停止了", this.name);
					Thread.currentThread().interrupt();
					return;
				}
			}
		}

	}

	private boolean consume() throws Exception {
		LogObject message = queue.poll(interval, TimeUnit.MILLISECONDS);
		if (message == null) {
			return true;
		}
		int batch = Math.min(queue.size() + 10, 100);
		List<LogObject> list = new ArrayList<>(batch);
		list.add(message);
		queue.drainTo(list, batch - 1);

		while (list.size() > 0) {
			output(list);
			this.handleLogCount += list.size();
			list.clear();
			queue.drainTo(list, batch);
		}
		return false;
	}

	public synchronized boolean start(Map<String, String> map) {
		if (map == null) {
			map = Collections.emptyMap();
		}
		if (!onStart(map)) {
			return false;
		}
		ConsoleLog.defaultLog.debug("{} started by {}", this, map);
		if (!jobStarted) {
			startJob();
			this.jobStarted = true;
		}
		return jobStarted;
	}

	protected void startJob() {
		SumkThreadPool.executor().execute(this);
	}

	protected abstract boolean onStart(Map<String, String> configMap);

	public synchronized void stop() throws Exception {
		this.matcher = BooleanMatcher.FALSE;
		ConsoleLog.defaultLog.info("日志{} stoped", this.name);
	}

	public void setInterval(int interval) {
		if (interval > 0) {
			this.interval = interval;
		}
	}

	public long getHandleLogCount() {
		return handleLogCount;
	}

	protected Predicate<String> getMatcher() {
		return matcher;
	}

	protected void setMatcher(Predicate<String> matcher) {
		this.matcher = Objects.requireNonNull(matcher);
	}

	@Override
	public String toString() {
		return this.name + " [queue size:" + queue.size() + ",matcher:" + matcher + ",logCount:" + handleLogCount + "]";
	}
}
