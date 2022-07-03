package AuD.template.project.rabbitmq_learn;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DeliverCallback;

import java.nio.charset.StandardCharsets;

/**
 * Description: 路由模式
 *
 * @author AuD/胡钊
 * @ClassName _4_RoutingCase
 * @date 2022/1/4 11:04
 * @Version 1.0
 */
public class _4_RoutingCase extends RabbitMQ{

    /** 场景(路由)1队列名称 */
    private final static String QUEUE_DIRECT_1 = "queue_direct_1";
    /** 场景(路由)2队列名称 */
    private final static String QUEUE_DIRECT_2 = "queue_direct_2";
    /** 交换机名称 */
    private final static String EXCHANGE_NAME = "exchange_direct";


    /**
     * 发送消息
     */
    public void sendMsg(){

        try(Connection connection = connectionFactory.newConnection(); Channel channel = connection.createChannel()) {
            // 声明交换机,其逻辑/原理与声明队列类似,有多个重载方法,比如,是否持久化、是否自动删除、额外属性参数等等.
            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
            // 使用不同的路由key发布消息
            channel.basicPublish(EXCHANGE_NAME,"delete", null, "delete...".getBytes());
            channel.basicPublish(EXCHANGE_NAME,"insert", null, "insert...".getBytes());
            channel.basicPublish(EXCHANGE_NAME,"update", null, "update...".getBytes());
        }catch (Exception e){
            e.printStackTrace();
        }

    }


    /**
     * 消费消息(routKey1)
     */
    public void consumer1(){
        try(Connection connection = connectionFactory.newConnection(); Channel channel = connection.createChannel()) {
            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
            // 声明（创建）队列 === 队列名称,是否持久化,是否独占,是否自动del,队列其他参数
            channel.queueDeclare(QUEUE_DIRECT_1, false, false, false, null);
            // 将队列绑定到交换机,并指定routKey
            channel.queueBind(QUEUE_DIRECT_1,EXCHANGE_NAME,"delete");
            channel.queueBind(QUEUE_DIRECT_2,EXCHANGE_NAME,"update");
            // 接收消息的回调方法,用于get message以及ACK
            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
                System.out.println(" [x] Received '" + message + "'");
            };
            System.out.println(" ------------------- Waiting for messages --------------------");
            while (true){
                // 监听队列 & 消费消息
                channel.basicConsume(QUEUE_DIRECT_1, true, deliverCallback, consumerTag -> { });
            }
        }catch (Exception e){

        }
    }


    /**
     * 消费消息(routKey2)
     */
    public void consumer2(){
        try(Connection connection = connectionFactory.newConnection(); Channel channel = connection.createChannel()) {
            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
            // 声明（创建）队列 === 队列名称,是否持久化,是否独占,是否自动del,队列其他参数
            channel.queueDeclare(QUEUE_DIRECT_2, false, false, false, null);
            // 将队列绑定到交换机,并指定routKey
            channel.queueBind(QUEUE_DIRECT_2,EXCHANGE_NAME,"delete");
            channel.queueBind(QUEUE_DIRECT_2,EXCHANGE_NAME,"insert");
            channel.queueBind(QUEUE_DIRECT_2,EXCHANGE_NAME,"update");
            // 接收消息的回调方法,用于get message以及ACK
            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
                System.out.println(" [x] Received '" + message + "'");
            };
            System.out.println(" ------------------- Waiting for messages --------------------");
            while (true){
                // 监听队列 & 消费消息
                channel.basicConsume(QUEUE_DIRECT_2, true, deliverCallback, consumerTag -> { });
            }
        }catch (Exception e){

        }
    }




}
