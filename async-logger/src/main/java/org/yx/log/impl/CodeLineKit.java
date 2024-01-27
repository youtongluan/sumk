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

import java.util.Objects;
import java.util.function.BiFunction;

import org.slf4j.Marker;
import org.yx.log.CodeLineMarker;

public final class CodeLineKit {

	private static BiFunction<Marker, String, CodeLine> parser = (marker, logModule) -> {
		if (marker != null && marker.getClass() == CodeLineMarker.class) {
			return LogHelper.extractCodeLine(marker.getName());
		}
		return LogHelper.extractCodeLine("org.yx.log.");
	};

	public static BiFunction<Marker, String, CodeLine> getParser() {
		return parser;
	}

	public static void setParser(BiFunction<Marker, String, CodeLine> parser) {
		CodeLineKit.parser = Objects.requireNonNull(parser);
	}

	public static CodeLine parse(Marker marker, String name) {
		return parser.apply(marker, name);
	}
}
