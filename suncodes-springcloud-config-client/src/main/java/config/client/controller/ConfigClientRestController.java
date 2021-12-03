package config.client.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * http://localhost:3355/config
 * applicationName: microservicecloud-config-atguigu-dev a:a b: b c: default
 */
@RestController
public class ConfigClientRestController {

    @Value("${spring.application.name}")
    private String applicationName;

    @Value("${a:default}")
    private String a;

    @Value("${b:default}")
    private String b;

    @Value("${c:default}")
    private String c;

    @RequestMapping("/config")
    public String getConfig() {
        String str = "applicationName: " + applicationName + "\t a:" + a + "\t b: " + b + "\t c: " + c;
        System.out.println("******str: " + str);
        return str;
    }
}
 
