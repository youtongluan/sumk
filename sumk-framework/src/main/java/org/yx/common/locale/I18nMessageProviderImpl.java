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

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import org.yx.conf.AppInfo;
import org.yx.util.StringUtil;

/**
 * 一般用于国际化或提示语
 */
public class I18nMessageProviderImpl implements I18nMessageProvider {

	private static final ThreadLocal<Locale> CURRENT_LOCALE = new ThreadLocal<>();

	@Override
	public String get(String orignName, String defaultTemplate, Object... params) {
		Locale locale = this.getCurrentLocale();
		if (locale == null) {
			locale = Locale.getDefault();
		}
		return this.getInLocale(locale, orignName, defaultTemplate, params);
	}

	@Override
	public String getInLocale(Locale locale, String orignName, String defaultTemplate, Object... params) {
		List<String> names = localedNames(orignName, locale);
		String template = null;
		for (String localeName : names) {
			template = AppInfo.get(localeName);
			if (StringUtil.isNotEmpty(template)) {
				return buildMessage(template, locale, params);
			}
		}
		return buildMessage(defaultTemplate, locale, params);
	}

	public List<String> localedNames(String name, Locale locale) {
		if (locale == null) {
			return Collections.singletonList(name);
		}
		List<String> result = new ArrayList<>(3);
		result.add(name);
		String language = locale.getLanguage();
		String country = locale.getCountry();
		String variant = locale.getVariant();
		StringBuilder temp = new StringBuilder();

		if (language.length() > 0) {
			temp.append(language);
			result.add(0, buildName(name, temp));
		}
		if (country.length() > 0) {
			if (temp.length() > 0) {
				temp.append('_');
			}
			temp.append(country);
			result.add(0, buildName(name, temp));
		}

		if (variant.length() > 0 && (language.length() > 0 || country.length() > 0)) {
			if (temp.length() > 0) {
				temp.append('_');
			}
			temp.append(variant);
			result.add(0, buildName(name, temp));
		}

		return result;
	}

	protected String buildName(String name, StringBuilder temp) {
		StringBuilder sb = new StringBuilder(temp);
		if (sb.length() > 0) {
			sb.append('.');
		}
		return sb.append(name).toString();
	}

	protected String buildMessage(String template, Locale locale, Object... params) {

		MessageFormat formater = new MessageFormat(template);
		if (locale != null) {
			formater.setLocale(locale);
		}
		return formater.format(params);
	}

	@Override
	public void setCurrentLocale(Locale locale) {
		CURRENT_LOCALE.set(locale);
	}

	@Override
	public void clearCurrentLocale() {
		CURRENT_LOCALE.remove();
	}

	@Override
	public Locale getCurrentLocale() {
		return CURRENT_LOCALE.get();
	}

}
