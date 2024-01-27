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
package org.yx.http.handler;

import java.util.List;

public final class MultipartHolder {
	private static final ThreadLocal<List<MultipartItem>> items = new ThreadLocal<>();

	public static List<MultipartItem> get() {
		return items.get();
	}

	static void set(List<MultipartItem> fs) {
		items.set(fs);
	}

	public static void remove() {
		items.remove();
	}

}
