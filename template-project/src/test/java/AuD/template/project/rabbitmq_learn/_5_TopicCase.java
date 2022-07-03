package AuD.template.project.rabbitmq_learn;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DeliverCallback;

import java.nio.charset.StandardCharsets;

/**
 * Description: 话题模式,与路由模式区别在于,topic支持routKey匹配符
 *
 * @author AuD/胡钊
 * @ClassName _5_TopicCase
 * @date 2022/1/4 11:28
 * @Version 1.0
 */
public class _5_TopicCase extends RabbitMQ{

    /** 队列1名称 */
    private final static String QUEUE_TOPIC_1 = "queue_topic_1";
    /** 队列2名称 */
    private final static String QUEUE_TOPIC_2 = "queue_topic_2";
    /** 交换机名称 */
    private final static String EXCHANGE_NAME = "exchange_topic";


    /**
     * 发送消息
     */
    public void sendMsg(){

        try(Connection connection = connectionFactory.newConnection(); Channel channel = connection.createChannel()) {
            // 声明交换机,其逻辑/原理与声明队列类似,有多个重载方法,比如,是否持久化、是否自动删除、额外属性参数等等.
            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);
            // 发布消息,使用不同的路由key
            channel.basicPublish(EXCHANGE_NAME,"routeKey.routing", null, "Hello World routing".getBytes());
            channel.basicPublish(EXCHANGE_NAME,"routing", null, "Hello World routing".getBytes());
        }catch (Exception e){

        }

    }

    /**
     * 消费消息(routKey1)
     */
    public void consumer1(){
        try(Connection connection = connectionFactory.newConnection(); Channel channel = connection.createChannel()) {
            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);
            // 声明（创建）队列 === 队列名称,是否持久化,是否独占,是否自动del,队列其他参数
            channel.queueDeclare(QUEUE_TOPIC_1, false, false, false, null);
            // 将队列绑定到交换机,并指定routKey
            channel.queueBind(QUEUE_TOPIC_1, EXCHANGE_NAME,"routeKey.*");
            // 接收消息的回调方法,用于get message以及ACK
            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
                System.out.println(" [x] Received '" + message + "'");
            };
            System.out.println(" ------------------- Waiting for messages --------------------");
            while (true){
                // 监听队列 & 消费消息
                channel.basicConsume(QUEUE_TOPIC_1, true, deliverCallback, consumerTag -> { });
            }
        }catch (Exception e){

        }
    }


    /**
     * 消费消息(routKey2)
     */
    public void consumer2(){
        try(Connection connection = connectionFactory.newConnection(); Channel channel = connection.createChannel()) {
            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);
            // 声明（创建）队列 === 队列名称,是否持久化,是否独占,是否自动del,队列其他参数
            channel.queueDeclare(QUEUE_TOPIC_2, false, false, false, null);
            // 将队列绑定到交换机,并指定routKey
            channel.queueBind(QUEUE_TOPIC_2, EXCHANGE_NAME,"*.*");
            // 接收消息的回调方法,用于get message以及ACK
            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
                System.out.println(" [x] Received '" + message + "'");
            };
            System.out.println(" ------------------- Waiting for messages --------------------");
            while (true){
                // 监听队列 & 消费消息
                channel.basicConsume(QUEUE_TOPIC_2, true, deliverCallback, consumerTag -> { });
            }
        }catch (Exception e){

        }
    }


}
