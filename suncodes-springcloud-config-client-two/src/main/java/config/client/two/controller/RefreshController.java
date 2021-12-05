package config.client.two.controller;

import config.client.two.pojo.OrderProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The type Refresh controller.
 *
 */
@RestController
@RequestMapping("refresh")
@RefreshScope
public class RefreshController {

    @Autowired
    private OrderProperties orderProperties;

    @Value(value = "${order.pay-timeout-seconds:1}")
    private Integer payTimeoutSeconds;

    /**
     * Test string.
     *
     * @return the string
     */
    @GetMapping("test")
    public String test() {
        return "payTimeoutSeconds:" + payTimeoutSeconds;
    }

    @GetMapping("test01")
    public String test01() {
        return orderProperties.toString();
    }
}