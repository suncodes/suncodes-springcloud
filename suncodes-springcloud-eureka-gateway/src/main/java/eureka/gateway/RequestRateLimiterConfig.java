package eureka.gateway;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

@Slf4j
@Configuration
public class RequestRateLimiterConfig {

    @Bean
    public KeyResolver ipAddressKeyResolver() {
        return exchange -> {
            String hostAddress = exchange.getRequest().getRemoteAddress().getAddress().getHostAddress();
            log.info("ipAddressKeyResolver 限流规则 ip: {}", hostAddress);
            return Mono.just(hostAddress);
        };
    }

//    @Bean
//    public KeyResolver pathKeyResolver() {
//        return exchange -> {
//            Route route = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR);
//            log.info("pathKeyResolver 限流规则 ip: {}", route.getId());
//            return Mono.just(exchange.getRequest().getPath().toString());
//        };
//    }
}
