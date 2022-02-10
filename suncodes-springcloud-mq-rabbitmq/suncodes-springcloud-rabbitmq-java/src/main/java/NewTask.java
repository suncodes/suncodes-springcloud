import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

import java.util.UUID;

/**
 * 如何运行？
 * （1）运行一个 Worker
 * （2）再运行一个 Worker
 * （3）之后运行 NewTask 进行发送消息，查看消费者消费情况
 * （4）之后再运行 NewTask 进行发送消息，查看消费者消费情况
 * （5）之后再再运行 NewTask 进行发送消息，查看消费者消费情况
 *
 */
public class NewTask {

    private static final String TASK_QUEUE_NAME = "task_queue";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.queueDeclare(TASK_QUEUE_NAME, true, false, false, null);

            // 为了每次发送的信息不一样，随机指定
            String string = UUID.randomUUID().toString();

            String message = string + "-lalala";

            // MessageProperties.PERSISTENT_TEXT_PLAIN 消息设置为持久化
            channel.basicPublish("", TASK_QUEUE_NAME,
                    MessageProperties.PERSISTENT_TEXT_PLAIN,
                    message.getBytes("UTF-8"));
            System.out.println(" [x] Sent '" + message + "'");
        }
    }

}
