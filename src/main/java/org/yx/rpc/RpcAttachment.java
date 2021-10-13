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

import org.yx.common.context.ActionContext;
import org.yx.common.context.Attachable;

public class RpcAttachment implements Attachable {

	private static final RpcAttachment inst = new RpcAttachment();

	private RpcAttachment() {
	}

	public static RpcAttachment get() {
		return inst;
	}

	@Override
	public Map<String, String> attachmentView() {
		return ActionContext.current().attachmentView();
	}

	@Override
	public void setAttachment(String key, String value) {
		ActionContext.current().setAttachment(key, value);
	}

	@Override
	public String getAttachment(String key) {
		return ActionContext.current().getAttachment(key);
	}

}
