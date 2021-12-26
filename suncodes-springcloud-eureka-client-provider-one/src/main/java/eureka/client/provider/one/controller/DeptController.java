package eureka.client.provider.one.controller;

import api.entities.Dept;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.shared.Application;
import com.netflix.discovery.shared.Applications;
import eureka.client.provider.one.service.DeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.*;


import java.util.List;

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

    /**
     * http://localhost:8001/dept/eurekaClient
     * 使用原始的 Netflix EurekaClient
     */
    @Autowired
    private EurekaClient eurekaClient;

    @RequestMapping(value = "/dept/eurekaClient", method = RequestMethod.GET)
    public String serviceUrl() {
        Applications applications = eurekaClient.getApplications();
        List<Application> registeredApplications = applications.getRegisteredApplications();
        for (Application registeredApplication : registeredApplications) {
            List<InstanceInfo> instances = registeredApplication.getInstances();
            for (InstanceInfo instance : instances) {
                System.out.println(instance);
            }
        }
        return "success";
    }

    @RequestMapping(value = "/yml/get/{id}", method = RequestMethod.GET)
    public Dept ymlGet(@PathVariable("id") Long id) {
        Dept dept = service.get(id);
        if (dept == null) {
            throw new RuntimeException("没有查询到数据");
        }
        return dept;
    }

    @RequestMapping(value = "/java/get/{id}", method = RequestMethod.GET)
    public Dept javaGet(@PathVariable("id") Long id) {
        Dept dept = service.get(id);
        if (dept == null) {
            throw new RuntimeException("没有查询到数据");
        }
        return dept;
    }
}
