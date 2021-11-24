package eureka.client.consumer.feign.controller;

import api.entities.Dept;
import eureka.client.api.feign.api.DeptAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
public class ConsumerFeignController {

    @Autowired
    private DeptAPI deptAPI;

    @RequestMapping(value = "/consumer/dept/add")
    public boolean add(Dept dept) {
        return deptAPI.add(dept);
    }

    @RequestMapping(value = "/consumer/dept/get/{id}")
    public Dept get(@PathVariable("id") Long id) {
        return deptAPI.get(id);
    }

    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/consumer/dept/list")
    public List<Dept> list() {
        return deptAPI.list();
    }
}
