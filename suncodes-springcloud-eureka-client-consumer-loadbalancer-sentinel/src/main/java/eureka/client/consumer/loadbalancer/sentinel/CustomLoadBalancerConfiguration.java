package eureka.client.consumer.loadbalancer.sentinel;

import eureka.client.consumer.loadbalancer.sentinel.loadbalancer.MyRandomLoadBalancer;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.loadbalancer.core.ReactorLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

public class CustomLoadBalancerConfiguration {

    // TODO 随意打开一个
//    /**
//     * 切换为LoadBalancer的其他内置策略
//     * @param environment
//     * @param loadBalancerClientFactory
//     * @return
//     */
//    @Bean
//    ReactorLoadBalancer<ServiceInstance> randomLoadBalancer(Environment environment,
//                                                            LoadBalancerClientFactory loadBalancerClientFactory) {
//        String property = environment.getProperty(LoadBalancerClientFactory.PROPERTY_NAME);
//        ObjectProvider<ServiceInstanceListSupplier> provider =
//                loadBalancerClientFactory.getLazyProvider(property, ServiceInstanceListSupplier.class);
//        return new RandomLoadBalancer(provider, property);
//    }

    /**
     * 自定义策略
     * @param environment
     * @param loadBalancerClientFactory
     * @return
     */
    @Bean
    ReactorLoadBalancer<ServiceInstance> myRandomLoadBalancer(Environment environment,
                                                            LoadBalancerClientFactory loadBalancerClientFactory) {
        String property = environment.getProperty(LoadBalancerClientFactory.PROPERTY_NAME);
        ObjectProvider<ServiceInstanceListSupplier> provider =
                loadBalancerClientFactory.getLazyProvider(property, ServiceInstanceListSupplier.class);
        return new MyRandomLoadBalancer(provider, property);
    }
}
