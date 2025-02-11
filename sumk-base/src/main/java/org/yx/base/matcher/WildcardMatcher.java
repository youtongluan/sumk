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

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;

import org.yx.base.ItemJoiner;
import org.yx.util.CollectionUtil;

/**
 * 或类型的匹配，只要能匹配中一个条件，就认为匹配成功
 *
 */
public class WildcardMatcher implements Predicate<String> {

	private final Set<String> exacts;

	private final String[] matchStarts;

	private final String[] matchEnds;

	private final String[] contains;

	private final HeadAndTail[] headTails;

	WildcardMatcher(Set<String> exacts, Set<String> matchStart, Set<String> matchEnd, Set<String> matchContain,
			Set<HeadAndTail> headAndTailMatch) {
		this.exacts = CollectionUtil.isEmpty(exacts) ? null : new HashSet<>(exacts);
		this.matchStarts = toArray(matchStart);
		this.matchEnds = toArray(matchEnd);
		this.contains = toArray(matchContain);
		this.headTails = CollectionUtil.isEmpty(headAndTailMatch) ? null
				: headAndTailMatch.toArray(new HeadAndTail[headAndTailMatch.size()]);
	}

	private String[] toArray(Collection<String> src) {
		if (CollectionUtil.isEmpty(src)) {
			return null;
		}
		return src.toArray(new String[src.size()]);
	}

	@Override
	public boolean test(String text) {

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

		if (this.contains != null) {
			for (String c : this.contains) {
				if (text.contains(c)) {
					return true;
				}
			}
		}

		if (this.headTails != null) {
			for (HeadAndTail ht : this.headTails) {
				if (ht.test(text)) {
					return true;
				}
			}
		}
		return false;
	}

	public Collection<String> exacts() {
		return exacts == null ? null : Collections.unmodifiableSet(exacts);
	}

	public List<String> matchStarts() {
		return CollectionUtil.unmodifyList(matchStarts);
	}

	public List<String> matchEnds() {
		return CollectionUtil.unmodifyList(matchEnds);
	}

	public List<String> contains() {
		return CollectionUtil.unmodifyList(contains);
	}

	@Override
	public String toString() {
		ItemJoiner join = new ItemJoiner(",");
		if (exacts != null) {
			join.item().append("exacts:").append(exacts);
		}
		if (matchStarts != null) {
			join.item().append("matchStarts:").append(Arrays.toString(matchStarts));
		}
		if (matchEnds != null) {
			join.item().append("matchEnds:").append(Arrays.toString(matchEnds));
		}
		if (contains != null) {
			join.item().append("contains:").append(Arrays.toString(contains));
		}
		if (headTails != null) {
			join.item().append("headTails:").append(Arrays.toString(headTails));
		}
		return join.toString();
	}

	public static final class HeadAndTail implements Predicate<String> {
		final String head;
		final String tail;

		public HeadAndTail(String head, String tail) {
			this.head = Objects.requireNonNull(head);
			this.tail = Objects.requireNonNull(tail);
		}

		@Override
		public boolean test(String text) {
			if (text.length() < head.length() + tail.length()) {
				return false;
			}
			return text.startsWith(head) && text.endsWith(tail);
		}

		@Override
		public String toString() {
			return "HeadAndTail [head=" + head + ", tail=" + tail + "]";
		}
	}

}
