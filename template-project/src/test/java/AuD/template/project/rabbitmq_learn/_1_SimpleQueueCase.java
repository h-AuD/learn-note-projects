package AuD.template.project.rabbitmq_learn;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DeliverCallback;

import java.nio.charset.StandardCharsets;

/**
 * Description: 基于队列的工作模式.
 * 1.simple queue -- 简单队列模型:一个队列,一个消费者(生产者可以多个,无所谓的).
 * 2.work queue -- 工作队列模式:一个队列,多个消费者(生产者可以多个,无所谓的).
 *
 * PS:在rabbitMQ中,一个队列里的msg只会被消费一次,通常一个队列绑定一个消费者即可
 *
 * @author AuD/胡钊
 * @ClassName _1_SimpleQueueCase
 * @date 2022/1/4 9:44
 * @Version 1.0
 */
public class _1_SimpleQueueCase extends RabbitMQ {

    /**
     * 队列名称
     */
    private final static String QUEUE_NAME = "simple_queue";



    /**
     * 生产者,发送消息.
     * 操作步骤:
     * 1.资源创建(eg.connection、channel)推荐使用try...catch..(因为{@link Connection} & {@link Channel}都继承{@link AutoCloseable})
     * 2.发布消息
     *
     */
    public void sendMsg(){
        // 创建连接,每次call{@link ConnectionFactory#newConnection()},返回的都是new对象
        // 从连接中创建通道,每次都是新的对象
        try(Connection connection = connectionFactory.newConnection(); Channel channel = connection.createChannel()) {

            // 声明(创建)队列 === 队列名称,是否持久化,是否独占,是否自动del,队列其他参数
            channel.queueDeclare(QUEUE_NAME, true, false, false, null);
            // 消息内容
            String message = "Hello World!";

            for (int i = 0; i < 10; i++) {
                message = message+"_"+i;
                /* ====================================================================
                 * 推送/发布消息到队列
                 * API参数说明:
                 * exchange -- 交换机名称,此处使用默认交换机("")
                 * routingKey -- 路由key,此处的key即是队列名
                 * props -- msg属性,eg.设置消息过期时间
                 * body -- msg内容
                 * 由此可见,发布消息与队列无关,消息先到达exchange,由exchange根据routingKey路由到各个queue中
                 * ====================================================================*/
                channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
                System.out.println("Sent '" + message + "'");
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 消费者,消费消息
     */
    public void consumer(){
        try(Connection connection = connectionFactory.newConnection(); Channel channel = connection.createChannel()) {
            // 声明（创建）队列 === 队列名称,是否持久化,是否独占,是否自动del,队列其他参数
            channel.queueDeclare(QUEUE_NAME, true, false, false, null);
            // 接收消息的回调方法,用于get message以及ACK
            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
                System.out.println(" [x] Received '" + message + "'");
            };
            System.out.println(" ------------------- Waiting for messages --------------------");
            while (true){
                /* ==================================================================================
                 * 监听队列 & 消费消息
                 * API参数说明:
                 * queue -- 需要监听的队列名称
                 * autoAck -- 表示是否自动确认已接收msg,true(Y),false(N).当为false时,需要手动ack
                 * deliverCallback -- 接收msg回调接口(函数式接口),这里可以拿到msg
                 * ==================================================================================*/
                channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> { });
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }



}
