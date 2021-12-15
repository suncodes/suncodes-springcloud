package eureka.client.consumer.feign.sentinel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = {"eureka.client.consumer.feign.sentinel",
        "eureka.client.api.feign"})
@EnableFeignClients(basePackages = "eureka.client.api.feign")
public class ConsumerFeignApplication {
    public static void main(String[] args) {
        SpringApplication.run(ConsumerFeignApplication.class, args);
    }
}
