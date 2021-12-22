package nacos.config.client.standalone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class NacosConfigStandaloneApplication {
    public static void main(String[] args) {
        SpringApplication.run(NacosConfigStandaloneApplication.class, args);
    }
}
