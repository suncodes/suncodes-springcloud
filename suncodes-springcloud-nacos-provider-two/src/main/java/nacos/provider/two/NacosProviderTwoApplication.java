package nacos.provider.two;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 在使用 Nacos 的时候，必须自己从官网下载软件包，之后进行运行，相当于运行了 Nacos Server
 */
@SpringBootApplication
public class NacosProviderTwoApplication {
    public static void main(String[] args) {
        SpringApplication.run(NacosProviderTwoApplication.class, args);
    }
}
