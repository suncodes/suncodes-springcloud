package eureka.client.consumer.ribbon;

import eureka.client.consumer.ribbon.myrule.MySelfRule;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.netflix.ribbon.RibbonClients;

/**
 * 没走通
 */
@SpringBootApplication
@RibbonClient(name = "PROVIDER", configuration = MySelfRule.class)
//@RibbonClients(defaultConfiguration = MySelfRule.class)
public class ConsumerRibbonApplication {
    public static void main(String[] args) {
        SpringApplication.run(ConsumerRibbonApplication.class, args);
    }
}
