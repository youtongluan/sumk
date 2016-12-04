#sumk
sumk的定位是为互联网公司提供一个快速开发、接口交互（RPC和HTTP）、数据缓存、读写分离、负载均衡、故障转移的框架。一站式解决互联网公司面临的常见问题。
除故障转移外，其它5个特性都已有体现。具体的技术实现上，sumk拥有一套类似于"SSH"（spring的IOC、hibernate的ORM以及spring mvc/struts的接口访问能力）的体系。引入sumk以及它的依赖包，再加入一些特定注解，就能将一个普通的项目，转化成web或微服务项目（内置jetty，类似于tomcat）。<BR>

###现有主要功能
* IOC模块，它是sumk的核心，无论是将sumk作为框架使用，还是作为一个普通的jar使用，都需要要初始化IOC模块。初始化方式是`Bootstrap.main(new String[0])`。它拥有类似于spring的注解扫描，反向注入，接口回调等特性。<br>
* DB模块，包括ORM、事务管理、数据缓存等。sumkDB（sumk的DB模块）是sumk的最大亮点。与hibernate或mybatis相比，sumkDB有自己的优势：支持多数据源、支持读写分离、能够智能地使用或刷新redis缓存、开发速度快。尤其是与redis的结合，它是sumkDB最大的卖点。<br>
	* sumkDB的事务管理：只要在方法上加入`@Box`注解，就表示开启一个新的事务，支持嵌套事务以及独立事务
	* sumkDB的ORM：入口是DB类，比如插入`DB.insert(obj).excute()`。它是jquery风格，提供许多设置参数，整个过程不需要编写sql。sumkDB的ORM只是实现了常用的操作，其它的数据库操作，要通过mybatis来补充（暂不支持hibernate）。目前只支持mysql，其它数据库未测试
	* sumkDB与后面介绍的HTTP、微服务是独立的，它可以在HTTP或微服务中使用，也可以在定时任务等其它场景中使用
* 接口层之HTTP：只需要在一个普通方法上添加`@Web`注解，就可以将它变成web项目的servlet。以json协议进行交互，预定义了业务出错的提示方式、登陆方式、鉴权方式、加解密方式等<br>
* 接口层之微服务（RPC）：只需要设置zk路径，然后在方法上添加`@Soa`注解，就能够将一个普通方法变成一个微服务方法<BR>
* 除IOC框架外，sumkDB的事务管理、sumkDB的ORM、HTTP模块、RPC模块相互之间是独立的，可以单独使用。但一起使用效果更好。
* 除上述四大模块外，还有一些小功能，比如redis。它提供了对官方redis驱动的封装。有连接管理、失败重试等功能。未来还会有主从检测等功能。<br>
* 因为配置简单、接口定义简单、异常处理简单、sql语句减少。即使不考虑缓存等卖点，单就开发速度而言，也比其它框架来得快
<br>

###环境搭建
* 搭建mysql数据库，执行根目录下的test.sql（创建用于测试的数据库）。mysql数据库的用户名、密码配置在test/resources/db/test/db.ini中
* 安装redis服务器（可选），如果有redis服务器，就将redis.properties的注释打开
* zookeeper服务器（可选），目前只有RPC功能有用到zookeeper。`org.test.Main`启动的时候，会启动测试环境内置的zookeeper，这样做的目的是便于新手入门

###执行测试用例
* 将sumk作为web或soa框架使用
	* `org.test.Main`启动服务器，同时提供RPC和WEB访问的能力
	* `org.test.web.client.HttpTest` 使用HttpClient模拟web浏览器的行为。包括用户登陆、加解密等。被调用的服务器端代码在org.test.web.demo包底下
	* `org.test.soa.client.RpcTest` RPC客户端的使用例子，2种方式的客户端调用都有。被调用的服务器端代码在`org.test.soa.demo`包底下
	
* 单独使用sumkDB（也可以只使用sumkDB中的事务管理，而不是用ORM）的测试用例在org.test.orm包底下。尤其是`SinglePrimaryTest.select()`，里面有很多查询的示例


###示例代码

####sumkDB

```
	@Box(dbName = "test")  //@Box表示启用sumkDB的事务管理，类似于spring的@Transaction
	public void select() {
		list=DB.select(obj).queryList(); //插入对象
		//查询
		list=DB.select().tableClass(DemoUser.class)
				.lessThan("lastupdate", new Date())
				.OrderByAsc("lastupdate")
				.offset(10)
				.limit(10)
				.resultHandler(MapResultHandler.handler)
				.queryList();
		
	}
```

####http服务器端：

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
* 客户端就是一般的http请求，请求路径是http://localhost/intf/webserver/demo?act=echo，请求实体是{"echo":"hi",“names”:["张三","李四"]}<br>

####RPC服务器端：

```java
@Soa //只需要这个注解，就能接收RPC请求，默认接口名是 appId.小写的类名.小写的方法名
public List<String> echo(String echo,List<String> names){
	List<String> list=new ArrayList<String>();
	for(String name:names){
		list.add(echo+" "+name);
	}
	return list;
}
```

####RPC客户端：

```Java
Rpc.init();
List<String> names=Arrays.asList("游夏","游侠");
String echo=",how are you";
//返回是json格式。key的格式是包名的最后一个单词+类名+方法名
String result=Rpc.call("demo.EchoAction.echo", echo,names);
```

<br>

###作者心声
雁过留声，人过留名，我也想在职业生涯结束后，留下点什么。也许sumk就是那个留下的。所以***我会长期维护的sumk***，虽然进度不一定能保障。<BR>
本项目要特别感谢福建一定火科技有限公司（现已改名）。我在一定火任架构师期间，曾为一定火搭建过一套服务器端框架，在高并发方面表现相当优异。但因为兼容旧代码、工期、当时的技术局限性等原因，有很多地方我觉得可以改进，尤其是它的通用性不强。sumk就是在这种背景下诞生的，但是sumk的体系架构、代码结构等都已经与原来的框架完全不同了。sumk从一开始就是一个全新的项目<BR>
作者：游夏，QQ：3205207767
