import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class Send {

    private final static String QUEUE_NAME = "hello";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            String message = "Hello World!";

            // 设置自定义属性
            Map<String, Object> headerMap = new HashMap<>();
            headerMap.put("name", "sunchuizhe");
            AMQP.BasicProperties basicProperties = new AMQP.BasicProperties()
                    .builder()
                    .appId("1").headers(headerMap)
                    .build();

            channel.basicPublish("", QUEUE_NAME, basicProperties, message.getBytes(StandardCharsets.UTF_8));
            System.out.println(" [x] Sent '" + message + "'");
        }
    }
}
