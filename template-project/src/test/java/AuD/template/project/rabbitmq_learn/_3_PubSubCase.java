package AuD.template.project.rabbitmq_learn;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DeliverCallback;

import java.nio.charset.StandardCharsets;

/**
 * Description: 订阅模式,关于模型强烈建议到官方文档(https://www.rabbitmq.com/getstarted.html)上看,这里就不做详细描述.
 *
 * 这个case中与上述case不同的是,需要设置交换机,以及队列需要绑定到交换机上
 *
 * @author AuD/胡钊
 * @ClassName _3_PubSubCase
 * @date 2022/1/4 10:44
 * @Version 1.0
 */
public class _3_PubSubCase extends RabbitMQ {

    /** 关注者1队列名称 */
    private final static String QUEUE_SUBSCRIBE_1 = "queue_subscribe_1";
    /** 关注者2队列名称 */
    private final static String QUEUE_SUBSCRIBE_2 = "queue_subscribe_2";
    /** 交换机名称 */
    private final static String EXCHANGE_NAME = "exchange_fanout";

    /* ===============================================================
     * 对照实验如下:
     * 1.消费者1和消费者采取相同的ACK,分别自动和手动
     * 2.消费者1和消费者采取不相同的ACK,即一个自动,另一个手动(可以忽略)
     * - 是否使用ACK,在"发布/订阅"模型下,是无所谓的,因为每个订阅者都有自己队列
     * ===============================================================*/

    /**
     * 发送消息
     */
    public void sendMsg(){

        try(Connection connection = connectionFactory.newConnection(); Channel channel = connection.createChannel()) {
            // 声明交换机,其逻辑/原理与声明队列类似,有多个重载方法,比如,是否持久化、是否自动删除、额外属性参数等等.
            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.FANOUT);
            // 消息内容
            String message = "Hello World Fanout!";
            for (int i = 0; i < 10; i++) {
                String sendMessage = message+"_"+i;
                // 推送消息,设置推送到指定交换机,路由key设置""
                channel.basicPublish(EXCHANGE_NAME,"", null, sendMessage.getBytes());
                System.out.println("Sent '" + sendMessage + "'");
            }
        }catch (Exception e){

        }

    }


    /**
     * 关注者1
     */
    public void consumer1(){
        doConsumer(QUEUE_SUBSCRIBE_1,true);
    }

    /**
     * 关注者2
     */
    public void consumer2(){
        doConsumer(QUEUE_SUBSCRIBE_2,false);
    }


    /**
     * 消费消息
     * @param queueName 绑定的队列名称
     * @param isAck 是否需要ACK
     */
    public void doConsumer(String queueName,boolean isAck){
        try(Connection connection = connectionFactory.newConnection(); Channel channel = connection.createChannel()) {
            // 声明（创建）队列 === 队列名称,是否持久化,是否独占,是否自动del,队列其他参数
            channel.queueDeclare(queueName, false, false, true, null);
            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.FANOUT);
            // 将队列绑定到交换机上
            channel.queueBind(queueName,EXCHANGE_NAME,"");

            // 设置了当前信道最大预获取(prefetch)消息数量 -- 同一时刻服务器只会发一条消息给消费者
            // 为了方便,也可以使用默认机制,即设置isAck=false
            if(isAck){
                channel.basicQos(1);
            }
            // 接收消息的回调方法,用于get message以及ACK
            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                try {
                    String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
                    System.out.println(" [x] Received '" + message + "'");
                }finally {
                    if(isAck){
                        // 手动确认消息已接收
                        channel.basicAck(delivery.getEnvelope().getDeliveryTag(),false);
                    }
                }
            };
            System.out.println(" ------------------- Waiting for messages --------------------");
            while (true){
                // 监听队列 & 消费消息
                if(isAck){
                    // 手动确认消息
                    channel.basicConsume(queueName, false, deliverCallback, consumerTag -> { });
                }else {
                    // 自动确认消息
                    channel.basicConsume(queueName, true, deliverCallback, consumerTag -> { });
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


}
