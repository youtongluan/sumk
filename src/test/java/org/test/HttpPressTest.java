/**
 * Copyright (C) 2016 - 2017 youtongluan.
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
package org.test;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.junit.Assert;
import org.junit.Test;
import org.yx.util.S;

public class HttpPressTest {
	@Test
	public void test() throws IOException, InterruptedException {
		String charset = "GBK";
		HttpClient client = HttpClientBuilder.create().setMaxConnTotal(500).setMaxConnPerRoute(200).build();
		ExecutorService executor=Executors.newFixedThreadPool(100);
		HttpPost post = new HttpPost("http://localhost:8080/rest/echo");
		Map<String, Object> json = new HashMap<>();
		json.put("echo", "你好!!!");
		json.put("names", Arrays.asList("小明", "小张"));
		StringEntity se = new StringEntity(S.json.toJson(json), charset);
		post.setEntity(se);
		System.out.println("开始压测，请耐心等待10秒左右。。。");
		long begin=System.currentTimeMillis();
		int count=100000;
		for(int i=0;i<count;i++){
			executor.execute(()->{
				try {
					HttpResponse resp = client.execute(post);
					HttpEntity resEntity = resp.getEntity();
					String ret = EntityUtils.toString(resEntity, charset);
					Assert.assertEquals("[\"你好!!! 小明\",\"你好!!! 小张\"]", ret);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			});
		}
		executor.shutdown();
		executor.awaitTermination(1, TimeUnit.DAYS);
		long time=System.currentTimeMillis()-begin;
		System.out.println(count+"次http请求总耗时:"+time+"ms,平均每秒请求数:"+(count*1000d/time));
	}
}
