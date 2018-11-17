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
package org.yx.rpc;

import java.util.Map;

import org.yx.common.ThreadContext;

public class RpcAttachment implements Attachable, AutoCloseable {

	private static final RpcAttachment inst = new RpcAttachment();

	private RpcAttachment() {
	}

	public static RpcAttachment get() {
		return inst;
	}

	public static void remove() {
		ThreadContext.remove();
	}

	/**
	 * 这个方法跟remove()是等价的
	 */
	@Override
	public void close() {
		remove();
	}

	@Override
	public Map<String, String> getAttachments() {
		return ThreadContext.get().getAttachments();
	}

	@Override
	public void setAttachments(Map<String, String> attachments) {
		ThreadContext.get().setAttachments(attachments);
	}

	@Override
	public void setAttachment(String key, String value) {
		ThreadContext.get().setAttachment(key, value);
	}

	@Override
	public void setAttachmentIfAbsent(String key, String value) {
		ThreadContext.get().setAttachmentIfAbsent(key, value);
	}

	@Override
	public void addAttachments(Map<String, String> attachments) {
		ThreadContext.get().addAttachments(attachments);
	}

	@Override
	public void addAttachmentsIfAbsent(Map<String, String> attachments) {
		ThreadContext.get().addAttachmentsIfAbsent(attachments);
	}

	@Override
	public String getAttachment(String key) {
		return ThreadContext.get().getAttachment(key);
	}

	@Override
	public String getAttachment(String key, String defaultValue) {
		return ThreadContext.get().getAttachment(key, defaultValue);
	}

}
