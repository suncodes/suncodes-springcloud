package rabbitmq.spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import rabbitmq.spring.service.RabbitMQService;

@RestController
public class RabbitMQController {

    @Autowired
    private RabbitMQService rabbitMQService;

    @GetMapping("/tut1Send")
    public String tut1Send() {
        return rabbitMQService.tut1Send();
    }

    @GetMapping("/tut2Send")
    public String tut2Send() {
        return rabbitMQService.tut2Send();
    }

    @GetMapping("/tut3Send")
    public String tut3Send() {
        return rabbitMQService.tut3Send();
    }

    @GetMapping("/tut4Send")
    public String tut4Send() {
        return rabbitMQService.tut4Send();
    }

    @GetMapping("/tut5Send")
    public String tut5Send() {
        return rabbitMQService.tut5Send();
    }

    @GetMapping("/tut6Send")
    public String tut6Send() {
        return rabbitMQService.tut6Send();
    }
}
