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

import org.yx.base.matcher.WildcardMatcher.HeadAndTail;
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

	/**
	 * 创建表达式。这里的表达式不做全角半角处理，也不对逗号做分割，但允许包含null
	 * 
	 * @param patterns         表达式字符串列表，支持为null。这里的每一个都是最小的表达式，不支持内部再含有逗号
	 * @param minPatternLength 最小长度，会忽略pattern长度小于这个最小长度的
	 * @return 表达式判断器
	 */
	public static Predicate<String> createWildcardMatcher(Collection<String> patterns, int minPatternLength) {
		if (patterns == null || patterns.isEmpty()) {
			return BooleanMatcher.FALSE;
		}
		Set<String> exact = new HashSet<>();
		Set<String> matchStart = new HashSet<>();
		Set<String> matchEnd = new HashSet<>();
		Set<String> matchContain = new HashSet<>();
		Set<HeadAndTail> headAndTailMatch = new HashSet<>();
		final String doubleWild = WILDCARD + WILDCARD;
		for (String s : patterns) {
			if (s == null || (s = s.trim()).isEmpty()) {
				continue;
			}
			if (s.length() < minPatternLength) {
				RawLog.warn("sumk.conf", "[" + s + "]的长度太短，被忽略.最小长度为:" + minPatternLength);
				continue;
			}

			if (WILDCARD.equals(s) || doubleWild.equals(s)) {
				return BooleanMatcher.TRUE;
			}

			int wildCount = s.length() - s.replace(WILDCARD, "").length();
			if (wildCount > 2) {
				RawLog.warn("sumk.conf", "[" + s + "]的*出现次数超过2次，将被忽略");

			}
			if (wildCount == 0) {
				exact.add(s);
				continue;
			}
			int firstIndex = s.indexOf(WILDCARD);
			if (wildCount == 1) {
				if (firstIndex == 0) {
					matchEnd.add(s.substring(1));
					continue;
				}
				if (firstIndex == s.length() - 1) {
					matchStart.add(s.substring(0, firstIndex));
					continue;
				}
				HeadAndTail h = new HeadAndTail(s.substring(0, firstIndex), s.substring(firstIndex + 1));
				headAndTailMatch.add(h);
			}
			if (wildCount == 2) {
				if (firstIndex == 0 && s.endsWith(WILDCARD)) {
					matchContain.add(s.substring(1, s.length() - 1));
					continue;
				}
				RawLog.warn("sumk.conf", "[" + s + "]的*不出现了2次，但不在头尾，将被忽略");
				continue;
			}

		}

		if (exact.isEmpty() && matchStart.isEmpty() && matchEnd.isEmpty() && matchContain.isEmpty()
				&& headAndTailMatch.isEmpty()) {
			return BooleanMatcher.FALSE;
		}

		return new WildcardMatcher(exact, matchStart, matchEnd, matchContain, headAndTailMatch);
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
