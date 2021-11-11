## suncodes-springcloud-provider-dept-rest

### POM 文件

需要引用 API，需要把对应的模块进行打包

注意：IDEA有点问题，打包引用API之后，可能还是会报红

```xml
    <properties>
        <java.version>1.8</java.version>
        <springboot.version>2.4.5</springboot.version>
        <mysql.version>8.0.18</mysql.version>
        <mybatis-springboot.version>2.1.4</mybatis-springboot.version>
    </properties>

    <dependencies>
        <dependency>
            <groupId>suncodes</groupId>
            <artifactId>suncodes-springcloud-api</artifactId>
            <version>1.0-SNAPSHOT</version>
        </dependency>

        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <version>${mysql.version}</version>
        </dependency>

        <dependency>
            <groupId>org.mybatis.spring.boot</groupId>
            <artifactId>mybatis-spring-boot-starter</artifactId>
            <version>${mybatis-springboot.version}</version>
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

### YML 文件

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
```

### SQL 文件

```sql
DROP DATABASE IF EXISTS springcloud;
CREATE DATABASE springcloud CHARACTER SET UTF8;
USE springcloud;
CREATE TABLE dept
(
  dept_no BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
  dept_name VARCHAR(60),
  db_source   VARCHAR(60)
);

INSERT INTO dept(dept_name,db_source) VALUES('开发部',DATABASE());
INSERT INTO dept(dept_name,db_source) VALUES('人事部',DATABASE());
INSERT INTO dept(dept_name,db_source) VALUES('财务部',DATABASE());
INSERT INTO dept(dept_name,db_source) VALUES('市场部',DATABASE());
INSERT INTO dept(dept_name,db_source) VALUES('运维部',DATABASE());

SELECT * FROM dept;

```

### 驼峰命名自动映射
mybatis/SqlMapConfig.xml
```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE configuration
        PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>
    <settings>
        <setting name="mapUnderscoreToCamelCase" value="true"/>
    </settings>
</configuration>
```

### DeptDao部门接口

注意 @Mapper 注解，当使用autoconfiger的时候，会自动被扫描

```java
@Mapper
public interface DeptDao {
    public boolean addDept(Dept dept);

    public Dept findById(Long id);

    public List<Dept> findAll();
}
```

### DeptMapper.xml

mybatis/mapper/DeptMapper.xml
```xml
<mapper namespace="provider.dept.rest.dao.DeptDao">
    <select id="findById" resultType="api.entities.Dept" parameterType="Long">
		select dept_no,dept_name,db_source from dept where dept_no=#{deptNo};
	</select>
    <select id="findAll" resultType="Dept">
		select dept_no,dept_name,db_source from dept;
	</select>
    <insert id="addDept" parameterType="Dept">
		INSERT INTO dept(dept_name,db_source) VALUES(#{deptName},DATABASE());
	</insert>
</mapper>
```

### 提供接口服务
```java
@Service
public class DeptService {
    @Autowired
    private DeptDao dao;

    public boolean add(Dept dept) {
        return dao.addDept(dept);
    }

    public Dept get(Long id) {
        return dao.findById(id);
    }

    public List<Dept> list() {
        return dao.findAll();
    }
}
```


```java
@RestController
public class DeptController {
    @Autowired
    private DeptService service;

    @RequestMapping(value = "/dept/add", method = RequestMethod.POST)
    public boolean add(@RequestBody Dept dept) {
        return service.add(dept);
    }

    @RequestMapping(value = "/dept/get/{id}", method = RequestMethod.GET)
    public Dept get(@PathVariable("id") Long id) {
        return service.get(id);
    }

    @RequestMapping(value = "/dept/list", method = RequestMethod.GET)
    public List<Dept> list() {
        return service.list();
    }
}
```



