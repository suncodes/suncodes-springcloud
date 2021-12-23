package nacos.provider.resilience4j.controller;

import api.entities.Dept;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import nacos.provider.resilience4j.service.DeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class DeptController {
    @Autowired
    private DeptService service;

    @RequestMapping(value = "/dept/add", method = RequestMethod.POST)
    public boolean add(@RequestBody Dept dept) {
        return service.add(dept);
    }

    @CircuitBreaker(name = "backendD", fallbackMethod = "fallback")
    @RequestMapping(value = "/dept/get/{id}", method = RequestMethod.GET)
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

    public Dept fallback(Long id, Throwable e) {
        System.out.println(e.getMessage());
        System.out.println("进入 fallback 方法");
        Dept dept = new Dept();
        dept.setDeptNo(id).setDeptName("CircuitBreaker").setDbSource("CircuitBreaker");
        return dept;
    }

    @RequestMapping(value = "/dept/list", method = RequestMethod.GET)
    public List<Dept> list() {
        return service.list();
    }

    /**
     * 使用 SpringCloud 的 DiscoveryClient
     */
    @Autowired
    private DiscoveryClient client;

    @RequestMapping(value = "/dept/discovery", method = RequestMethod.GET)
    public Object discovery() {
        List<String> list = client.getServices();
        System.out.println("**********" + list);

        for (String serviceId : list) {
            List<ServiceInstance> srvList = client.getInstances(serviceId);
            for (ServiceInstance element : srvList) {
                System.out.println(element.getServiceId() + "\t" + element.getHost() + "\t" + element.getPort() + "\t"
                        + element.getUri());
            }
        }
        return this.client;
    }

//    /**
//     * http://localhost:8001/dept/eurekaClient
//     * 使用原始的 Netflix EurekaClient
//     */
//    @Autowired
//    private EurekaClient eurekaClient;
//
//    @RequestMapping(value = "/dept/eurekaClient", method = RequestMethod.GET)
//    public String serviceUrl() {
//        Applications applications = eurekaClient.getApplications();
//        List<Application> registeredApplications = applications.getRegisteredApplications();
//        for (Application registeredApplication : registeredApplications) {
//            List<InstanceInfo> instances = registeredApplication.getInstances();
//            for (InstanceInfo instance : instances) {
//                System.out.println(instance);
//            }
//        }
//        return "success";
//    }
}
