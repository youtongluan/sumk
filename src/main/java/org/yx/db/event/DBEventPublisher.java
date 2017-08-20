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
package org.yx.db.event;

import org.yx.db.listener.DBListener;
import org.yx.listener.ListenerGroup;
import org.yx.listener.ListenerGroupImpl;

public class DBEventPublisher {

	private static ListenerGroup<DBEvent> group = new ListenerGroupImpl<>();

	public static void publish(DBEvent event) {
		group.listen(event);
	}

	public static synchronized boolean addListener(DBListener<DBEvent> listener) {
		group.addListener(listener);
		return true;
	}

	public static synchronized void removeListener(DBListener<DBEvent> listener) {
		group.removeListener(listener);
	}

}