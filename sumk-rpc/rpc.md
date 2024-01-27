## 客户端

### 建立连接

1. Rpc.init()初始化Transport

2. 请求某个地址的时候，视情况创建TransportClient

3. TransportClient创建TransportChannel而TransportChannel实例创建之前，会创建mina的session或netty的channel。并且将setAttribute(Client.getName()，Client)，setAttribute(TransportChannel.getName()，channel)。这样session就跟channel和client关联起来

### 断开连接

* 外部通过TransportClient来关闭。

* ClientHandler中只是通过Channel来关闭就行了。因为框架有定时监测idle的功能，所以被意外关闭的无需在意，让定时器来监测就好
* 各个实现类要设法监听inputClose方法，它去close session

   

## 服务端

### 接收连接

1. SoaPlugin初始化Transport
2.Transport初始化TransportServer

2. Handler第一次接收session数据的时候，创建TransportChannel，并且setAttribute(TransportChannel.getName()，channel)


### 断开连接

* server只有idle、异常会主动断开连接

* 各个实现类要设法监听inputClose方法，它去close session