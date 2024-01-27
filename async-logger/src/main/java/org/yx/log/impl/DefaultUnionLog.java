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
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import org.yx.base.matcher.Matchers;
import org.yx.conf.AppInfo;
import org.yx.conf.SystemConfig;

public class DefaultUnionLog extends LogQueue implements UnionLog {

	private UnionLogDao dao;
	private boolean started;
	private Consumer<SystemConfig> observer;

	private Function<LogObject, UnionLogObject> logObjectSerializer;

	private Supplier<Predicate<String>> matcherSupplier;

	public DefaultUnionLog() {
		super("unionlog");
		this.dao = new LocalFileDao();
		this.matcherSupplier = () -> Matchers.includeAndExclude(AppInfo.get("sumk.unionlog.module", null),
				AppInfo.get("sumk.unionlog.exclude", null));
	}

	@Override
	protected void flush(boolean idle) throws Exception {
		this.dao.flush(idle);
	}

	@Override
	protected void output(List<LogObject> list) throws Exception {
		List<UnionLogObject> logs = new ArrayList<>(list.size());
		for (LogObject raw : list) {
			UnionLogObject log = this.logObjectSerializer.apply(raw);
			if (log == null) {
				continue;
			}
			logs.add(log);
		}
		if (logs.size() > 0) {
			this.dao.store(logs);
		}
	}

	@Override
	protected boolean onStart(Map<String, String> configMap) {
		if (this.logObjectSerializer == null) {
			this.logObjectSerializer = getLogObjectSerializer();
		}
		if (this.observer == null) {
			this.observer = c -> setMatcher(matcherSupplier.get());
			AppInfo.addObserver(this.observer);
		}
		this.started = true;
		return true;
	}

	@Override
	public synchronized void stop() throws Exception {
		this.started = false;
		super.stop();

	}

	@Override
	public boolean directOffer(LogObject logObject) {
		if (!this.started) {
			return false;
		}
		return this.queue.offer(logObject);
	}

	@Override
	public boolean offer(LogObject logObject) {
		if (!this.started) {
			return false;
		}
		return super.offer(logObject);
	}

	@Override
	public boolean isStarted() {
		return this.started;
	}

	public Supplier<Predicate<String>> getMatcherSupplier() {
		return matcherSupplier;
	}

	public void setMatcherSupplier(Supplier<Predicate<String>> matcherSupplier) {
		this.matcherSupplier = Objects.requireNonNull(matcherSupplier);
		this.setMatcher(this.matcherSupplier.get());
	}

	public Function<LogObject, UnionLogObject> getLogObjectSerializer() {
		return logObjectSerializer == null ? new UnionLogObjectSerializer() : this.logObjectSerializer;
	}

	public void setLogObjectSerializer(Function<LogObject, UnionLogObject> logObjectSerializer) {
		this.logObjectSerializer = Objects.requireNonNull(logObjectSerializer);
	}

	public UnionLogDao getDao() {
		return dao;
	}

	public void setDao(UnionLogDao dao) {
		this.dao = Objects.requireNonNull(dao);
	}

	public void removeObserver() {
		Consumer<SystemConfig> ob = this.observer;
		if (ob == null) {
			return;
		}
		AppInfo.removeObserver(ob);
		this.observer = null;
	}
}
