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

import java.util.List;

import org.yx.bean.IOC;
import org.yx.common.listener.EventBus;
import org.yx.conf.Const;

public final class DBEventPublisher {

	private static EventBus modifyBus;
	private static EventBus queryBus;
	private static EventBus onCommitBus;

	public static void init() {
		modifyBus = IOC.get(Const.LISTENER_DB_MODIFY, EventBus.class);
		queryBus = IOC.get(Const.LISTENER_DB_QUERY, EventBus.class);
		onCommitBus = IOC.get(Const.LISTENER_DB_MODIFY_ON_COMMIT, EventBus.class);
	}

	public static void onCommit(List<DBEvent> events) {
		if (onCommitBus != null) {
			onCommitBus.publishBatch(events);
		}
	}

	public static void publishModify(List<DBEvent> events) {
		modifyBus.publishBatch(events);
	}

	public static void publishModify(DBEvent event) {
		modifyBus.publish(event);
	}

	public static void publishQuery(QueryEvent event) {
		queryBus.publish(event);
	}

}