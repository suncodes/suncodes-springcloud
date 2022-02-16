package rabbitmq.spring.service;

import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class RabbitMQService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    @Qualifier("hello")
    private Queue queue;

    @Autowired
    @Qualifier("tutHello")
    private Queue tutHello;

    @Autowired
    private FanoutExchange fanout;

    // =======================================================

    /**
     * 例1
     * @return
     */
    public String tut1Send() {
        String message = "Hello World!";
        rabbitTemplate.convertAndSend(queue.getName(), message);
        String printStr = " [x] Sent '" + message + "'";
        System.out.println(printStr);
        return printStr;
    }

    @RabbitListener(queues = "hello")
    @RabbitHandler
    public void tut1Rev(String in) {
        System.out.println(" [x] Received '" + in + "'");
    }

    // ===========================================================

    /**
     * 例2
     * @return
     */
    public String tut2Send() {
        String message = "Hello" + "-" + UUID.randomUUID().toString();
        rabbitTemplate.convertAndSend(tutHello.getName(), message);
        System.out.println(" [x] Sent '" + message + "'");
        return message;
    }

    @RabbitListener(queues = "tut.hello")
    @RabbitHandler
    public void tut2Rev1(String in) {
        System.out.println(" [x-1] Received '" + in + "'");
    }

    @RabbitListener(queues = "tut.hello")
    @RabbitHandler
    public void tut2Rev2(String in) {
        System.out.println(" [x-2] Received '" + in + "'");
    }


    // ===========================================================
    /**
     * 例2
     * @return
     */
    public String tut3Send() {
        String message = "Hello" + "-" + UUID.randomUUID().toString();
        rabbitTemplate.convertAndSend(fanout.getName(), "", message);
        System.out.println(" [x] Sent '" + message + "'");
        return message;
    }

    @RabbitListener(queues = "#{autoDeleteQueue1.name}")
    @RabbitHandler
    public void tut3Rev1(String in) {
        System.out.println(" [x-1] Received '" + in + "'");
    }

    @RabbitListener(queues = "#{autoDeleteQueue2.name}")
    @RabbitHandler
    public void tut3Rev2(String in) {
        System.out.println(" [x-2] Received '" + in + "'");
    }

    // =========================================================

    /**
     * 例子
     */
    @Autowired
    @Qualifier("tutDirect")
    private DirectExchange tutDirect;

    private final String[] keys = {"orange", "black", "green"};

    private static int tut4SendInt = 0;

    /**
     * 例2
     * @return
     */
    public String tut4Send() {

        if (tut4SendInt >= keys.length - 1) {
            tut4SendInt = 0;
        } else {
            tut4SendInt ++;
        }
        String key = keys[tut4SendInt];
        System.out.println(key);
        String message = "Hello" + "-" + UUID.randomUUID().toString();
        rabbitTemplate.convertAndSend(tutDirect.getName(), key, message);
        System.out.println(" [x] Sent '" + message + "'");
        return message;
    }

    @RabbitListener(queues = "#{autoDeleteQueue3.name}")
    @RabbitHandler
    public void tut4Rev1(String in) {
        System.out.println(" [x-1] Received '" + in + "'");
    }

    @RabbitListener(queues = "#{autoDeleteQueue4.name}")
    @RabbitHandler
    public void tut4Rev2(String in) {
        System.out.println(" [x-2] Received '" + in + "'");
    }

    // ===================================================
    @Autowired
    private TopicExchange topic;

    private final String[] keys1 = {"quick.orange.rabbit", "lazy.orange.elephant", "quick.orange.fox",
            "lazy.brown.fox", "lazy.pink.rabbit", "quick.brown.fox"};

    private static int tut5SendInt = 0;

    /**
     * 例2
     * @return
     */
    public String tut5Send() {

        if (tut5SendInt >= keys1.length - 1) {
            tut5SendInt = 0;
        } else {
            tut5SendInt ++;
        }
        String key = keys1[tut5SendInt];
        System.out.println(key);
        String message = "Hello" + "-" + UUID.randomUUID().toString();
        rabbitTemplate.convertAndSend(topic.getName(), key, message);
        System.out.println(" [x] Send '" + message + "'");
        return message;
    }

    @RabbitListener(queues = "#{autoDeleteQueue5.name}")
    @RabbitHandler
    public void tut5Rev1(String in) {
        System.out.println(" [x-1] Received '" + in + "'");
    }

    @RabbitListener(queues = "#{autoDeleteQueue6.name}")
    @RabbitHandler
    public void tut5Rev2(String in) {
        System.out.println(" [x-2] Received '" + in + "'");
    }

    // =========================================================

    @Autowired
    @Qualifier("tutRpc")
    private DirectExchange tutRpc;

    private int start = 0;

    public String tut6Send() {
        System.out.println(" [x] Requesting fib(" + start + ")");
        Integer response = (Integer) rabbitTemplate.convertSendAndReceive(tutRpc.getName(), "rpc", start++);
        System.out.println(" [.] Got '" + response + "'");
        return String.valueOf(response);
    }

    @RabbitListener(queues = "tut.rpc.requests")
    // @SendTo("tut.rpc.replies") used when the client doesn't set replyTo.
    public int fibonacci(int n) {
        System.out.println(" [x] Received request for " + n);
        int result = fib(n);
        System.out.println(" [.] Returned " + result);
        return result;
    }

    public int fib(int n) {
        return n == 0 ? 0 : n == 1 ? 1 : (fib(n - 1) + fib(n - 2));
    }
}
