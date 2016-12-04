package org.test.web.client;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.junit.Assert;
import org.junit.Test;
import org.yx.demo.member.DemoUser;
import org.yx.http.filter.Session;
import org.yx.log.Log;
import org.yx.util.GsonUtil;
import org.yx.util.secury.EncryUtil;
import org.yx.util.secury.MD5Utils;

public class HttpTest {

	private String getUrl(String act) {
		return "http://localhost:8080/intf/webserver/demo?act=" + act;
	}

	private String getUploadUrl(String act) {
		return "http://localhost:8080/intf/upload/demo?act=" + act;
	}

	@Test
	public void plain() throws IOException {
		String charset = "GBK";
		HttpClient client = HttpClientBuilder.create().build();
		String act = "echo";
		HttpPost post = new HttpPost(getUrl(act));
		Map<String, Object> json = new HashMap<>();
		json.put("echo", "你好!!!");
		json.put("names", Arrays.asList("小明", "小张"));
		StringEntity se = new StringEntity(GsonUtil.toJson(json), charset);
		post.setEntity(se);
		HttpResponse resp = client.execute(post);
		String line = resp.getStatusLine().toString();
		Assert.assertEquals("HTTP/1.1 200 OK", line);
		HttpEntity resEntity = resp.getEntity();
		String ret = EntityUtils.toString(resEntity, charset);
		Assert.assertEquals("[\"你好!!! 小明\",\"你好!!! 小张\"]", ret);
	}

	@Test
	public void plain_sign() throws Exception {
		String charset = "GBK";
		HttpClient client = HttpClientBuilder.create().build();
		String act = "plain_sign";
		Map<String, Object> json = new HashMap<>();
		json.put("name", "小明");
		String req = GsonUtil.toJson(json);
		String sign = MD5Utils.encrypt(req.getBytes(charset));
		HttpPost post = new HttpPost(getUrl(act) + "&sign=" + sign);
		StringEntity se = new StringEntity(req, charset);
		post.setEntity(se);
		HttpResponse resp = client.execute(post);
		String line = resp.getStatusLine().toString();
		Assert.assertEquals("HTTP/1.1 200 OK", line);
		HttpEntity resEntity = resp.getEntity();
		String ret = EntityUtils.toString(resEntity, charset);
		Assert.assertEquals("hello 小明", ret);
	}

	@Test
	public void base64() throws IOException {
		String charset = "UTF-8";
		HttpClient client = HttpClientBuilder.create().build();
		String act = "base64";
		HttpPost post = new HttpPost(getUrl(act));
		Map<String, Object> json = new HashMap<>();
		json.put("echo", "你好!!!");
		json.put("names", Arrays.asList("小明", "小张"));
		String req = Base64.encodeBase64String(GsonUtil.toJson(json).replace("\"names\"", "names").getBytes(charset));
		System.out.println("req:" + req);
		StringEntity se = new StringEntity(req, charset);
		post.setEntity(se);
		HttpResponse resp = client.execute(post);
		String line = resp.getStatusLine().toString();
		Assert.assertEquals("HTTP/1.1 200 OK", line);
		HttpEntity resEntity = resp.getEntity();
		String ret = new String(Base64.decodeBase64(EntityUtils.toString(resEntity)), charset);
		Assert.assertEquals("[\"你好!!! 小明\",\"你好!!! 小张\"]", ret);
	}

	@Test
	public void aes_base64() throws Exception {
		String charset = "UTF-8";
		HttpResponse resp = login();
		String sessionId = resp.getFirstHeader(Session.SESSIONID).getValue();
		Log.get("login").info("sessionId:{}", sessionId);
		String logined = EntityUtils.toString(resp.getEntity());
		int ln = logined.indexOf("\t\n");
		String key_str = logined.substring(0, ln);
		Log.get("login").info("key:{}", key_str);
		byte[] key = Base64.decodeBase64(key_str);
		HttpClient client = HttpClientBuilder.create().build();
		String act = "aes_base64";
		HttpPost post = new HttpPost(getUrl(act));
		post.setHeader(Session.SESSIONID, sessionId);
		Map<String, Object> json = new HashMap<>();
		json.put("echo", "你好!!!");
		json.put("names", Arrays.asList("小明", "小张"));
		byte[] conts = EncryUtil.encrypt(GsonUtil.toJson(json).getBytes(charset), key);
		String req = Base64.encodeBase64String(conts);
		System.out.println("req:" + req);
		post.setHeader(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded");
		StringEntity se = new StringEntity("data=" + URLEncoder.encode(req, "ASCII"), charset);
		post.setEntity(se);
		resp = client.execute(post);
		String line = resp.getStatusLine().toString();
		Log.get(this.getClass(), "aes_base64").info(line);
		Assert.assertEquals("HTTP/1.1 200 OK", line);
		HttpEntity resEntity = resp.getEntity();
		String raw = EntityUtils.toString(resEntity);
		Log.get("aes").info("raw resp:{}", raw);
		byte[] contentBytes = Base64.decodeBase64(raw);
		String ret = new String(EncryUtil.decrypt(contentBytes, key), charset);
		Log.get(this.getClass(), "aes_base64").info("服务器返回：" + ret);
		Assert.assertEquals("[\"你好!!! 小明\",\"你好!!! 小张\"]", ret);

		/*
		 * 非表单MIME的方式提交 去掉application/x-www-form-urlencoded，内容也不要URLEncoder编码
		 */
		post = new HttpPost(getUrl(act));
		post.setHeader(Session.SESSIONID, sessionId);
		System.out.println("req:" + req);
		se = new StringEntity("data=" + req, charset);
		post.setEntity(se);
		resp = client.execute(post);
		line = resp.getStatusLine().toString();
		Log.get(this.getClass(), "aes_base64").info(line);
		Assert.assertEquals("HTTP/1.1 200 OK", line);
		resEntity = resp.getEntity();
		raw = EntityUtils.toString(resEntity);
		Log.get("aes").info("raw resp:{}", raw);
		contentBytes = Base64.decodeBase64(raw);
		ret = new String(EncryUtil.decrypt(contentBytes, key), charset);
		Log.get(this.getClass(), "aes_base64").info("服务器返回：" + ret);
		Assert.assertEquals("[\"你好!!! 小明\",\"你好!!! 小张\"]", ret);

		post = new HttpPost(getUrl("bizError"));
		post.setHeader(Session.SESSIONID, sessionId);
		resp = client.execute(post);
		Assert.assertEquals(499, resp.getStatusLine().getStatusCode());
		resEntity = resp.getEntity();
		String errMsg = EntityUtils.toString(resEntity);
		Log.get("aes").info("raw resp:{}", errMsg);
		Assert.assertEquals("{\"code\":12345,\"message\":\"业务异常\"}", errMsg);
	}

	@Test
	public void aes_sign() throws Exception {
		String charset = "UTF-8";
		HttpResponse resp = login();
		String sessionId = resp.getFirstHeader(Session.SESSIONID).getValue();
		Log.get("login").info("sessionId:{}", sessionId);
		String logined = EntityUtils.toString(resp.getEntity());
		int ln = logined.indexOf("\t\n");
		String key_str = logined.substring(0, ln);
		Log.get("login").info("key:{}", key_str);
		byte[] key = Base64.decodeBase64(key_str);
		HttpClient client = HttpClientBuilder.create().build();
		String act = "aes_sign";
		Map<String, Object> json = new HashMap<>();
		json.put("name", "小明");
		byte[] conts = EncryUtil.encrypt(GsonUtil.toJson(json).getBytes(charset), key);
		String req = Base64.encodeBase64String(conts);
		Log.get("aes_sign").info("req:" + req);
		String sign = MD5Utils.encrypt(GsonUtil.toJson(json).getBytes(charset));
		System.out.println("sign:" + sign);
		HttpPost post = new HttpPost(getUrl(act) + "&sign=" + sign);
		post.setHeader(Session.SESSIONID, sessionId);

		StringEntity se = new StringEntity(req, charset);
		post.setEntity(se);
		resp = client.execute(post);
		String line = resp.getStatusLine().toString();
		Log.get(this.getClass(), "aes_sign").info(line);

		HttpEntity resEntity = resp.getEntity();
		String raw = EntityUtils.toString(resEntity);
		Log.get("aes").info("raw resp:{}", raw);
		byte[] contentBytes = Base64.decodeBase64(raw);
		String ret = new String(EncryUtil.decrypt(contentBytes, key), charset);
		Log.get(this.getClass(), "aes_base64").info("服务器返回：" + ret);
		Assert.assertEquals("hello 小明", ret);
	}

	@Test
	public void upload() throws IOException {
		String charset = "UTF-8";
		HttpClient client = HttpClientBuilder.create().build();
		String act = "upload";
		HttpPost post = new HttpPost(getUploadUrl(act));
		Map<String, Object> json = new HashMap<>();
		json.put("name", "张三");
		json.put("age", 23);
		String req = Base64.encodeBase64String(GsonUtil.toJson(json).getBytes(charset));
		System.out.println("req:" + req);

		MultipartEntity reqEntity = new MultipartEntity();
		reqEntity.addPart("Api", StringBody.create("common", "text/plain", Charset.forName(charset)));
		reqEntity.addPart("data", StringBody.create(req, "text/plain", Charset.forName(charset)));
		reqEntity.addPart("img", new FileBody(new File("E:\\works\\logo.png")));

		post.setEntity(reqEntity);
		HttpResponse resp = client.execute(post);
		String line = resp.getStatusLine().toString();
		Assert.assertEquals("HTTP/1.1 200 OK", line);
		HttpEntity resEntity = resp.getEntity();
		Log.get("upload").info(EntityUtils.toString(resEntity, charset));
	}

	private HttpResponse login() throws Exception {
		HttpClient client = HttpClientBuilder.create().build();
		HttpGet post = new HttpGet("http://localhost:8080/intf/login?username=admin&password=123456&code=9999");
		HttpResponse resp = client.execute(post);
		String line = resp.getStatusLine().toString();
		Assert.assertTrue(line, resp.getStatusLine().getStatusCode() == 200);
		return resp;
	}

	Random r = new Random();

	@Test
	public void db_insert() throws IOException {
		String charset = "UTF-8";
		HttpClient client = HttpClientBuilder.create().build();
		String act = "add";
		HttpPost post = new HttpPost(getUrl(act));
		List<DemoUser> list = new ArrayList<DemoUser>();

		for (int i = 0; i < 10; i++) {
			DemoUser obj = new DemoUser();
			obj.setAge(r.nextInt(100));
			obj.setName("名字" + r.nextInt());
			obj.setId(r.nextLong());
			list.add(obj);
		}
		Map<String, Object> json = new HashMap<>();
		json.put("users", list);
		StringEntity se = new StringEntity(GsonUtil.toJson(json), charset);
		post.setEntity(se);
		HttpResponse resp = client.execute(post);
		String line = resp.getStatusLine().toString();
		System.out.println(line);
		HttpEntity resEntity = resp.getEntity();
		String ret = EntityUtils.toString(resEntity, charset);
		System.out.println(ret);
		Assert.assertEquals(list.size() + "", ret);
	}

	@Test
	public void db_insert_query() throws IOException {
		String charset = "UTF-8";
		HttpClient client = HttpClientBuilder.create().build();
		String act = "addAndGet";
		HttpPost post = new HttpPost(getUrl(act));
		List<DemoUser> list = new ArrayList<DemoUser>();

		for (int i = 0; i < 10; i++) {
			DemoUser obj = new DemoUser();
			obj.setAge(r.nextInt(100));
			obj.setName("名字" + r.nextInt());
			obj.setId(r.nextLong());
			list.add(obj);
		}
		Map<String, Object> json = new HashMap<>();
		json.put("users", list);
		StringEntity se = new StringEntity(GsonUtil.toJson(json), charset);
		post.setEntity(se);
		HttpResponse resp = client.execute(post);
		String line = resp.getStatusLine().toString();
		System.out.println(line);
		HttpEntity resEntity = resp.getEntity();
		String ret = EntityUtils.toString(resEntity, charset);
		Log.get("db_insert_query").info("返回结果：" + ret);
		System.out.println(ret);
	}
}
