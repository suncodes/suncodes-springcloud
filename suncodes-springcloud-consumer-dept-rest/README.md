## suncodes-springcloud-consumer-dept-rest

使用 REST 接口的方式进行访问服务

### POM 文件

```xml
<properties>
        <java.version>1.8</java.version>
        <springboot.version>2.4.5</springboot.version>
    </properties>

    <dependencies>
        <dependency>
            <groupId>suncodes</groupId>
            <artifactId>suncodes-springcloud-api</artifactId>
            <version>1.0-SNAPSHOT</version>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
            <version>${springboot.version}</version>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <version>${springboot.version}</version>
            <scope>test</scope>
        </dependency>

        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <configuration>
                    <source>8</source>
                    <target>8</target>
                </configuration>
            </plugin>
        </plugins>
    </build>
```

### YML
```yaml
server:
  port: 80
```

### 配置类
```java
@Configuration
public class ConsumerDeptRestConfiguration {
    @Bean
    RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
```

### 服务
RestTemplate提供了多种便捷访问远程Http服务的方法， 
是一种简单便捷的访问restful服务模板类，是Spring提供的用于访问Rest服务的客户端模板工具集

```text
http://localhost/consumer/dept/get/2
http://localhost/consumer/dept/list
http://localhost/consumer/dept/add?deptName=AI
```
