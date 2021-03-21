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
package org.yx.annotation.spec;

import javax.servlet.DispatcherType;

public class SumkFilterSpec {
	private final String name;
	private final String[] path;
	private final DispatcherType[] dispatcherType;
	private final boolean isMatchAfter;
	private final boolean asyncSupported;

	public SumkFilterSpec(String name, String[] path, DispatcherType[] dispatcherType, boolean isMatchAfter,
			boolean asyncSupported) {
		this.name = name;
		this.path = path;
		this.dispatcherType = dispatcherType;
		this.isMatchAfter = isMatchAfter;
		this.asyncSupported = asyncSupported;
	}

	public String name() {
		return this.name;
	}

	public String[] path() {
		return this.path;
	}

	public boolean asyncSupported() {
		return this.asyncSupported;
	}

	public DispatcherType[] dispatcherType() {
		return dispatcherType;
	}

	public boolean isMatchAfter() {
		return isMatchAfter;
	}
}
