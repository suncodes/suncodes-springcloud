package eureka.client.consumer.loadbalancer.sentinel.controller;

import api.entities.Dept;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 *
 */
@RestController
public class ConsumerLoadBalancerController {

    @Autowired
    private DiscoveryClient client;

    private static String REST_URL_PREFIX = "http://localhost:8001";

    @PostConstruct
    public void init() {
        List<String> list = client.getServices();
        REST_URL_PREFIX = "http://" + list.get(0);
        System.out.println(REST_URL_PREFIX);
    }

    /**
     * 使用 使用restTemplate访问restful接口非常的简单粗暴无脑。 (url, requestMap,
     * ResponseBean.class)这三个参数分别代表 REST请求地址、请求参数、HTTP响应转换被转换成的对象类型。
     */
    @Autowired
    private RestTemplate restTemplate;

    @RequestMapping(value = "/consumer/dept/add")
    public boolean add(Dept dept) {
        return restTemplate.postForObject(REST_URL_PREFIX + "/dept/add", dept, Boolean.class);
    }

    /**
     * TODO 方式一
     * fallback ：方法异常回调
     * blockHandler ：限流
     * exceptionsToIgnore ：忽略哪些异常进入fallback以及异常统计，异常原样抛出
     * @param id
     * @return
     */
    @RequestMapping(value = "/consumer/dept/get/{id}")
    @SentinelResource(value = "getId",
            fallback = "timeOutFallbackMethod",
            blockHandler = "blockMethod",
            exceptionsToIgnore = IllegalArgumentException.class)
    public Dept get(@PathVariable("id") Long id) {
        if (id < 0) {
            throw new IllegalArgumentException ("参数非法");
        }
        Dept dept = restTemplate.getForObject(REST_URL_PREFIX + "/dept/get/" + id, Dept.class);
        if (dept == null) {
            throw new RuntimeException("数据不存在");
        }
        return dept;
    }

    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/consumer/dept/list")
    public List<Dept> list() {
        return restTemplate.getForObject(REST_URL_PREFIX + "/dept/list", List.class);
    }

    /**
     * 测试@EnableDiscoveryClient,消费端可以调用服务发现
     */
    @RequestMapping(value = "/consumer/dept/discovery")
    public Object discovery() {
        return restTemplate.getForObject(REST_URL_PREFIX + "/dept/discovery", Object.class);
    }

    /**
     * 兜底方法
     * @param id
     * @return
     */
    public Dept timeOutFallbackMethod(@PathVariable("id") Long id) {
        Dept dept = new Dept();
        dept.setDeptNo(-1L);
        dept.setDeptName("我是消费者80，对付支付系统繁忙请10秒钟后再试或者自己运行出错请检查自己,(┬＿┬)");
        dept.setDbSource("");
        return dept;
    }

    public Dept blockMethod(@PathVariable("id") Long id) {
        Dept dept = new Dept();
        dept.setDeptNo(-1L);
        dept.setDeptName("sentinel block method");
        dept.setDbSource("");
        return dept;
    }
}
