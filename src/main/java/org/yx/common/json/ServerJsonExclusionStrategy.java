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
package org.yx.common.json;

import java.lang.annotation.Annotation;

import org.yx.annotation.ExcludeFromParams;
import org.yx.annotation.ExcludeFromResponse;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.GsonBuilder;

public class ServerJsonExclusionStrategy implements ExclusionStrategy {

	private final Class<? extends Annotation> annotation;

	public ServerJsonExclusionStrategy(Class<? extends Annotation> excludeAnnotation) {
		this.annotation = excludeAnnotation;
	}

	@Override
	public boolean shouldSkipField(FieldAttributes f) {
		return f.getAnnotation(annotation) != null;
	}

	@Override
	public boolean shouldSkipClass(Class<?> clazz) {
		return clazz.getAnnotation(annotation) != null;
	}

	public static GsonBuilder addServerExclusionStrategy(GsonBuilder gb) {
		return gb.addSerializationExclusionStrategy(new ServerJsonExclusionStrategy(ExcludeFromResponse.class))
				.addDeserializationExclusionStrategy(new ServerJsonExclusionStrategy(ExcludeFromParams.class));
	}
}
