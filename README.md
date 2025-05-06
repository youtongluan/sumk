# sumk

sumk是为互联网而生的，在性能、分布式、扩展性等方面考虑较多，互联网常见的特性很多都内置支持，比如数据库读写分离、调用链跟踪、统一日志等，与spring体系相比，sumk更轻量、性能更高、内存消耗更低。以下是主要工程介绍，具体功能参见[sumk的功能特性](https://p2nwdvhb36.feishu.cn/docx/LQxXdjwbdoWDrFxcyUTcNWTUnSh)

- sumk-base：配置管理和日志接口

- sumk-framework：核心框架，主要是IOC

- sumk-db：数据库功能，包括事务、ORM等

- sumk-http：mvc组件

- Sumk-rpc：微服务功能

- sumk-rpc-mina：可选组件，如果引入该组件，rpc底层就会使用mina替代netty

- async-logger（又名sumk-log)：slf4j日志的实现，同时支持鹰眼跟踪和统一日志。
  
  

### 功能介绍

[sumk总体介绍](https://p2nwdvhb36.feishu.cn/docx/AEIhdF4M5oDXouxdfNLc0ya2nZb)

[sumk功能特性](https://p2nwdvhb36.feishu.cn/docx/LQxXdjwbdoWDrFxcyUTcNWTUnSh)

### 使用

[sumk框架入门](https://p2nwdvhb36.feishu.cn/docx/AOl0dhDqJoymnSxuWUhcTQ1SnMf)

[注解及接口及工具类](https://p2nwdvhb36.feishu.cn/docx/UuIPduSDuo6kSlxSOEDcGzdPnSh)

[sumk常用配置](https://p2nwdvhb36.feishu.cn/docx/RUBidOGQboZTkaxGJ8uc1Z93n8c)

[sumk-db使用介绍](https://p2nwdvhb36.feishu.cn/docx/TQnUdmM1YomahpxVIKMcxPdYnFc)

[接口文档及状态信息查看](https://p2nwdvhb36.feishu.cn/docx/ZvV3dCbLuog5wfxoSAgcV6frnvc)

### 示例工程

<https://github.com/youtongluan/sumk-server-demo>

### 

### 作者：游夏。QQ：3205207767
