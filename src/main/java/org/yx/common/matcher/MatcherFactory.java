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
package org.yx.common.matcher;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.yx.log.ConsoleLog;
import org.yx.util.StringUtil;

public class MatcherFactory {
	public static final String WILDCARD = "*";

	private static final Logger log = ConsoleLog.get("sumk.common.matcher");

	private Set<String> exacts;

	private List<String> matchStarts;

	private List<String> matchEnds;

	public static TextMatcher createWildcardMatcher(String patterns, int minPatternLength) {

		if (patterns == null) {
			return BooleanMatcher.FALSE;
		}
		Set<String> exact = new HashSet<>();
		Set<String> start = new HashSet<>();
		Set<String> end = new HashSet<>();
		String[] noProxyArray = StringUtil.splitByComma(patterns);
		for (String s : noProxyArray) {
			if ((s = s.trim()).isEmpty()) {
				continue;
			}

			if (!s.contains(WILDCARD)) {
				exact.add(s);
				continue;
			}

			if (s.indexOf(WILDCARD) != s.lastIndexOf(WILDCARD)) {
				log.warn("{}出现了2次*,本配置将被忽略", s);
				continue;
			}
			if (s.length() < minPatternLength) {
				log.warn("{}的长度太短，将被忽略", s);
				continue;
			}
			if (WILDCARD.equals(s)) {
				return BooleanMatcher.TRUE;
			}
			if (s.endsWith(WILDCARD)) {
				start.add(s.substring(0, s.length() - 1));
			} else if (s.startsWith(WILDCARD)) {
				end.add(s.substring(1));
			} else {
				log.warn("{}的*不是出现在头尾，将被忽略", s);
			}
		}

		Set<String> exacts = exact.size() > 0 ? exact : null;
		String[] matchStarts = start.size() > 0 ? start.toArray(new String[start.size()]) : null;
		String[] matchEnds = end.size() > 0 ? end.toArray(new String[end.size()]) : null;
		return new WildcardMatcher(exacts, matchStarts, matchEnds);
	}

	public boolean match(String text) {

		if (this.exacts != null && this.exacts.contains(text)) {

			return true;
		}

		if (this.matchStarts != null) {
			for (String start : this.matchStarts) {
				if (text.startsWith(start)) {

					return true;
				}
			}
		}

		if (this.matchEnds != null) {
			for (String end : this.matchEnds) {
				if (text.endsWith(end)) {

					return true;
				}
			}
		}
		return false;
	}

	@Override
	public String toString() {
		return "[exacts=" + exacts + ", matchStarts=" + matchStarts + ", matchEnds=" + matchEnds + "]";
	}

}
