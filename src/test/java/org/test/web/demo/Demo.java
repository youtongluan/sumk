package org.test.web.demo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.yx.exception.BizException;
import org.yx.http.EncryptType;
import org.yx.http.Upload;
import org.yx.http.Web;
import org.yx.http.handler.UploadFile;
import org.yx.http.handler.UploadFileHolder;

public class Demo {

	@Web(value = "echo")
	public List<String> echo(String echo, List<String> names) {
		List<String> list = new ArrayList<String>();
		for (String name : names) {
			list.add(echo + " " + name);
		}
		return list;
	}

	@Web(value = "base64", requestEncrypt = EncryptType.BASE64, responseEncrypt = EncryptType.BASE64)
	public List<String> base64(String echo, List<String> names) {
		List<String> list = new ArrayList<String>();
		for (String name : names) {
			list.add(echo + " " + name);
		}
		return list;
	}

	@Web(value = "upload", requestEncrypt = EncryptType.BASE64)
	@Upload
	public String upload(String name, int age) throws FileNotFoundException, IOException {
		List<UploadFile> files = UploadFileHolder.getFiles();
		for (UploadFile f : files) {
			System.out.println(f.getName());
			File file = new File("d:\\" + f.getName());
			FileOutputStream out = new FileOutputStream(file);
			IOUtils.copy(f.getInputStream(), out);
			out.flush();
			out.close();
		}
		return "姓名:" + name + ",年龄:" + age;
	}

	@Web(value = "aes_base64", requestEncrypt = EncryptType.AES_BASE64, responseEncrypt = EncryptType.AES_BASE64)
	public List<String> aes_base64(String echo, List<String> names) {
		List<String> list = new ArrayList<String>();
		for (String name : names) {
			list.add(echo + " " + name);
		}
		return list;
	}

	@Web(requestEncrypt = EncryptType.AES_BASE64, responseEncrypt = EncryptType.AES_BASE64, sign = true)
	public String aes_sign(String name) {
		return "hello " + name;
	}

	@Web(value = "plain_sign", sign = true)
	public String plain_sign(String name) {
		return "hello " + name;
	}

	@Web(requestEncrypt = EncryptType.AES_BASE64, responseEncrypt = EncryptType.AES_BASE64)
	public String bizError() {
		BizException.throwException(12345, "业务异常");
		return "";
	}

}
