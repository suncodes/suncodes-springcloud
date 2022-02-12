package rabbitmq.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RabbitMQSpringApplication {
    /**
     * 启动入口
     * @param args
     */
    public static void main(String[] args) {
        SpringApplication.run(RabbitMQSpringApplication.class, args);
    }
}
