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
package org.yx.http.server;

import javax.servlet.annotation.MultipartConfig;

import org.yx.annotation.Bean;
import org.yx.annotation.http.SumkServlet;
import org.yx.exception.BizException;
import org.yx.http.HttpErrorCode;
import org.yx.http.handler.HttpHandlerChain;
import org.yx.http.handler.WebContext;
import org.yx.http.kit.HttpException;
import org.yx.util.M;

/**
 * 
 * @author Administrator
 */
@Bean
@SumkServlet(value = { "/upload/*" }, loadOnStartup = -1, appKey = "upload")
@MultipartConfig(location = "", maxFileSize = -1L, maxRequestSize = -1L, fileSizeThreshold = -1)
public class MultipartServer extends AbstractHttpServer {

	private static final long serialVersionUID = 1L;
	final static String MULTI = "multipart/form-data";

	@Override
	protected void handle(WebContext wc) throws Throwable {
		if (HttpHandlerChain.multipart == null) {
			log.error("上传功能被禁用");
			throw HttpException.create(HttpErrorCode.UPLOAD_DISABLED, "上传功能暂时无法使用");
		}
		String contextType = wc.httpRequest().getContentType();
		if (contextType == null || !contextType.startsWith(MULTI)) {
			log.error("the MIME of act is " + MULTI + ",not " + contextType);
			throw BizException.create(HttpErrorCode.UPLOAD_NOT_MULTI_TYPE,
					M.get("sumk.http.upload.error.contentType", "ContentType不是" + MULTI, wc.rawAct()));
		}
		if (wc.httpNode().upload() == null) {
			log.error("{}缺少 @upload", wc.rawAct());
			throw BizException.create(HttpErrorCode.UPLOAD_ANNOTATION_MISS,
					M.get("sumk.http.upload.error.annocation", "不是上传接口", wc.rawAct()));
		}
		HttpHandlerChain.multipart.handle(wc);
	}

}
