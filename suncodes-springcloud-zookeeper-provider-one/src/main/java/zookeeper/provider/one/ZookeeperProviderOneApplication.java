package zookeeper.provider.one;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class ZookeeperProviderOneApplication {
    public static void main(String[] args) {
        SpringApplication.run(ZookeeperProviderOneApplication.class);
    }
}
