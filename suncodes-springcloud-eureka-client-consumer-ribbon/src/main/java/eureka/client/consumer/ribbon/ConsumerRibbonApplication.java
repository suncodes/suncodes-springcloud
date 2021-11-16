package eureka.client.consumer.ribbon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClients;

@SpringBootApplication
//@LoadBalancerClient(name = "111", configuration = {CustomLoadBalancerConfiguration.class})
@LoadBalancerClients(defaultConfiguration = {CustomLoadBalancerConfiguration.class})
public class ConsumerRibbonApplication {
    public static void main(String[] args) {
        SpringApplication.run(ConsumerRibbonApplication.class, args);
    }
}
