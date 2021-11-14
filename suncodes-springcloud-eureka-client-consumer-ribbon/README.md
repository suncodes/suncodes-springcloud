# Ribbon

## Ribbon配置初步

(1) 配置Pom文件

配置 eureka 依赖，注意：ribbon 在 SpringCloud 2020 版本已经被移除，被 Spring Cloud Load Balancer 代替。

在 SpringCloud 2020 版本中的 spring-cloud-starter-netflix-eureka-client 依赖中，已经集成了 Spring Cloud Load Balancer。

Spring Cloud Load Balancer 目前只支持 轮询策略。

```xml
    <dependencies>
        <!-- 将微服务client侧注册进eureka -->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-devtools</artifactId>
        </dependency>

        <dependency>
            <groupId>suncodes</groupId>
            <artifactId>suncodes-springcloud-api</artifactId>
            <version>1.0-SNAPSHOT</version>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>

        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
        </dependency>
    </dependencies>
```


（2）修改application.yml，追加 eureka 的服务注册地址

```yaml
server:
  port: 80

#客户端注册进eureka服务列表内
eureka:
  client:
    service-url:
      defaultZone: http://${eureka-hostname}:${eureka-port[0]}/eureka/,http://${eureka-hostname}:${eureka-port[1]}/eureka/,http://${eureka-hostname}:${eureka-port[2]}/eureka/
    register-with-eureka: false

eureka-hostname: localhost
eureka-port: [7001,7002,7003]

```

（3）对ConfigBean进行新注解@LoadBalanced，获得Rest时加入Ribbon的配置

```java
@Configuration
public class ConsumerRibbonConfiguration {
    @Bean
    @LoadBalanced
    RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
```

（4）Ribbon和Eureka整合后Consumer可以直接调用服务而不用再关心地址和端口号

直接就可以通过 http://APPLICATION_NAME/地址 进行访问，也不是IP:PORT

```java
@RestController
public class ConsumerRibbonController {

    @Autowired
    private DiscoveryClient client;

    private static String REST_URL_PREFIX = "http://localhost:8001";

    @PostConstruct
    public void init() {
        List<String> list = client.getServices();
        REST_URL_PREFIX = "http://" + list.get(0);
        System.out.println(REST_URL_PREFIX);
    }

    /**
     * 使用 使用restTemplate访问restful接口非常的简单粗暴无脑。 (url, requestMap,
     * ResponseBean.class)这三个参数分别代表 REST请求地址、请求参数、HTTP响应转换被转换成的对象类型。
     */
    @Autowired
    private RestTemplate restTemplate;

    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/consumer/dept/list")
    public List<Dept> list() {
        return restTemplate.getForObject(REST_URL_PREFIX + "/dept/list", List.class);
    }
}

```

（5）测试

开启注册中心：
> suncodes-springcloud-eureka-server-one
>
> suncodes-springcloud-eureka-server-two
>
> suncodes-springcloud-eureka-server-three

开启服务提供者：
> 需要注册到eureka服务中心
> 
> suncodes-springcloud-eureka-client-provider-one

开启服务消费者：
> 需要注册到eureka服务中心
> 
> suncodes-springcloud-eureka-client-consumer-ribbon





