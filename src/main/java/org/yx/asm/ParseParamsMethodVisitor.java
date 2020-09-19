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
package org.yx.asm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.objectweb.asm.Label;
import org.objectweb.asm.Type;
import org.slf4j.Logger;
import org.yx.exception.SumkException;
import org.yx.log.Log;

public class ParseParamsMethodVisitor extends EmptyMethodVisitor {

	private final MethodParamInfo info;

	private final List<LocalArg> argPojos;
	private final int maxIndex;

	public ParseParamsMethodVisitor(int api, MethodParamInfo info) {
		super(api);
		this.info = info;
		this.maxIndex = info.getArgNames().length * 2;
		this.argPojos = new ArrayList<>(maxIndex);
	}

	@Override
	public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {

		if (index == 0 && "this".equals(name)) {
			return;
		}
		if (index >= maxIndex) {
			return;
		}
		argPojos.add(new LocalArg(name, desc, index, signature));
	}

	@Override
	public void visitEnd() {
		Logger log = Log.get("sumk.asm");
		String methodName = info.getMethod().getName();
		Type[] args = info.getArgumentTypes();
		Collections.sort(argPojos);
		if (log.isTraceEnabled()) {
			log.trace("{}.{}():{}", info.getMethod().getDeclaringClass().getName(), methodName, argPojos);
		}
		String[] argNames = info.getArgNames();
		String[] signatures = info.getSignatures();
		int size = argNames.length;
		if (argPojos.size() < size) {
			log.error("{}.{},real param size:{},but is {}", info.getMethod().getDeclaringClass().getName(), methodName,
					size, argPojos.size());
			throw new SumkException(123253, "failed to parse parameter because parameter size not satisfied");
		}
		for (int i = 0; i < size; i++) {
			LocalArg pojo = argPojos.get(i);
			if (!args[i].getDescriptor().equals(pojo.desc)) {
				log.error("{}.{},i:{},index:{},except:{},indeed:{}", info.getMethod().getDeclaringClass().getName(),
						methodName, i, pojo.index, args[i].getDescriptor(), pojo.desc);
				throw new SumkException(123253, "failed to parse parameter");
			}
			argNames[i] = pojo.name;
			signatures[i] = pojo.signature;
		}
	}

	private static class LocalArg implements Comparable<LocalArg> {
		final String name;
		final String desc;
		final int index;
		final String signature;

		public LocalArg(String name, String desc, int index, String signature) {
			this.name = name;
			this.desc = desc;
			this.index = index;
			this.signature = signature;
		}

		@Override
		public int compareTo(LocalArg o) {
			return index - o.index;
		}

		@Override
		public String toString() {
			return "{name=" + name + ", desc=" + desc + ", index=" + index + ", signature=" + signature + "}";
		}

	}
}
