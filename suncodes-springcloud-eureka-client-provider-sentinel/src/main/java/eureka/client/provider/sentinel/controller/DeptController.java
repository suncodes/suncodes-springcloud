package eureka.client.provider.sentinel.controller;

import api.entities.Dept;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.shared.Application;
import com.netflix.discovery.shared.Applications;
import eureka.client.provider.sentinel.service.DeptService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
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

    /**
     * 测试熔断降级策略：慢调用比例 RT
     * @return
     */
    @GetMapping("/testD")
    public String testD() {
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("testD 测试RT");
        return "------testD";
    }

    /**
     * 测试熔断降级策略：异常比例
     * @return
     */
    @GetMapping("/testDD")
    public String testDD() {
        log.info("testDD 测试 异常比例");
        int age = 10 / 0;
        return "------testDD";
    }

    /**
     * 测试熔断降级策略：异常数
     * @return
     */
    @GetMapping("/testDDD")
    public String testDDD() {
        log.info("testDDD 测试 异常数");
        int age = 10 / 0;
        return "------testDDD";
    }

    /**
     * 热点参数限流测试（此方法对于热点限流不生效，必须使用@SentinelResource）
     * CommonFilter是否支持热点限流？
     * 答案：不支持
     * 因为CommonFilter源码里标记资源SphU.entry(String, int, EntryType),
     * 并没有像sentinel-dubbo-adaptor里的SentinelDubboProviderFilter那样通过4个参数重载的方法
     * SphU.entry(java.lang.String, int, EntryType, java.lang.Object[])来标记资源，
     * 即传递接口相关的参数，因此它不能使用热点参数规则。
     * @param p1
     * @param p2
     * @return
     */
    @GetMapping("/testHotKey")
    public String testHotKey(@RequestParam(value = "p1",required = false) String p1,
                             @RequestParam(value = "p2",required = false) String p2) {
        return "------testHotKey";
    }

    @GetMapping("/testHotKey1")
    @SentinelResource(value = "testHotKey1",blockHandler = "dealTestHotKey")
    public String testHotKey1(@RequestParam(value = "p1",required = false) String p1,
                             @RequestParam(value = "p2",required = false) String p2) {
        return "------testHotKey1";
    }

    @GetMapping("/testHotKey2")
    @SentinelResource(value = "testHotKey2",blockHandler = "dealTestHotKey")
    public String testHotKey2(@RequestParam(value = "p1",required = false) String p1,
                              @RequestParam(value = "p2",required = false) String p2) {
        int age = 10/0;
        return "------testHotKey2";
    }

    //兜底方法
    public String dealTestHotKey (String p1, String p2, BlockException exception){
        return "------deal_testHotKey,o(╥﹏╥)o";
    }

}
