package eureka.gateway;

//import com.netflix.hystrix.HystrixCommand;
//import com.netflix.hystrix.HystrixCommandGroupKey;
//import com.netflix.hystrix.HystrixCommandProperties;
import com.google.common.base.Function;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.circuitbreaker.event.CircuitBreakerOnErrorEvent;
import io.github.resilience4j.circuitbreaker.event.CircuitBreakerOnSuccessEvent;
import io.github.resilience4j.core.EventConsumer;
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
//    @Bean
//    public Customizer<ReactiveResilience4JCircuitBreakerFactory> defaultCustomizer() {
//
//                CircuitBreakerConfig circuitBreakerConfig = CircuitBreakerConfig.custom() //
//                .slidingWindowType(CircuitBreakerConfig.SlidingWindowType.COUNT_BASED) // 滑动窗口的类型为时间窗口
//                .slidingWindowSize(6) // 时间窗口的大小为60秒
//                .minimumNumberOfCalls(1) // 在单位时间窗口内最少需要5次调用才能开始进行统计计算
//                .failureRateThreshold(50) // 在单位时间窗口内调用失败率达到50%后会启动断路器
//                .enableAutomaticTransitionFromOpenToHalfOpen() // 允许断路器自动由打开状态转换为半开状态
//                .permittedNumberOfCallsInHalfOpenState(5) // 在半开状态下允许进行正常调用的次数
//                .waitDurationInOpenState(Duration.ofSeconds(60)) // 断路器打开状态转换为半开状态需要等待60秒
////                .recordExceptions(RuntimeException.class) // 所有异常都当作失败来处理
//                        .recordException(throwable -> {
//                            if (throwable instanceof RuntimeException) {
//                                return true;
//                            } else {
//                                return false;
//                            }
//                        })
//                        .recordResult(o -> {
//                            System.out.println(o.toString());
//                            return true;
//                        })
//                        .writableStackTraceEnabled(true)
//
//                .build();
//
////        Resilience4JConfigBuilder.Resilience4JCircuitBreakerConfiguration build = new Resilience4JConfigBuilder("")
//////                .circuitBreakerConfig(CircuitBreakerConfig.ofDefaults())
////                .circuitBreakerConfig(circuitBreakerConfig)
////                .timeLimiterConfig(TimeLimiterConfig.custom().timeoutDuration(Duration.ofSeconds(4)).build())
////                .build();
//
////        io.github.resilience4j.reactor.circuitbreaker.operator.CircuitBreakerOperator CircuitBreakerSubscriber
//
//        return factory -> factory.configure(builder -> builder
////                .circuitBreakerConfig(CircuitBreakerConfig.ofDefaults())
//                .circuitBreakerConfig(circuitBreakerConfig)
////                .timeLimiterConfig(TimeLimiterConfig.custom().timeoutDuration(Duration.ofSeconds(4)).build())
//                .build(), "providerCircuitBreaker-02", "provider-8001-02","providerCircuitBreaker-01", "provider-8001-01");
//    }

//    @Bean
//    public ReactiveResilience4JCircuitBreakerFactory reactiveResilience4JCircuitBreakerFactory(
//            CircuitBreakerRegistry circuitBreakerRegistry) {
//        ReactiveResilience4JCircuitBreakerFactory reactiveResilience4JCircuitBreakerFactory = new ReactiveResilience4JCircuitBreakerFactory();
//        reactiveResilience4JCircuitBreakerFactory.configureCircuitBreakerRegistry(circuitBreakerRegistry);
//
//        TimeLimiterConfig timeLimiterConfig = TimeLimiterConfig.custom()
//                .timeoutDuration(Duration.ofSeconds(1)).cancelRunningFuture(true)
//                .build();
//        reactiveResilience4JCircuitBreakerFactory.configure(builder -> builder.timeLimiterConfig(timeLimiterConfig).build(),
//                "backendA", "backendB");
//        return reactiveResilience4JCircuitBreakerFactory;
//    }

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("provider-8001-02",
                        r -> r.path("/provider02/**")
                                .filters(f -> f.circuitBreaker(c ->
                                        // TODO 此名称和 自定义的配置策略必须一致
                                        c.setName("thisCircuitBreaker")
                                                .setFallbackUri("forward:/provider-fallback").addStatusCode("500"))
                                        .rewritePath("/provider02/?(?<segment>.*)",
                                                "/java/$\\{segment}"))
                                .uri("http://localhost:8001"))
                .build();
    }

    @Bean
    public Customizer<ReactiveResilience4JCircuitBreakerFactory> defaultCustomizer() {

        CircuitBreakerConfig circuitBreakerConfig = CircuitBreakerConfig.custom()
                // 滑动窗口的类型为时间窗口、次数窗口
                .slidingWindowType(CircuitBreakerConfig.SlidingWindowType.COUNT_BASED)
                // 时间窗口的大小为60秒
                .slidingWindowSize(60)
                // 在单位时间窗口内最少需要5次调用才能开始进行统计计算
                .minimumNumberOfCalls(5)
                // 在单位时间窗口内调用失败率达到50%后会启动断路器
                .failureRateThreshold(50)
                // 允许断路器自动由打开状态转换为半开状态
                .enableAutomaticTransitionFromOpenToHalfOpen()
                // 在半开状态下允许进行正常调用的次数
                .permittedNumberOfCallsInHalfOpenState(5)
                // 断路器打开状态转换为半开状态需要等待60秒
                .waitDurationInOpenState(Duration.ofSeconds(60))
//                .recordExceptions(RuntimeException.class) // 所有异常都当作失败来处理
                // 记录异常：指的是此异常是否计算在异常调用之内；影响的就是调用失败的比例。
                // 其实当有异常发生，会调用回退函数，只是此异常当做成功调用了。
                // 也就是说此异常的发生，不会打开熔断器，但是会调用回退函数。
                // 调用成功与否，和记录异常与否没有直接关联。
                // 至于调用失败了，才有可能记录异常，如果忽略记录异常，则此异常不被计算在内。
                .recordException(throwable -> {
                    System.out.println("--------------------");
                    if (throwable instanceof RuntimeException) {
                        System.out.println(11111111);
                        return false;
                    } else {
                        System.out.println(222222222);
                        return true;
                    }
                })
//                .recordResult(o -> {
//                    System.out.println(o.toString());
//                    return true;
//                })
//                .writableStackTraceEnabled(true)

                .build();

//        Resilience4JConfigBuilder.Resilience4JCircuitBreakerConfiguration build = new Resilience4JConfigBuilder("")
////                .circuitBreakerConfig(CircuitBreakerConfig.ofDefaults())
//                .circuitBreakerConfig(circuitBreakerConfig)
//                .timeLimiterConfig(TimeLimiterConfig.custom().timeoutDuration(Duration.ofSeconds(4)).build())
//                .build();

//        io.github.resilience4j.reactor.circuitbreaker.operator.CircuitBreakerOperator CircuitBreakerSubscriber

        return factory -> factory.configure(builder -> builder
//                .circuitBreakerConfig(CircuitBreakerConfig.ofDefaults())
                .circuitBreakerConfig(circuitBreakerConfig)
                .timeLimiterConfig(TimeLimiterConfig.custom().timeoutDuration(Duration.ofSeconds(4)).build())
                .build(), "thisCircuitBreaker");
    }
}
