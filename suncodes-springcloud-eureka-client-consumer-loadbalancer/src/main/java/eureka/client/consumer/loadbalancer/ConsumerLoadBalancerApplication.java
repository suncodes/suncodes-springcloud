package eureka.client.consumer.loadbalancer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClients;

@SpringBootApplication
//@LoadBalancerClient(name = "111", configuration = {CustomLoadBalancerConfiguration.class})
@LoadBalancerClients(defaultConfiguration = {CustomLoadBalancerConfiguration.class})
public class ConsumerLoadBalancerApplication {
    public static void main(String[] args) {
        SpringApplication.run(ConsumerLoadBalancerApplication.class, args);
    }
}
