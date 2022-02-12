package rabbitmq.spring.service;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public String test() {
//        DirectExchange directExchange = new DirectExchange("x.test");
//        Queue queue = new Queue("x.test");
//
//        rabbitTemplate.set
//
////        Binding binding = new Binding()
//        rabbitTemplate.convertAndSend();
    }
}
