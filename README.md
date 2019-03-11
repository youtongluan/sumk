# sumk
&emsp;&emsp;sumk的定位是为互联网公司提供一个**开发速度快**、又能很容易进行**横向扩展**的**高性能**框架。
同时具备接口交互（RPC和HTTP）、数据缓存、读写分离、负载均衡、故障转移、异常处理等功能。**一站式**解决互联网公司面临的常见问题。<BR>
&emsp;&emsp;具体的技术实现上，sumk拥有一套类似于传统"SSH"的体系。
引入sumk以及它的依赖包，再加入一些特定注解，就能将一个普通的项目，转化成web或微服务项目（内置jetty，类似于tomcat。从1.7.0开始，支持tomcat7及以上版本，支持所有servlet3.0的监听器）。
[点此查看http应用的搭建](https://github.com/youtongluan/sumk-http-demo)，也可以查看访问[https://www.oschina.net/p/sumk](https://www.oschina.net/p/sumk)查看更多细节<BR>

### 引入sumk.jar
```
<dependency>
    <groupId>com.github.youtongluan</groupId>
    <artifactId>sumk</artifactId>
    <version>2.1.0</version>
</dependency>
```

### 现有主要模块
* **sumk-core**：这个模块类似于spring-core，它的核心是IOC。除此之外，它还有许多彩蛋等待你去挖掘<br>
	* **sumk-tx**：类似于spring-tx，使用@Box来开启事务，连接的开关，以及事务的提交、回滚对开发人员透明。sumk-tx支持异构数据源、读写分离等互联网经常遇到的场景。<br>
		* **sumk-orm**：类似于hibernate。最大特点是跟redis缓存结合在一起，在查询数据的时候，会根据情况优先从redis查询数据，没查到的数据再从数据库查询，然后将数据组装在一起。DB类目前只支持mysql，但RawDB、NamedDB以及sumk-tx都与数据库类型无关。sumk-orm的入口类是DB。此外还提供RawDB和NamedDB两个类，作为DB的补充。<br>
		* **sumk-batis**：本功能是可选的。类似于spring-mybatis.jar，提供对mybatis的兼容，因为sumk-orm本身有些类似hibernate，所以没有内置对hibernate的兼容<br>
	* **sumk-http**：json版的spring mvc，主要面向于服务器端（提供给移动端访问）。只需要在一个普通方法上添加`@Web`注解，就可以让它提供http访问，内置了异常处理、加解密等。支持单机部署或分布式部署（session可存放在本机或redis上）。其中String和byte[]类型的返回值，会将原始信息返回回去，类似于@ResponseBody<br>
	* **sumk-rpc**：提供微服务能力。只需要配置zookeeper地址，然后在方法上添加`@Soa`注解，就能够将一个普通方法变成一个微服务方法。拥有故障转移、动态调整权重、备机等功能<BR>
上述的层级代表了他们的依赖关系。因为模块化的关系，我们可以只用其中的某一部分功能。

<BR>

### sumk-core与传统Spring框架的对应关系<BR>
我将列出sumk-core中元素与Spirng的对应关系，让大家更易入手<br><BR>

|sumk的元素 |spring的元素                        |作用                                                  |
|----------|-------------------------|--------------------------------|
|@Bean     |@Component               |声明一个bean                    |
|@Inject   |@Autowired               |注入                                                  |
|@Box      |@Transactional           |数据库事务                                        |
|IOCWatcher接口|spirng的Aware系列接口 |接收IOC框架回调                                |
|IOC.get() |SpringContextUtil.getBean()|在框架外部获得sumk/spring的bean  |
<BR>

### 环境搭建
* 安装JDK8、eclipse及maven
* 搭建mysql数据库，执行根目录下的test.sql（创建用于测试的数据库）。mysql数据库的用户名、密码配置在app.properties中
* 安装redis服务器（可选），如果有redis服务器，就配置在app.properties中
* zookeeper服务器（可选），目前只有RPC功能有用到zookeeper。`org.test.Main`启动的时候，会启动测试环境内置的zookeeper，这样做的目的是便于新手入门

### 执行测试用例
* 将sumk作为web或soa框架使用
	* [org.test.Main](https://github.com/youtongluan/sumk/blob/master/src/test/java/org/test/Main.java)启动服务器，同时提供RPC和WEB访问的能力
	* [org.test.web.client.HttpTest](https://github.com/youtongluan/sumk/blob/master/src/test/java/org/test/web/client/HttpTest.java) 使用HttpClient模拟web浏览器的行为。包括用户登陆、加解密等。被调用的服务器端代码在org.test.web.demo包底下
	* [org.test.soa.client.RpcTest](https://github.com/youtongluan/sumk/blob/master/src/test/java/org/test/soa/client/RpcTest.java) RPC客户端的使用例子，2种方式的客户端调用都有。被调用的服务器端代码在`org.test.soa.demo`包底下
	
* 单独使用sumkDB（也可以只使用sumkDB中的事务管理，连ORM都不用）的测试用例在[org.test.orm](https://github.com/youtongluan/sumk/tree/master/src/test/java/org/test/orm)包底下。真正的代码在[org.yx.orm](https://github.com/youtongluan/sumk/tree/master/src/test/java/org/yx/orm)


### 示例代码

#### sumk的事务及ORM

```
	@Box  //@Box表示启用sumkDB的事务管理，类似于spring的@Transaction
	public void test() {
		DemoUser user = new DemoUser();
		user.setAge(30);
		user.setName("张三");
		user.setLastUpdate(new Date());
		DB.insert(user).execute(); //插入对象
		//多条件查询
		List<DemoUser> list=DB.select().tableClass(DemoUser.class)
				.lessThan("lastupdate", new Date())
				.orderByAsc("lastupdate")
				.offset(10)
				.limit(10)
				.queryList();
				
	}
	
```

RawDB和NamedDB是DB类的补充，以下是简单例子：<BR>

```
	@Box  //@Box表示启用sumkDB的事务管理，类似于spring的@Transaction
	public void query() {
		// RawDB使用的是原生的sql，后面跟的是要注入的参数。参数个数与sql中的?的个数一致
		RawDB.list("select * from demouser where name=? and age=?", "登陆",12);
		
		//NamedDB类似于mybatis，目前只支持#{}方式，不支持<if>等标签。
		NamedDB.count("select count(1) from demouser where name=#{name}", SBuilder.map("name", "登陆").toMap()); 
		}
```


#### http服务器端：

```java
public class Demo {
	
	@Web   //只需要这个注解就表示可以接受http请求
	public List<String> echo(String echo,List<String> names){
		List<String> list=new ArrayList<String>();
		for(String name:names){
			list.add(echo+" "+name);
		}
		return list;
	}
}
```
* 客户端就是一般的http请求，请求路径是http://localhost/rest/echo，请求实体是data={"echo":"hi","names":["张三","李四"]}<br>

#### RPC服务器端：

```java
@Soa //只需要这个注解，就能接收RPC请求，默认接口名是 [groupId.appId.方法名]
public List<String> echo(String echo,List<String> names){
	List<String> list=new ArrayList<String>();
	for(String name:names){
		list.add(echo+" "+name);
	}
	return list;
}
```

#### RPC客户端：

```Java
Rpc.init();// 如果在app.preperties中添加soa.client.start=true，就可以不用这行代码

List<String> names=Arrays.asList("游夏","游侠");
String echo=",how are you";
//返回是json格式。key的格式是包名的最后一个单词+类名+方法名
String result=Rpc.call("groupId.appId.echo", echo,names);
```

<br>

### 作者心声
雁过留声，人过留名，我也想在职业生涯结束后，留下点什么。所以 ***我会长期维护sumk的***，虽然进度不一定能保障。<BR>

作者：游夏，QQ：3205207767
