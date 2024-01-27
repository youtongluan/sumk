package org.test.web.client;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.http.HttpEntity;
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
import org.yx.common.util.S;
import org.yx.demo.member.DemoUser;
import org.yx.log.Log;

/*
 * 通过这个类，可以了解web的通讯。
 */
public class PlainClientTest {

	private String getUrl(String act) {
		return "http://localhost:8080/rest/" + act;
	}

	private String getUploadUrl(String act) {
		return "http://localhost:8080/upload/" + act;
	}

	private HttpResponse login(HttpClient client) throws Exception {
		HttpGet post = new HttpGet("http://localhost:8080/login?username=admin&password=123456&code=9999");
		HttpResponse resp = client.execute(post);
		String line = resp.getStatusLine().toString();
		Assert.assertTrue(line, resp.getStatusLine().getStatusCode() == 200);
		return resp;
	}

	Random r = new Random();
	
	@Test
	public void plain() throws IOException {
		String charset = "GBK";
		HttpClient client = HttpClientBuilder.create().build();
		String act = "echo";
		HttpPost post = new HttpPost(getUrl(act));
		Map<String, Object> json = new HashMap<>();
		json.put("echo", "你好!!!");
		json.put("names", Arrays.asList("小明", "小张"));
		StringEntity se = new StringEntity(S.json().toJson(json), charset);
		post.setEntity(se);
		HttpResponse resp = client.execute(post);
		String line = resp.getStatusLine().toString();
		Assert.assertEquals("HTTP/1.1 200 OK", line);
		HttpEntity resEntity = resp.getEntity();
		String ret = EntityUtils.toString(resEntity, charset);
		Assert.assertEquals("[\"你好!!! 小明\",\"你好!!! 小张\"]", ret);
	}

	//测试需要登录的情况
	@Test
	public void login_sign() throws Exception {
		String charset = "UTF-8";
		HttpClient client = HttpClientBuilder.create().build();
		login(client);
		String act = "plain_sign";
		Map<String, Object> json = new HashMap<>();
		json.put("name", "小明");
		String req = S.json().toJson(json);
		String sign = Encrypt.sign(req.getBytes(charset));
		HttpPost post = new HttpPost(getUrl(act) + "?_sign=" + sign);
		StringEntity se = new StringEntity(req,charset);
		post.setEntity(se);
		HttpResponse resp = client.execute(post);
		
		//以下验证请求是否正确
		String line = resp.getStatusLine().toString();
		Assert.assertEquals("HTTP/1.1 200 OK", line);
		HttpEntity resEntity = resp.getEntity();
		String ret = EntityUtils.toString(resEntity,charset);
		Assert.assertEquals("hello 小明，来自admin的问候", ret);
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
		String req = Base64.getEncoder().encodeToString(S.json().toJson(json).replace("\"names\"", "names").getBytes(charset));
		System.out.println("req:" + req);
		StringEntity se = new StringEntity(req, charset);
		post.setEntity(se);
		HttpResponse resp = client.execute(post);
		String line = resp.getStatusLine().toString();
		Assert.assertEquals("HTTP/1.1 200 OK", line);
		HttpEntity resEntity = resp.getEntity();
		String ret = new String(Base64.getMimeDecoder().decode(EntityUtils.toString(resEntity)), charset);
		Assert.assertEquals("[\"你好!!! 小明\",\"你好!!! 小张\"]", ret);
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
		String req = Base64.getEncoder().encodeToString(S.json().toJson(json).getBytes(charset));
		System.out.println("req:" + req);

		MultipartEntity reqEntity = new MultipartEntity();
		reqEntity.addPart("Api", StringBody.create("common", "text/plain", Charset.forName(charset)));
		reqEntity.addPart("param", StringBody.create(req, "text/plain", Charset.forName(charset)));
		reqEntity.addPart("img", new FileBody(new File("logo_bluce.jpg")));

		post.setEntity(reqEntity);
		HttpResponse resp = client.execute(post);
		String line = resp.getStatusLine().toString();
		Assert.assertEquals("HTTP/1.1 200 OK", line);
		HttpEntity resEntity = resp.getEntity();
		Log.get("upload").info(EntityUtils.toString(resEntity, charset));
	}

	

	@Test
	public void db_insert() throws IOException {
		String charset = "UTF-8";
		HttpClient client = HttpClientBuilder.create().build();
		String act = "add";
		HttpPost post = new HttpPost(getUrl(act));
		List<DemoUser> list = new ArrayList<>();

		for (int i = 0; i < 10; i++) {
			DemoUser obj = new DemoUser();
			obj.setAge(r.nextInt(100));
			obj.setName("名字" + r.nextInt());
			obj.setId(r.nextLong());
			list.add(obj);
		}
		Map<String, Object> json = new HashMap<>();
		json.put("users", list);
		StringEntity se = new StringEntity(S.json().toJson(json), charset);
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
		List<DemoUser> list = new ArrayList<>();

		for (int i = 0; i < 10; i++) {
			DemoUser obj = new DemoUser();
			obj.setAge(r.nextInt(100));
			obj.setName("名字" + r.nextInt());
			obj.setId(r.nextLong());
			list.add(obj);
		}
		Map<String, Object> json = new HashMap<>();
		json.put("users", list);
		StringEntity se = new StringEntity(S.json().toJson(json), charset);
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
