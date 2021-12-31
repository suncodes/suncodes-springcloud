package eureka.gateway;

//import com.netflix.hystrix.HystrixCommand;
//import com.netflix.hystrix.HystrixCommandGroupKey;
//import com.netflix.hystrix.HystrixCommandProperties;
import com.google.common.base.Function;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import org.springframework.cloud.circuitbreaker.resilience4j.ReactiveResilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.cloud.gateway.config.GatewayResilience4JCircuitBreakerAutoConfiguration;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
//import org.springframework.cloud.netflix.hystrix.HystrixCircuitBreakerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

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

//    @Bean
//    public HystrixCircuitBreakerFactory defaultConfig() {
//        HystrixCircuitBreakerFactory circuitBreakerFactory = new HystrixCircuitBreakerFactory();
//        circuitBreakerFactory.configureDefault(id -> HystrixCommand.Setter
//                .withGroupKey(HystrixCommandGroupKey.Factory.asKey(id)).andCommandPropertiesDefaults(
//                        HystrixCommandProperties.Setter()
//                                .withExecutionTimeoutInMilliseconds(4000)
//                                .withCircuitBreakerEnabled(true)
//                                .withCircuitBreakerRequestVolumeThreshold(10)
//                                .withCircuitBreakerErrorThresholdPercentage(50)
//                                .withCircuitBreakerSleepWindowInMilliseconds(5000)));
////        GatewayResilience4JCircuitBreakerAutoConfiguration
//        return circuitBreakerFactory;
//    }


//    @Bean
//    public ReactiveResilience4JCircuitBreakerFactory circuitBreaker() {
//
//        CircuitBreakerConfig circuitBreakerConfig = CircuitBreakerConfig.custom() //
//                .slidingWindowType(CircuitBreakerConfig.SlidingWindowType.TIME_BASED) // 滑动窗口的类型为时间窗口
//                .slidingWindowSize(60) // 时间窗口的大小为60秒
//                .minimumNumberOfCalls(5) // 在单位时间窗口内最少需要5次调用才能开始进行统计计算
//                .failureRateThreshold(50) // 在单位时间窗口内调用失败率达到50%后会启动断路器
//                .enableAutomaticTransitionFromOpenToHalfOpen() // 允许断路器自动由打开状态转换为半开状态
//                .permittedNumberOfCallsInHalfOpenState(5) // 在半开状态下允许进行正常调用的次数
//                .waitDurationInOpenState(Duration.ofSeconds(60)) // 断路器打开状态转换为半开状态需要等待60秒
//                .recordExceptions(Throwable.class) // 所有异常都当作失败来处理
//                .build();
//
//        ReactiveResilience4JCircuitBreakerFactory factory = new ReactiveResilience4JCircuitBreakerFactory();
//        factory.configureDefault(id -> new Resilience4JConfigBuilder(id)
//                .timeLimiterConfig(TimeLimiterConfig.custom().timeoutDuration(Duration.ofMillis(500)).build())
//                .circuitBreakerConfig(circuitBreakerConfig).build());
//
//        return factory;
//    }


    // https://stackoverflow.com/questions/58879226/configure-specific-breaker-for-route-with-resilience4j-in-spring-cloud-gateway
    // spring cloud gateway circuit breaker
    @Bean
    public Customizer<ReactiveResilience4JCircuitBreakerFactory> defaultCustomizer() {

                CircuitBreakerConfig circuitBreakerConfig = CircuitBreakerConfig.custom() //
                .slidingWindowType(CircuitBreakerConfig.SlidingWindowType.TIME_BASED) // 滑动窗口的类型为时间窗口
                .slidingWindowSize(6) // 时间窗口的大小为60秒
                .minimumNumberOfCalls(5) // 在单位时间窗口内最少需要5次调用才能开始进行统计计算
                .failureRateThreshold(50) // 在单位时间窗口内调用失败率达到50%后会启动断路器
                .enableAutomaticTransitionFromOpenToHalfOpen() // 允许断路器自动由打开状态转换为半开状态
                .permittedNumberOfCallsInHalfOpenState(5) // 在半开状态下允许进行正常调用的次数
                .waitDurationInOpenState(Duration.ofSeconds(60)) // 断路器打开状态转换为半开状态需要等待60秒
                .recordExceptions(Throwable.class) // 所有异常都当作失败来处理
                .build();

//        Resilience4JConfigBuilder.Resilience4JCircuitBreakerConfiguration build = new Resilience4JConfigBuilder("")
////                .circuitBreakerConfig(CircuitBreakerConfig.ofDefaults())
//                .circuitBreakerConfig(circuitBreakerConfig)
//                .timeLimiterConfig(TimeLimiterConfig.custom().timeoutDuration(Duration.ofSeconds(4)).build())
//                .build();



        return factory -> factory.configure(builder -> builder
//                .circuitBreakerConfig(CircuitBreakerConfig.ofDefaults())
                .circuitBreakerConfig(circuitBreakerConfig)
                .timeLimiterConfig(TimeLimiterConfig.custom().timeoutDuration(Duration.ofSeconds(4)).build())
                .build(), "providerCircuitBreaker-02");
    }
}
