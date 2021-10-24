## suncodes-springcloud-api

新建部门Entity且配合lombok使用

```JAVA
package suncodes.springcloud.api.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * entity --orm--- db_table
 *
 * @author sunchuizhe
 */
@NoArgsConstructor
//@AllArgsConstructor
@Data
@Accessors(chain = true)
public class Dept implements Serializable {
    /**
     * 主键
     */
    private Long deptNo;
    /**
     * 部门名称
     */
    private String deptName;
    /**
     * 来自那个数据库，因为微服务架构可以一个服务对应一个数据库，同一个信息被存储到不同数据库
     */
    private String dbSource;

    public Dept(String deptName) {
        super();
        this.deptName = deptName;
    }
}

```

mvn clean install后给其它模块引用，达到通用目的。

也即需要用到部门实体的话，不用每个工程都定义一份，直接引用本模块即可。



