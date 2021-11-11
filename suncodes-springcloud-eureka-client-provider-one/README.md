## Eureka 客户端（服务提供者）

（1）复制 suncodes-springcloud-provider-dept-rest 代码

把对应的 代码复制，主要功能：
- 连接mysql
- 提供增/删/改/查功能

（2）增加有关eureka依赖

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
</dependency>
```

（3）添加有关 yml 配置

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/springcloud?serverTimezone=UTC
    username: root
    password: root
    driver-class-name: com.mysql.cj.jdbc.Driver
mybatis:
  config-location: classpath:mybatis/SqlMapConfig.xml
  type-aliases-package: suncodes.springcloud.api.entities    # 所有Entity别名类所在包
  mapper-locations:
    - classpath:mybatis/mapper/**/*.xml

server:
  port: 8001

#客户端注册进eureka服务列表内
eureka:
  client:
    service-url:
      defaultZone: http://localhost:7001/eureka
```

（4）启动测试

先启动 suncodes-springcloud-eureka-server-one，端口：7001

之后启动 suncodes-springcloud-eureka-client-provider-one，端口：8001


Instances currently registered with Eureka

| Application | AMIs        | Availability Zones | Status                                                       |
| :---------- | :---------- | :----------------- | :----------------------------------------------------------- |
| **UNKNOWN** | **n/a** (1) | (1)                | **UP** (1) - [windows10.microdone.cn:8001](http://windows10.microdone.cn:8001/actuator/info) |


（5）微服务注册名配置说明

微服务实例名称：

如果没有明确指定名称，则为 UNKNOWN，否则，为

```yaml
spring:
  application:
    name: provider-7001
```

## Eureka 配置说明

https://blog.csdn.net/cc907566076/article/details/77849149

配置主要分为以下两个方面：

服务注册相关的配置信息，包括服务注册中心的地址，服务获取的间隔时间，可用区域等

服务实例相关的配置信息，包括服务实例的名称，ip地址，端口号，健康检查路径等。

Eureka服务端

更多滴类似于一个现成产品，大多数情况下，我们不需要修改它的配置信息，可查看org.springframework.cloud.netflix.eureka.server.EurekaServerConfigBean类的定义来做进一步学习，这些参数均以eureka.server作为前缀。

服务注册类配置

可以通过查看org.springframework.cloud.netflix.eureka.EurekaClientConfigBean的源码来获得比官方文档中更为详尽的内容，这些配置信息都以eureka.client为前缀。

指定注册中心

主要通过eureka.client.serviceUrl实现
有一组默认值：defaultZone = http://localhost:8761/eureka/

可以配置自己的端口等：
```
eureka.client.serviceUrl.defaultZone = http://localhost:1111/eureka/
```

也可以配置多个，例如注册中心高可用时，服务提供者就可以注册到多个注册中心：
```text
eureka.client.serviceUrl.defaultZone = http://peer1:1111/eureka/,http://peer2:1112/eureka/
```

很多时候我们还会为注册中心加入安全校验，例如：
http://:@localhost:1111/eureka/

此类的构造方法中有默认值：
```java
public EurekaClientConfigBean()
  {
    this.enabled = true;
    this.transport = new CloudEurekaTransportConfig();
    this.registryFetchIntervalSeconds = 30;
    this.instanceInfoReplicationIntervalSeconds = 30;
    this.initialInstanceInfoReplicationIntervalSeconds = 40;
    this.eurekaServiceUrlPollIntervalSeconds = 300;
    this.eurekaServerReadTimeoutSeconds = 8;
    this.eurekaServerConnectTimeoutSeconds = 5;
    this.eurekaServerTotalConnections = 200;
    this.eurekaServerTotalConnectionsPerHost = 50;
    this.region = "us-east-1";
    this.eurekaConnectionIdleTimeoutSeconds = 30;
    this.heartbeatExecutorThreadPoolSize = 2;
    this.heartbeatExecutorExponentialBackOffBound = 10;
    this.cacheRefreshExecutorThreadPoolSize = 2;
    this.cacheRefreshExecutorExponentialBackOffBound = 10;
    this.serviceUrl = new HashMap();
    this.serviceUrl.put("defaultZone", "http://localhost:8761/eureka/");
    this.gZipContent = true;
    this.useDnsForFetchingServiceUrls = false;
    this.registerWithEureka = true;
    this.preferSameZoneEureka = true;
    this.availabilityZones = new HashMap();
    this.filterOnlyUpInstances = true;
    this.fetchRegistry = true;
    this.dollarReplacement = "_-";
    this.escapeCharReplacement = "__";
    this.allowRedirects = false;
    this.onDemandUpdateStatusChange = true;
    this.clientDataAccept = EurekaAccept.full.name();
  }
```


服务实例类配置

可以通过查看org.springframework.cloud.netflix.eureka.EurekaInstanceConfigBean的源码，这些信息都以eureka.instance为前缀


元数据

元数据：Eureka客户端在向服务注册中心发送注册请求时，用来描述自身服务信息的对象，其中包含了一些标准化的元数据，比如服务名称，实例名称，实例IP，实例端口等，用于服务治理的重要信息，以及一些用于负载均衡策略或是其他特殊用途的自定义数据信息。

在使用spring cloud Eureka的时候，所有配置的信息都通过org.springframework.cloud.netflix.eureka.EurekaInstanceConfigBean进行加载，真正进行服务注册的时候，还是会包装成com.netflix.appinfo.InstanceInfo对象发送给Eureka服务端。

我们可以通过eureka.instance.=的格式对标准化元数据直接进行配置，其中就是EurekaInstanceConfigBean对象中成员变量名，对于自定义元数据，可以通过eureka.instance.metadataMap.=的格式来进行配置
例如eureka.instance.metadataMap.ccName=cc


实例名配置

即InstanceInfo中的instanceId参数，区分同一服务中不同实例的唯一标识。在Netflix Eureka的原生实现中，实例名采用主机名作为默认值，这样的设置使得同一主机上无法启动多个相同的服务实例。所以在springcloudEureka的配置中，针对同一主机中启动多实例的情况，对实例名的默认命名做了更为合理的扩展，采用如下默认规则：

> ${spring.cloud.client.hostname}:${spring.application.name}:${spring.application.instance_id}:${server.port}

当时我们是通过传递不同的server.port参数来保证启动多个实例的，也可以通过设置server.port=0或者server.port=random.int[10000,19999]来让tomcat启动的时候采用随机端口。但是此时会发现只有一个服务实例能够正常服务，此时我们就可以通过设置实例名规则来轻松解决：Eureka.instance.instanceId=random.int[10000,19999]来让tomcat启动的时候采用随机端口。但是此时会发现只有一个服务实例能够正常服务，此时我们就可以通过设置实例名规则来轻松解决：

> Eureka.instance.instanceId={spring.application.name}:${random.int},

这样在同一台主机上，不指定端口就能轻松启动多个实例的效果。


端点配置

InstanceInfo中，可以看到一些URL的配置信息。比如homePageUrl,statusPageUrl,healthCheckUrl,分别代表了应用主页Url,状态也Url以及健康检查的URL。其中状态页和健康检查的URL在spring cloud eureka中默认使用了spring-boot-acturator模块提供的/info端点和/health端点。

为了服务的正常运作，必须保证客户端的/health端点在发送元数据的时候，是一个能够被注册中心访问到的地址，否则服务注册中心不会根据应用的健康检查来更改状态（仅当开启了healthcheck功能时，以该端点信息作为健康检查的标准）。而/info端点如果不正确的话，会导致在Eureka面板中单击服务实例，无法访问到服务实例提供的信息接口。

一般不需要修改这几个URL的配置。

1.为应用设置了context-path,这时，所有spring-boot-actuator模块的监控端点都会增加一个前缀，所以相应的eureka的这些url端点也要增加前缀：
```text
management.context-path=/client
eureka.instance.statusPageUrlPath=${management.context-path}/info
eureka.instance.healthCheckUrlPath=${management.context-path}/health
```
2.有时为了安全考虑，可能会修改/info和/health端点的原始路径
```text
endpoints.info.path=/appInfo
endpoints.health.path=/checkHealth

eureka.instance.statusPageUrlPath=/${endpoints.info.path}
eureka.instance.healthCheckUrlPath=/${endpoints.health.path}
```
以上都是用相对路径来配置的，由于eureka的服务注册中心默认以HTTP的方式访问和暴露这些端点，如果客户端应用以HTTPS的方式暴露服务和监控端点的话，spring cloud eureka还提供了绝对路径的配置参数：
```text
eureka.instance.statusPageUrlPath=https://${eureka.instance.hostname}/info
eureka.instance.healthCheckUrlPath=https://${eureka.instance.hostname}/health
eureka.instance.homePageUrl=https://${eureka.instance.hostname}/
```


健康监测

默认情况下，eureka中各个实例的健康监测并不是通过spring-boot-actuator模块的/health端点来实现的，而是依靠客户端心跳的方式来保持服务实例的存活。

默认的心跳实现方式可以有效检查客户端是否正常运作，但是无法保证客户端应用能够正常提供服务，比如数据库等，如果无法联通，实际已经无法正常对外服务了，但是心跳依然进行，所以服务消费者还可以调用，这样的调用实际不能获得预期的结果。

可以通过简单的配置，吧eureka客户端的健康监测交给spring-boot-actuator模块的/health端点，以实现更加全面的健康状态维护

Pom文件：
```xml
<!-- 监控 -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

application.properties:
```properties
# let the spring-boot-actuator works for the health ,not the default heart in eureka
eureka.client.healthcheck.enabled=true
```

如果有之前端点配置中说的一些特殊处理，再配置一下即可

eureka.instance.hostname：主机名，不配置的时候将根据操作系统的主机名来获取。


## actuator与注册微服务信息完善

### instance.instance-id：实例名称

```yaml
#客户端注册进eureka服务列表内
eureka:
  client:
    service-url:
      defaultZone: http://localhost:7001/eureka
  # 实例名称
  instance:
    instance-id: instance-provider-7001

```


### 访问信息有IP信息提示

```yaml
#客户端注册进eureka服务列表内
eureka:
  client:
    service-url:
      defaultZone: http://localhost:7001/eureka
  # 实例名称
  instance:
    instance-id: instance-provider-7001
    ## 显示IP
    prefer-ip-address: true
```

### 微服务info内容详细信息

```xml
<dependency>
   <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```




