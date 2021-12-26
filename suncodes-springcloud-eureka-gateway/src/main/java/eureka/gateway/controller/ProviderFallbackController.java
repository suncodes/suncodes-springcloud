package eureka.gateway.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProviderFallbackController {

    @GetMapping("/provider-fallback")
    public String fallback() {
        System.out.println("provider-fallback");
        return "fallback";
    }
}
