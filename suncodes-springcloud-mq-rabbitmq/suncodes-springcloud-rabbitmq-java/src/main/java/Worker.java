import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

/**
 * 如何运行？
 * （1）运行一个 Worker
 * （2）再运行一个 Worker
 * （3）之后运行 NewTask 进行发送消息，查看消费者消费情况
 * （4）之后再运行 NewTask 进行发送消息，查看消费者消费情况
 * （5）之后再再运行 NewTask 进行发送消息，查看消费者消费情况
 *
 */
public class Worker {

    private static final String TASK_QUEUE_NAME = "task_queue";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        final Connection connection = factory.newConnection();
        final Channel channel = connection.createChannel();

        channel.queueDeclare(TASK_QUEUE_NAME, true, false, false, null);
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        // 一次只接受一个未确认的消息
        // 可以理解为 不同消费者对于同一个队列的权重
        channel.basicQos(1);

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");

            System.out.println(" [x] Received '" + message + "'");
            try {
                // 模拟实际业务
                doWork(message);
            } finally {
                System.out.println(" [x] Done");
                // 确认消息，当消息没有被确认的时候，此线程不会再接收其他消息
                // 循环给多个消费者投递消息
                channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
            }
        };
        // 消息手动确认
        channel.basicConsume(TASK_QUEUE_NAME, false, deliverCallback, consumerTag -> { });
    }

    /**
     * 模拟实际业务
     * @param task
     */
    private static void doWork(String task) {

        for (char ch : task.toCharArray()) {
            if (ch == '-') {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException _ignored) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }
}

