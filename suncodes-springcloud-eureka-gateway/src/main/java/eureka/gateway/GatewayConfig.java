package eureka.gateway;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {
    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("provider-8001-02",
                        r -> r.path("/provider02/**")
                                .filters(f -> f.circuitBreaker(c ->
                                        c.setName("providerCircuitBreaker-02")
                                                .setFallbackUri("forward:/provider-fallback"))
                                        .rewritePath("/provider02/?(?<segment>.*)",
                                                "/java/$\\{segment}"))
                                .uri("http://localhost:8001"))
                .build();
    }
}
