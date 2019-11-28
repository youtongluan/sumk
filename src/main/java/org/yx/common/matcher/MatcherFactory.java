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

import static org.yx.common.matcher.WildcardMatcher.WILDCARD;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

import org.yx.log.InnerLog;
import org.yx.util.StringUtil;

public class MatcherFactory {

	public static Predicate<String> createWildcardMatcher(String patterns, int minPatternLength) {

		if (patterns == null || patterns.isEmpty()) {
			return BooleanMatcher.FALSE;
		}
		Set<String> exact = new HashSet<>();
		Set<String> matchStart = new HashSet<>();
		Set<String> matchEnd = new HashSet<>();
		Set<String> matchContain = new HashSet<>();
		patterns = StringUtil.toLatin(patterns);
		String[] noProxyArray = patterns.split(",");
		String doubleWild = WILDCARD + WILDCARD;
		for (String s : noProxyArray) {
			if ((s = s.trim()).isEmpty()) {
				continue;
			}
			if (s.length() < minPatternLength) {
				InnerLog.warn("sumk.conf", s + "的长度太短，将被忽略");
				continue;
			}

			int beginIndex = s.indexOf(WILDCARD);
			if (beginIndex < 0) {
				exact.add(s);
				continue;
			}

			if (WILDCARD.equals(s) || doubleWild.equals(s)) {
				return BooleanMatcher.TRUE;
			}

			if (beginIndex == s.length() - 1) {
				matchStart.add(s.substring(0, beginIndex));
				continue;
			}

			if (beginIndex != 0) {
				InnerLog.warn("sumk.conf", s + "的*不是出现在头尾，将被忽略");
				continue;
			}

			int endIndex = s.indexOf(WILDCARD, beginIndex + 1);
			if (endIndex < 0) {
				matchEnd.add(s.substring(1));
				continue;
			}
			if (endIndex == s.length() - 1) {
				matchContain.add(s.substring(1, endIndex));
				continue;
			}
			InnerLog.warn("sumk.conf", s + "的*不止出现在头尾，将被忽略！！！");
		}

		Set<String> exacts = exact.size() > 0 ? exact : null;
		String[] matchStarts = matchStart.size() > 0 ? matchStart.toArray(new String[0]) : null;
		String[] matchEnds = matchEnd.size() > 0 ? matchEnd.toArray(new String[0]) : null;
		String[] matchContains = matchContain.size() > 0 ? matchContain.toArray(new String[0]) : null;
		if (exacts == null && matchStarts == null && matchEnds == null && matchContains == null) {
			return BooleanMatcher.FALSE;
		}
		return new WildcardMatcher(exacts, matchStarts, matchEnds, matchContains);
	}

}
