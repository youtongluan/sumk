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

import org.yx.listener.ComposeListener;
import org.yx.listener.SumkListener;

public final class DBEventPublisher {

	private static final ComposeListener group = new ComposeListener();

	public static void publish(DBEvent event) {
		group.listen(event);
	}

	public static void setListener(SumkListener[] listeners) {
		group.setListener(listeners);
	}

	public List<SumkListener> getListeners() {
		return group.getListeners();
	}

}