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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.Part;

import org.yx.annotation.Bean;
import org.yx.annotation.spec.UploadSpec;
import org.yx.exception.BizException;
import org.yx.http.HttpErrorCode;
import org.yx.http.kit.InnerHttpUtil;
import org.yx.log.Logs;
import org.yx.util.M;

@Bean
public class MultipartHandler implements HttpHandler {

	@Override
	public boolean supportRestType(String type) {
		return RestType.MULTI_PART.equals(type);
	}

	@Override
	public int order() {
		return 1300;
	}

	@Override
	public void handle(WebContext ctx) throws Throwable {
		UploadSpec upload = ctx.node().upload();
		if (upload == null) {
			Logs.http().error("{}缺少 @Upload", ctx.rawAct());
			throw BizException.create(HttpErrorCode.UPLOAD_ANNOTATION_MISS,
					M.get("sumk.http.upload.error.annocation", "不是上传接口", ctx.rawAct()));
		}
		Collection<Part> list = ctx.httpRequest().getParts();
		if (list == null || list.isEmpty()) {
			throw BizException.create(HttpErrorCode.FILE_MISS, "没有文件");
		}
		List<MultipartItem> files = new ArrayList<>(list.size());
		for (Part p : list) {

			if (upload.paramName().equals(p.getName())) {
				ctx.data(InnerHttpUtil.extractData(p.getInputStream(), (int) p.getSize()));
				continue;
			}
			files.add(new MultiItemImpl(p));
		}
		MultipartHolder.set(Collections.unmodifiableList(files));
	}

}
