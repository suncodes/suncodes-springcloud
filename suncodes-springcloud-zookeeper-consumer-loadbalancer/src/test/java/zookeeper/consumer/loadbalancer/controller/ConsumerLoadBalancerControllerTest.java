package zookeeper.consumer.loadbalancer.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.zookeeper.discovery.ZookeeperDiscoveryClient;

import java.util.List;

@SpringBootTest
public class ConsumerLoadBalancerControllerTest {

    @Autowired
    private ZookeeperDiscoveryClient client;

    @Test
    public void f () {
        List<String> services = client.getServices();
        System.out.println(services);
    }
}