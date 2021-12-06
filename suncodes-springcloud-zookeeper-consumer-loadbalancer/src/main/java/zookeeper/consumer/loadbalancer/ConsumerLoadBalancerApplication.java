package zookeeper.consumer.loadbalancer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClients;

/**
 * 单独使用 hystrix（不结合Feign），需要使用注解 @EnableHystrix 进行开启
 */
@SpringBootApplication
@LoadBalancerClients
public class ConsumerLoadBalancerApplication {
    public static void main(String[] args) {
        SpringApplication.run(ConsumerLoadBalancerApplication.class, args);
    }
}
