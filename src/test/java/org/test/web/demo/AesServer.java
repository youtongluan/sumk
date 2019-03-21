package org.test.web.demo;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.yx.annotation.Bean;
import org.yx.annotation.http.Web;
import org.yx.http.EncryptType;
import org.yx.http.user.WebSessions;

@Bean
public class AesServer {

	@Web(value = "aes_base64", requestEncrypt = EncryptType.AES_BASE64, responseEncrypt = EncryptType.AES_BASE64)
	public List<String> aes_base64(String echo, List<String> names) {
		Assert.assertEquals("admin", WebSessions.getUserObject(DemoSessionObject.class).getUserId());
		List<String> list = new ArrayList<>();
		for (String name : names) {
			list.add(echo + " " + name);
		}
		return list;
	}

	@Web(requestEncrypt = EncryptType.AES_BASE64, responseEncrypt = EncryptType.AES_BASE64, sign = true)
	public String aes_sign(String name) {
		return "hello " + name;
	}

}
