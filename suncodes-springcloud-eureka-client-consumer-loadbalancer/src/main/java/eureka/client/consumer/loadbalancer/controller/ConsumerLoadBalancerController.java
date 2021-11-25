package eureka.client.consumer.loadbalancer.controller;

import api.entities.Dept;
import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * 由于使用 @HystrixCommand(fallbackMethod = "timeOutFallbackMethod") 方式
 * 只能对单个方法进行服务降级，但是我想对当前类的所有方法进行服务降级，怎么办？
 * 需要每个方法都要写一遍吗？
 *
 * 可以在类上添加注解 @DefaultProperties(defaultFallback = "")
 * 就可以对当前类的所有方法进行处理，
 * 此时，不再需要在 @HystrixCommand 注解上使用 fallbackMethod 属性
 *
 */
@RestController
//@DefaultProperties(defaultFallback = "")
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

//    @HystrixCommand(fallbackMethod = "timeOutFallbackMethod", commandProperties = {
//            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds",
//                    value = "1500")  //3秒钟以内就是正常的业务逻辑
//    })
    //服务熔断
    @HystrixCommand(fallbackMethod = "timeOutFallbackMethod",commandProperties = {

            @HystrixProperty(name = "circuitBreaker.enabled",value = "true"),  //是否开启断路器
            @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold",value = "10"),   //请求次数
            @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds",value = "10000"),  //时间范围
            @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage",value = "60"), //失败率达到多少后跳闸
            // 上述意思就是开启服务熔断，在10秒内，请求10次，失败率达到60%以上熔断器打开，此时拒绝访问，
            // 同样，在接下来的10秒内，熔断器半开状态，如果可以访问的错误率达到60%以下，就开始放行，关闭熔断器，
    })
    @RequestMapping(value = "/consumer/dept/get/{id}")
    public Dept get(@PathVariable("id") Long id) {
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
}
