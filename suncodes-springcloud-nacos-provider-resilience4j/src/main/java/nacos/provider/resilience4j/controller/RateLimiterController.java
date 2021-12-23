package nacos.provider.resilience4j.controller;

import api.entities.Dept;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import nacos.provider.resilience4j.service.DeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class RateLimiterController {

    @Autowired
    private DeptService service;

    /**
     * 隔离
     *
     * @param id
     * @return
     */
    @RateLimiter(name = "backendE", fallbackMethod = "fallback")
    @RequestMapping(value = "rateLimiter/dept/get/{id}", method = RequestMethod.GET)
    public Dept get(@PathVariable("id") Long id) {
        System.out.println("进入 get 方法");
        return service.get(id);
    }

    public Dept fallback(Long id, Throwable e) {
        System.out.println(e.getMessage());
        System.out.println("进入 fallback 方法");
        Dept dept = new Dept();
        dept.setDeptNo(id).setDeptName("bulkhead").setDbSource("bulkhead");
        return dept;
    }
}
