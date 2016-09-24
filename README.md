#sumk
本项目的目标是为中小互联网公司提供一个封装接口交互（RPC和HTTP）、数据缓存、读写分离、负载均衡、故障转移的框架。一站式解决这些互联网公司都面临的难题。<br>

###现有功能
* HTTP方式的接口调用，用于客户端调用。包括APP、web浏览器等。<br>
* 用于微服务的RPC调用，提供服务器间通信能力。开发人员不需要考虑与手机端的交互细节、安全性，以及session维护等<br>
* 无论RPC还是HTTP，都可以通过注解的方式，配置数据库访问。系统会自动开闭连接，提交事务，或异常回滚。<br>
* 轻量级IOC框架，比spring更轻，更快，配置更简单（但是spring大家更熟悉,且功能更丰富）。只需要@Inject就可以自动注入，也可以使用IOC.get(..)在代码中直接获取。在基本IOC上，还扩展了一些特殊注解：@Cached表示使用缓存的DAO,@Login表示是登陆接口，@Web和@Soa也被认为是特殊的IOC注解<BR>
* 数据库连接的管理与事务支持，防止连接泄漏。并在数据库上加了一层redis缓存层，提高应用的吞吐能力<BR>
* 异常体系，开发人员直接抛出异常就行，不需要考虑异常处理。<br>
* 对redis的封装。提供了redis连接的管理以及重试机制。<br>
<br>

###测试用例说明
1. org.test.web底下是http的测试用例，TestServer启动服务器端，系统内置jetty，所以不需要tomcat就能启动。client包是用java模拟http请求，其中的文件上传和数据库操作需要文件和数据库的配合，其他的直接就能执行。
* org.test.soa是微服务的测试用例。SOAServer是启动soa的服务器端，ClientTest是客户端的使用例子。<br>
* 数据库的使用，参考SimpleDBTest测试用例，或者http测试用例中DBDemo。数据库的配置在db.ini文件中，支持读写分离（写库有多少个，取决于数据库集群的搭建方式，主库可以是只写，也可以既写又读）。<br>
* redis的使用，参见SingleTest类。开发人员不需要考虑redis的创建与销毁，也不需要担心连接池被多次创建。RedisPool.get()第一次使用的时候，会自动创建。创建之后，可以把Redis对象保存起来，也可以每次都调用get。支持使用不同的db，或者使用多个redis实例。<br>

###HTTP工程搭建步骤

* 在工程的resources底下添加app.properties文件，必须的key只有http，它表示action所在的包名。
* 在http所配置的包底下创建Action类（类名任意，不强制继承任何接口。但方法名不能重名，也就是不支持方法overload和override）。在方法上加@Web注解，如果需要上传文件，还要添加@Upload注解<br>
示例（服务端参见com.test.web.demo包，测试用例参见com.test.client.HttpTest)：<br>
服务器端添加接口（无需其它额外配置）：<br>
```java
public class Demo {
	
	@Web(value="echo")
	public List<String> echo(String echo,List<String> names){
		List<String> list=new ArrayList<String>();
		for(String name:names){
			list.add(echo+" "+name);
		}
		return list;
	}
}
```
* 客户端就是一般的http请求，请求路径是http://localhost/webserver?act=echo，请求实体是{"echo":"hi",“names”:["张三","李四"]}<br>

###RPC工程搭建步骤：
* 在工程的resources底下添加app.properties文件，必须的key只有zkurl和soa，zkurl是zookeeper的地址，soa是RPC接口所放置的位置。本测试用例内置了zookeeper服务器，可以直接运行
* 服务器端：只需要添加@SOA注解就行，方法名不能重名<BR>

```java
@SOA
public List<String> echo(String echo,List<String> names){
	List<String> list=new ArrayList<String>();
	for(String name:names){
		list.add(echo+" "+name);
	}
	return list;
}
```
* 客户端：<BR>
```Java
Client.init();
List<String> names=new ArrayList<String>();
names.add("游侠");
names.add("BOSS");
String echo=",how are you";
//ret是json格式。key的格式是包名的最后一个单词+类名+方法名
String ret=Client.call("demo.EchoAction.echo", echo,names);
```

<br>
问题反馈：<br>
如果使用过程中遇到问题，或者需要某种扩展，请联系QQ：3205207767，尤其是福州地区的朋友。
