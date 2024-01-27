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
package org.yx.db.event;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.yx.db.conn.SumkConnection;

public final class EventLane {
	private List<DBEvent> events;

	public EventLane() {
	}

	private List<DBEvent> makeSureEvents() {
		if (this.events == null) {
			this.events = new ArrayList<>();
		}
		return this.events;
	}

	public void pubuishModify(SumkConnection conn, DBEvent event) {
		if (event == null) {
			return;
		}
		if (!conn.getAutoCommit()) {
			makeSureEvents().add(event);
		} else {
			DBEventPublisher.publishModify(event);
		}
	}

	public void commit(SumkConnection conn) throws SQLException {
		if (this.events == null) {
			conn.commit();
			return;
		}
		DBEventPublisher.onCommit(events);
		conn.commit();
		DBEventPublisher.publishModify(events);
		this.clear();
	}

	public void clear() {
		this.events = null;
	}

	public boolean existModifyEvent(String table) {
		if (this.events == null) {
			return false;
		}
		for (DBEvent event : this.events) {
			if (event.getTable().equals(table) && (event instanceof ModifyEvent)) {
				return true;
			}
		}
		return false;
	}
}
