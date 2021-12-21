package nacos.api.feign.fallback;

import api.entities.Dept;
import nacos.api.feign.api.DeptAPI;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DeptFallbackFactory implements FallbackFactory<DeptAPI> {
    @Override
    public DeptAPI create(Throwable cause) {
        return new DeptAPI() {
            @Override
            public boolean add(Dept dept) {
                return false;
            }

            @Override
            public Dept get(Long id) {
                return new Dept().setDeptNo(id)
                        .setDeptName("该ID：" + id + "没有对应的信息," +
                                "Consumer客户端提供的降级信息,此刻服务Provider已经关闭")
                        .setDbSource("no this database in MySQL");
            }

            @Override
            public List<Dept> list() {
                return null;
            }
        };
    }
}
