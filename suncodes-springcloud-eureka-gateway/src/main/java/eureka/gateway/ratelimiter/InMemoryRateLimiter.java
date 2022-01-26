package eureka.gateway.ratelimiter;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.ConsumptionProbe;
import io.github.bucket4j.Refill;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.ratelimit.AbstractRateLimiter;
import org.springframework.validation.annotation.Validated;
import reactor.core.publisher.Mono;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class InMemoryRateLimiter extends AbstractRateLimiter<InMemoryRateLimiter.Config> {

    public static final String CONFIGURATION_PROPERTY_NAME = "in-memory-rate-limiter";

    private InMemoryRateLimiter.Config defaultConfig;

    private final Map<String, Bucket> ipBucketMap = new ConcurrentHashMap<>();

    public InMemoryRateLimiter() {
        super(InMemoryRateLimiter.Config.class, CONFIGURATION_PROPERTY_NAME, null);
    }

    public InMemoryRateLimiter(int defaultReplenishRate, int defaultBurstCapacity) {
        super(Config.class, CONFIGURATION_PROPERTY_NAME, null);
        this.defaultConfig = new InMemoryRateLimiter.Config()
                .setReplenishRate(defaultReplenishRate)
                .setBurstCapacity(defaultBurstCapacity);
    }

    @Override
    public Mono<Response> isAllowed(String routeId, String id) {
        log.info("自定义 InMemoryRateLimiter isAllowed 方法");
        InMemoryRateLimiter.Config routeConfig = getConfig().get(routeId);

        if (routeConfig == null) {
            if (defaultConfig == null) {
                throw new IllegalArgumentException("No Configuration found for route " + routeId);
            }
            routeConfig = defaultConfig;
        }

        // How many requests per second do you want a user to be allowed to do?
        int replenishRate = routeConfig.getReplenishRate();

        // How much bursting do you want to allow?
        int burstCapacity = routeConfig.getBurstCapacity();

        Bucket bucket = ipBucketMap.computeIfAbsent(id, k -> {
            Refill refill = Refill.greedy(replenishRate, Duration.ofSeconds(1));
            Bandwidth limit = Bandwidth.classic(burstCapacity, refill);
            return Bucket4j.builder().addLimit(limit).build();
        });

        // tryConsume returns false immediately if no tokens available with the bucket
        ConsumptionProbe probe = bucket.tryConsumeAndReturnRemaining(1);
        if (probe.isConsumed()) {
            // the limit is not exceeded
            return Mono.just(new Response(true, getHeaders(routeConfig, probe.getRemainingTokens())));
        } else {
            // limit is exceeded
            return Mono.just(new Response(false, getHeaders(routeConfig, -1L)));
        }
    }

    @NotNull
    public Map<String, String> getHeaders(InMemoryRateLimiter.Config config, Long tokensLeft) {
        Map<String, String> headers = new HashMap<>();

        String replenishRateHeader = "X-RateLimit-Replenish-Rate";
        headers.put(replenishRateHeader, String.valueOf(config.getReplenishRate()));
        String burstCapacityHeader = "X-RateLimit-Burst-Capacity";
        headers.put(burstCapacityHeader, String.valueOf(config.getBurstCapacity()));

        return headers;
    }

    @Validated
    public static class Config {
        @Min(1)
        private int replenishRate;

        @Min(0)
        private int burstCapacity = 0;

        public int getReplenishRate() {
            return replenishRate;
        }

        public InMemoryRateLimiter.Config setReplenishRate(int replenishRate) {
            this.replenishRate = replenishRate;
            return this;
        }

        public int getBurstCapacity() {
            return burstCapacity;
        }

        public InMemoryRateLimiter.Config setBurstCapacity(int burstCapacity) {
            this.burstCapacity = burstCapacity;
            return this;
        }

        @Override
        public String toString() {
            return "Config{"
                    + "replenishRate=" + replenishRate
                    + ", burstCapacity=" + burstCapacity
                    + '}';
        }
    }
}