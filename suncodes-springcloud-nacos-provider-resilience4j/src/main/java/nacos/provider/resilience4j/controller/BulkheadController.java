package nacos.provider.resilience4j.controller;

import api.entities.Dept;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import nacos.provider.resilience4j.service.DeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
public class BulkheadController {
    @Autowired
    private DeptService service;

    /**
     * 隔离
     *
     * @param id
     * @return
     */
    @Bulkhead(name = "backendA", fallbackMethod = "fallback", type = Bulkhead.Type.SEMAPHORE)
    @RequestMapping(value = "bulkhead/dept/get/{id}", method = RequestMethod.GET)
    public Dept get(@PathVariable("id") Long id) {
        System.out.println("进入 get 方法");
        if (id < 0) {
            throw new RuntimeException("参数不能为负数");
        }
        Dept dept = service.get(id);
        if (dept == null) {
            throw new RuntimeException("查询不到数据");
        }
        return dept;
    }

    @Bulkhead(name = "backendC", fallbackMethod = "fallbackFuture", type = Bulkhead.Type.THREADPOOL)
    @RequestMapping(value = "bulkhead/dept/get1/{id}", method = RequestMethod.GET)
    public CompletableFuture<Dept> get1(@PathVariable("id") Long id) {
        System.out.println("进入 get 方法");
        return CompletableFuture.supplyAsync(() -> service.get(id));
    }

    public Dept fallback(Long id, Throwable e) {
        System.out.println(e.getMessage());
        System.out.println("进入 fallback 方法");
        Dept dept = new Dept();
        dept.setDeptNo(id).setDeptName("bulkhead").setDbSource("bulkhead");
        return dept;
    }

    public CompletableFuture<Dept> fallbackFuture(Long id, Throwable e) {
        System.out.println(e.getMessage());
        System.out.println("进入 fallback 方法");
        Dept dept = new Dept();
        dept.setDeptNo(id).setDeptName("bulkhead").setDbSource("bulkhead");
        return CompletableFuture.supplyAsync(() -> dept);
    }
}
