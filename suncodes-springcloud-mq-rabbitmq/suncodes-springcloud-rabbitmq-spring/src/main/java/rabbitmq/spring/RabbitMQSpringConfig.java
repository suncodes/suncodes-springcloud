package rabbitmq.spring;

import org.springframework.amqp.core.AnonymousQueue;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class RabbitMQSpringConfig {

    @Bean("hello")
    public Queue hello() {
        return new Queue("hello");
    }

    @Bean("tutHello")
    public Queue tutHello() {
        return new Queue("tut.hello");
    }

    @Bean
    public FanoutExchange fanout() {
        return new FanoutExchange("tut.fanout");
    }

    @Bean
    public Queue autoDeleteQueue1() {
        return new AnonymousQueue();
    }

    @Bean
    public Queue autoDeleteQueue2() {
        return new AnonymousQueue();
    }

    @Bean
    public Binding binding1(FanoutExchange fanout, Queue autoDeleteQueue1) {
        return BindingBuilder.bind(autoDeleteQueue1).to(fanout);
    }

    @Bean
    public Binding binding2(FanoutExchange fanout, Queue autoDeleteQueue2) {
        return BindingBuilder.bind(autoDeleteQueue2).to(fanout);
    }

    @Bean("tutDirect")
    @Primary
    public DirectExchange direct() {
        return new DirectExchange("tut.direct");
    }

    @Bean
    public Queue autoDeleteQueue3() {
        return new AnonymousQueue();
    }

    @Bean
    public Queue autoDeleteQueue4() {
        return new AnonymousQueue();
    }

    @Bean
    public Binding binding1a(DirectExchange direct, Queue autoDeleteQueue3) {
        return BindingBuilder.bind(autoDeleteQueue3).to(direct).with("orange");
    }

    @Bean
    public Binding binding1b(DirectExchange direct, Queue autoDeleteQueue3) {
        return BindingBuilder.bind(autoDeleteQueue3).to(direct).with("black");
    }

    @Bean
    public Binding binding2a(DirectExchange direct, Queue autoDeleteQueue4) {
        return BindingBuilder.bind(autoDeleteQueue4).to(direct).with("green");
    }

    @Bean
    public Binding binding2b(DirectExchange direct, Queue autoDeleteQueue4) {
        return BindingBuilder.bind(autoDeleteQueue4).to(direct).with("black");
    }

    @Bean
    public TopicExchange topic() {
        return new TopicExchange("tut.topic");
    }

    @Bean
    public Queue autoDeleteQueue5() {
        return new AnonymousQueue();
    }

    @Bean
    public Queue autoDeleteQueue6() {
        return new AnonymousQueue();
    }

    @Bean
    public Binding binding5a(TopicExchange topic, Queue autoDeleteQueue5) {
        return BindingBuilder.bind(autoDeleteQueue5).to(topic).with("*.orange.*");
    }

    @Bean
    public Binding binding5b(TopicExchange topic, Queue autoDeleteQueue6) {
        return BindingBuilder.bind(autoDeleteQueue6).to(topic).with("*.*.rabbit");
    }

    @Bean
    public Binding binding5aa(TopicExchange topic, Queue autoDeleteQueue6) {
        return BindingBuilder.bind(autoDeleteQueue6).to(topic).with("lazy.#");
    }

    @Bean("tutRpc")
    public DirectExchange tutRpc() {
        return new DirectExchange("tut.rpc");
    }

    @Bean
    public Queue tutRpcRequests() {
        return new Queue("tut.rpc.requests");
    }

    @Bean
    public Binding binding(@Qualifier("tutRpc") DirectExchange tutRpc, Queue tutRpcRequests) {
        return BindingBuilder.bind(tutRpcRequests).to(tutRpc).with("rpc");
    }
}
