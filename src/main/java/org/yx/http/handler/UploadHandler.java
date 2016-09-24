package org.yx.http.handler;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import org.yx.exception.HttpException;
import org.yx.http.Upload;
import org.yx.http.Web;
import org.yx.log.Log;

public class UploadHandler implements HttpHandler {

	@Override
	public boolean accept(Web web) {
		return true;
	}

	@Override
	public boolean handle(WebContext ctx) throws Throwable {
		HttpServletRequest request = ctx.getHttpRequest();
		Upload uploadInfo = ctx.getInfo().getUpload();

		DiskFileItemFactory factory = new DiskFileItemFactory();
		factory.setSizeThreshold(uploadInfo.maxSize());
		ServletFileUpload upload = new ServletFileUpload(factory);
		upload.setHeaderEncoding(ctx.getCharset());
		List<FileItem> list = (List<FileItem>) upload.parseRequest(request);
		if (list == null || list.isEmpty()) {
			HttpException.throwException(this.getClass(), "没有文件");
		}

		List<UploadFile> files = new ArrayList<>(list.size());
		for (FileItem fi : list) {
			String name = fi.getName();
			if (name == null) {
				if ("data".equals(fi.getFieldName())) {
					ctx.setData(fi.get());
					continue;
				}

				continue;
			}
			Log.get(this.getClass()).debug("fileupload#name:{},field:{}", name, fi.getFieldName());
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
