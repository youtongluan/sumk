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
package org.yx.base.matcher;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

import org.yx.log.RawLog;
import org.yx.util.CollectionUtil;
import org.yx.util.StringUtil;

public class Matchers {

	/**
	 * 表达式的分隔符
	 */
	public static final String SPLIT = ",";

	public static final String WILDCARD = "*";

	public static Predicate<String> createWildcardMatcher(String patterns) {
		return createWildcardMatcher(patterns, 1);
	}

	public static Predicate<String> createWildcardMatcher(String patterns, int minPatternLength) {
		if (patterns == null || patterns.isEmpty()) {
			return BooleanMatcher.FALSE;
		}
		String[] array = StringUtil.toLatin(patterns).split(SPLIT);
		return createWildcardMatcher(CollectionUtil.unmodifyList(array), minPatternLength);
	}

	public static Predicate<String> createWildcardMatcher(Collection<String> patterns, int minPatternLength) {
		if (patterns == null || patterns.isEmpty()) {
			return BooleanMatcher.FALSE;
		}
		Set<String> exact = new HashSet<>();
		Set<String> matchStart = new HashSet<>();
		Set<String> matchEnd = new HashSet<>();
		Set<String> matchContain = new HashSet<>();
		String doubleWild = WILDCARD + WILDCARD;
		for (String s : patterns) {
			if ((s = s.trim()).isEmpty()) {
				continue;
			}
			if (s.length() < minPatternLength) {
				RawLog.warn("sumk.conf", s + "的长度太短，将被忽略");
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
				RawLog.warn("sumk.conf", s + "的*不是出现在头尾，将被忽略");
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
			RawLog.warn("sumk.conf", s + "的*不止出现在头尾，将被忽略！！！");
		}

		Set<String> exacts = exact.size() > 0 ? exact : null;
		String[] matchStarts = toArray(matchStart);
		String[] matchEnds = toArray(matchEnd);
		String[] matchContains = toArray(matchContain);
		if (exacts == null && matchStarts == null && matchEnds == null && matchContains == null) {
			return BooleanMatcher.FALSE;
		}
		return new WildcardMatcher(exacts, matchStarts, matchEnds, matchContains);
	}

	private static String[] toArray(Collection<String> src) {
		if (src.size() == 0) {
			return null;
		}
		return src.toArray(new String[src.size()]);
	}

	/**
	 * 创建InOutMatcher、BooleanMatcher或其它Matcher
	 * 
	 * @param include 可以匹配的Predicate,不能为null
	 * @param exclude 被排除的Predicate,不能为null
	 * @return 返回值有可能是InOutMatcher类型，也有可能是BooleanMatcher
	 */
	public static Predicate<String> includeAndExclude(Predicate<String> include, Predicate<String> exclude) {
		if (exclude == BooleanMatcher.FALSE) {
			return include;
		}
		if (include == BooleanMatcher.FALSE || exclude == BooleanMatcher.TRUE) {
			return BooleanMatcher.FALSE;
		}
		return new InOutMatcher(include, exclude);
	}

	public static Predicate<String> includeAndExclude(String include, String exclude) {
		return includeAndExclude(createWildcardMatcher(include), createWildcardMatcher(exclude));
	}
}
