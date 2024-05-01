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
package org.yx.common.locale;

import java.util.Locale;
import java.util.Objects;

/**
 * 一般用于国际化或提示语
 */
public final class I18n {

	private static I18nMessageProvider provider = new I18nMessageProviderImpl();

	static I18nMessageProvider getProvider() {
		return provider;
	}

	static void setProvider(I18nMessageProvider provider) {
		I18n.provider = Objects.requireNonNull(provider);
	}

	public static String get(String orignName, String defaultTemplate, Object... params) {
		return provider.get(orignName, defaultTemplate, params);
	}

	public static String getInLocale(Locale locale, String orignName, String defaultTemplate, Object... params) {
		return provider.getInLocale(locale, orignName, defaultTemplate, params);
	}

	public static void setCurrentLocale(Locale locale) {
		provider.setCurrentLocale(locale);
	}

	public static void clearCurrentLocale() {
		provider.clearCurrentLocale();
	}

	public static Locale getCurrentLocale() {
		return provider.getCurrentLocale();
	}
}
