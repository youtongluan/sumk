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
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.yx.annotation.Bean;
import org.yx.annotation.http.Upload;
import org.yx.annotation.http.Web;
import org.yx.common.SumkLogs;
import org.yx.exception.HttpException;
import org.yx.http.kit.InnerHttpUtil;

@Bean
public class UploadHandler implements HttpHandler {

	@Override
	public boolean supportRestType(RestType type) {
		return type == RestType.UPLOAD;
	}

	@Override
	public int order() {
		return 1300;
	}

	@Override
	public boolean accept(Web web) {
		return true;
	}

	@Override
	public boolean handle(WebContext ctx) throws Throwable {
		HttpServletRequest request = ctx.httpRequest();
		Upload uploadInfo = ctx.httpNode().upload;

		DiskFileItemFactory factory = new DiskFileItemFactory();
		factory.setSizeThreshold(uploadInfo.maxSize());
		ServletFileUpload upload = new ServletFileUpload(factory);
		upload.setHeaderEncoding(ctx.charset().name());
		List<FileItem> list = upload.parseRequest(request);
		if (list == null || list.isEmpty()) {
			HttpException.throwException(this.getClass(), "没有文件");
		}
		List<UploadFile> files = new ArrayList<>(list.size());
		for (FileItem fi : list) {
			String name = fi.getName();
			if (name == null) {
				if ("data".equals(fi.getFieldName())) {
					ctx.data(InnerHttpUtil.extractData(fi.get()));
					continue;
				}
				continue;
			}
			SumkLogs.HTTP_LOG.debug("fileupload#name:{},field:{}", name, fi.getFieldName());
			name = name.toLowerCase();
			boolean valid = false;
			for (String ext : uploadInfo.exts()) {
				if (name.endsWith(ext)) {
					UploadFile item = new UploadFile();
					item.fieldName = fi.getFieldName();
					item.name = fi.getName();
					item.inputStream = fi.getInputStream();
					item.size = fi.getSize();
					files.add(item);
					valid = true;
					break;
				}
			}
			if (!valid) {
				HttpException.throwException(this.getClass(), name + "不是有效的文件类型");
			}
		}
		UploadFileHolder.setFiles(files);
		return false;
	}

}
