package org.test.web.demo;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.yx.annotation.Bean;
import org.yx.annotation.http.Web;
import org.yx.http.MessageType;
import org.yx.http.user.WebSessions;

@Bean
public class AesTestServer {

	@Web(value = "aes_base64", requestType = MessageType.ENCRYPT_BASE64, responseType = MessageType.ENCRYPT_BASE64)
	public List<String> aes_base64(String echo, List<String> names) {
		Assert.assertEquals("admin", WebSessions.getUserObject(DemoSessionObject.class).getUserId());
		List<String> list = new ArrayList<>();
		for (String name : names) {
			list.add(echo + " " + name);
		}
		return list;
	}

	@Web(requestType = MessageType.ENCRYPT_BASE64, responseType = MessageType.ENCRYPT_BASE64, sign = true)
	public String aes_sign(String name) {
		return "hello " + name;
	}

}
