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
package org.yx.http.handler;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Objects;

import javax.servlet.http.Part;

public class MultiItemImpl implements MultipartItem {
	private final Part part;

	public MultiItemImpl(Part part) {
		this.part = Objects.requireNonNull(part);
	}

	@Override
	public String getName() {
		return part.getName();
	}

	@Override
	public long getSize() {
		return part.getSize();
	}

	@Override
	public InputStream getInputStream() throws IOException {
		return part.getInputStream();
	}

	@Override
	public String getSubmittedFileName() {
		return part.getSubmittedFileName();
	}

	@Override
	public String getHeader(String name) {
		return part.getHeader(name);
	}

	@Override
	public Collection<String> getHeaders(String name) {
		return part.getHeaders(name);
	}

	@Override
	public Collection<String> getHeaderNames() {
		return part.getHeaderNames();
	}

	@Override
	public String getContentType() {
		return part.getContentType();
	}

}
