package eureka.gateway;

import eureka.gateway.ratelimiter.InMemoryRateLimiter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.factory.RequestRateLimiterGatewayFilterFactory;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import reactor.core.publisher.Mono;

import java.util.function.Consumer;

@Slf4j
@Configuration
public class RequestRateLimiterConfig {

    @Bean
    @Primary
    public KeyResolver ipAddressKeyResolver() {
        return exchange -> {
            String hostAddress = exchange.getRequest().getRemoteAddress().getAddress().getHostAddress();
            log.info("ipAddressKeyResolver 限流规则 ip: {}", hostAddress);
            return Mono.just(hostAddress);
        };
    }

    @Bean
    public KeyResolver pathKeyResolver() {
        return exchange -> {
            Route route = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR);
            log.info("pathKeyResolver 限流规则 ip: {}", route.getId());
            return Mono.just(exchange.getRequest().getPath().toString());
        };
    }

    @Bean
    @Primary
    public RedisRateLimiter myRateLimiter() {
        return new RedisRateLimiter(2, 4);
    }

    @Bean(name = "requestRateLimiter")
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("requestRateLimiter-02",
                        r -> r.path("/rate2/**")
                                .filters(f -> f
                                        .requestRateLimiter(config -> config
                                                .setRateLimiter(myRateLimiter())
                                                .setKeyResolver(pathKeyResolver()))
                                        .rewritePath("/rate2/?(?<segment>.*)",
                                                "/$\\{segment}"))
                                .uri("https://httpbin.org"))
                .build();
    }

    @Bean
    public InMemoryRateLimiter inMemoryRateLimiter() {
        return new InMemoryRateLimiter(1, 1);
    }
}
