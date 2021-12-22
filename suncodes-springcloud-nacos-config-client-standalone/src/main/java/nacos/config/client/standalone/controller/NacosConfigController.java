package nacos.config.client.standalone.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NacosConfigController {

    @Autowired
    private ApplicationContext applicationContext;

    @Value("${user.name:default}")
    private String userName;

    @Value("${user.age:default}")
    private String userAge;

    @GetMapping("/echo")
    public String echo() {
        String s = "user name :" + userName + "; age: " + userAge;
        System.err.println(s);

        String userName1 = applicationContext.getEnvironment().getProperty("user.name");
        String userAge1 = applicationContext.getEnvironment().getProperty("user.age");
        System.out.println(userName1);
        System.out.println(userAge1);

        System.out.println("========================");
        return s;
    }
}
