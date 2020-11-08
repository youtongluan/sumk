package org.test.web.client;

import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.junit.Assert;
import org.junit.Test;
import org.yx.log.Log;
import org.yx.util.S;

/*
 * 通过这个类，可以了解web的通讯。
 * 加解密部分需要些技术功底才好看明白。
 * 好消息是：现在web应用一般都使用https了，就可以不用加解密方式了，就可以不看加解密部分
 */
public class AesClientTest {

	private String getUrl(String act) {
		String url = "http://localhost:8080/rest/" + act;
		System.out.println("本次请求的url: "+url);
		return url;
	}


	//加密传输的例子。展示了使用表单以及不使用表单两种方式
	@Test
	public void aes_base64() throws Exception {
		String charset = "UTF-8";
		HttpClient client = HttpClientBuilder.create().build();
		HttpResponse resp = login(client);
		String logined = EntityUtils.toString(resp.getEntity());
		String key_str = resp.getFirstHeader("skey").getValue();
		Log.get("login").info("key:{}", key_str);
		byte[] key = Base64.getMimeDecoder().decode(key_str);
		String act = "aes_base64";
		HttpPost post = new HttpPost(getUrl(act));
		Map<String, Object> json = new HashMap<>();
		json.put("echo", "你好!!!");
		json.put("names", Arrays.asList("小明", "小张"));
		byte[] conts = Encrypt.encrypt(S.json().toJson(json).getBytes(charset), key);
		String req = Base64.getEncoder().encodeToString(conts);
		System.out.println("req:" + req);
		post.setHeader(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded");
		StringEntity se = new StringEntity("data=" + URLEncoder.encode(req, "ASCII"), charset);
		post.setEntity(se);
		resp = client.execute(post);
		String line = resp.getStatusLine().toString();
		Log.get( "aes_base64").info(line);
		Assert.assertEquals("HTTP/1.1 200 OK", line);
		HttpEntity resEntity = resp.getEntity();
		String raw = EntityUtils.toString(resEntity);
		Log.get("aes").info("raw resp:{}", raw);
		byte[] contentBytes = Base64.getMimeDecoder().decode(raw);
		String ret = new String(Encrypt.decrypt(contentBytes, key), charset);
		Log.get( "aes_base64").info("服务器返回：" + ret);
		Assert.assertEquals("[\"你好!!! 小明\",\"你好!!! 小张\"]", ret);

		/*
		 * 非表单MIME的方式提交,去掉application/x-www-form-urlencoded，内容也不要URLEncoder编码
		 */
		post = new HttpPost(getUrl(act));
		System.out.println("req:" + req);
		se = new StringEntity("data=" + req, charset);
		post.setEntity(se);
		resp = client.execute(post);
		line = resp.getStatusLine().toString();
		Log.get( "aes_base64").info(line);
		Assert.assertEquals("HTTP/1.1 200 OK", line);
		resEntity = resp.getEntity();
		raw = EntityUtils.toString(resEntity);
		Log.get("aes").info("raw resp:{}", raw);
		contentBytes = Base64.getMimeDecoder().decode(raw);
		ret = new String(Encrypt.decrypt(contentBytes, key), charset);
		Log.get( "aes_base64").info("服务器返回：" + ret);
		Assert.assertEquals("[\"你好!!! 小明\",\"你好!!! 小张\"]", ret);

		post = new HttpPost(getUrl("bizError"));
		resp = client.execute(post);
		Assert.assertEquals(550, resp.getStatusLine().getStatusCode());
		resEntity = resp.getEntity();
		String errMsg = EntityUtils.toString(resEntity);
		Log.get("aes").info("raw resp:{}", errMsg);
		Assert.assertEquals("{\"code\":12345,\"message\":\"业务异常\"}", errMsg);
	}

	//测试加密传输并且签名
	@Test
	public void aes_sign() throws Exception {
		String charset = "UTF-8";
		String act = "aes_sign";
		
		//登陆，并获取加密用的key[]
		HttpClient client = HttpClientBuilder.create().build();
		HttpResponse resp = login(client);
		String logined = EntityUtils.toString(resp.getEntity());
		System.out.println("logined:"+logined);
		String key_str = resp.getFirstHeader("skey").getValue();
		Log.get("login").info("key:{}", key_str);
		byte[] key = Base64.getMimeDecoder().decode(key_str);
		
		
		//加密数据
		Map<String, Object> map = new HashMap<>();
		map.put("name", "小明");
		byte[] conts = Encrypt.encrypt(S.json().toJson(map).getBytes(charset), key);//用AES加密
		String req = Base64.getEncoder().encodeToString(conts);//变成base64
		StringEntity se = new StringEntity(req, charset);
		Log.get("aes_sign").info("req:" + req);
		
		//生成sign，这个是需要sign的接口特有的
		String sign = Encrypt.sign(S.json().toJson(map).getBytes(charset));
		System.out.println("sign:" + sign);
		
		HttpPost post = new HttpPost(getUrl(act) + "?sign=" + sign);
		post.setEntity(se);
		resp = client.execute(post);
		String line = resp.getStatusLine().toString();
		Log.get( "aes_sign").info(line);

		HttpEntity resEntity = resp.getEntity();
		String raw = EntityUtils.toString(resEntity);
		Log.get("aes").info("raw resp:{}", raw);
		byte[] contentBytes = Base64.getMimeDecoder().decode(raw);
		String ret = new String(Encrypt.decrypt(contentBytes, key), charset);
		Log.get( "aes_base64").info("服务器返回：" + ret);
		Assert.assertEquals("hello 小明", ret);
	}


	private HttpResponse login(HttpClient client) throws Exception {
		HttpGet post = new HttpGet("http://localhost:8080/login?username=admin&password=123456&code=9999");
		HttpResponse resp = client.execute(post);
		String line = resp.getStatusLine().toString();
		Assert.assertTrue(line, resp.getStatusLine().getStatusCode() == 200);
		return resp;
	}

	
}
