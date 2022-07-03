package AuD.template.project.rabbitmq_learn;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DeliverCallback;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

/**
 * Description: work queue case -- 单个队列,多个消费者模式(与simple queue模式几乎相似,区别在于:多个消费者连接一个队列,注意:一个队列里一个消息只能被消费一次)
 *
 * this case 有一个生产者 一个队列 多个(2个)消费者,并且消费者1的性能比2要好(通过thread.sleep来控制).
 * 1.当消费者没有设置"qos"(默认情况下),即没有设置"channel.basicQos(1)" & autoAck=true时,
 * - 消费者1和消费者2获取到的消息的数量是相同的,一个是消费奇数号消息,一个是偶数
 * - 消费者1和消费者2获取到的消息内容是不同的,说明同一个消息只能被一个消费者获取
 * 但是这样其实是不合理的,因为消费者1的性能比较好,应该可以处理更多的消息
 *
 * RabbitMQ默认将消息顺序发送给下一个消费者,这样,每个消费者会得到相同数量的消息.即轮询(round-robin)分发消息
 * -- 怎样才能做到按照每个消费者的能力分配消息呢?联合使用 Qos 和 Acknowledge 就可以做到
 * 通过basicQos方法设置了当前信道最大预获取(prefetch)消息数量为1,
 * 消息从队列异步推送给消费者,消费者的 ack 也是异步发送给队列,
 * 从队列的视角去看,总是会有一批消息已推送但尚未获得 ack 确认,
 * Qos 的 prefetchCount 参数就是用来限制这批未确认消息数量的。
 * 设为1时,队列只有在收到消费者发回的上一条消息 ack 确认后,才会向该消费者发送下一条消息。
 * prefetchCount 的默认值为0,即没有限制，队列会将所有消息尽快发给消费者
 *
 * 2个概念
 * - 轮询分发:
 * 使用任务队列的优点之一就是可以轻易的并行工作.如果我们积压了好多工作,我们可以通过增加工作者(消费者)来解决这一问题,使得系统的伸缩性更加容易.
 * 在默认情况下,RabbitMQ将逐个发送消息到在序列中的下一个消费者(而不考虑每个任务的时长等等,且是提前一次性分配,并非一个一个分配).
 * 平均每个消费者获得相同数量的消息.这种方式分发消息机制称为Round-Robin(轮询)。
 * - 公平分发:
 * 虽然上面的分配法方式也还行,但是有个问题就是,比如,现在有2个消费者,所有的奇数的消息都是繁忙的,而偶数则是轻松的.
 * 按照轮询的方式,奇数的任务交给了第一个消费者,所以一直在忙个不停.偶数的任务交给另一个消费者,则立即完成任务,然后闲得不行.
 * 而RabbitMQ则是不了解这些的.这是因为当消息进入队列,RabbitMQ就会分派消息.它不看消费者为应答的数目,只是盲目的将消息发给轮询指定的消费者。
 *
 * 为了解决这个问题,我们使用basicQos( prefetchCount = 1)方法,来限制RabbitMQ只发不超过1条的消息给同一个消费者。
 * 当消息处理完毕后,有了反馈,才会进行第二次发送。
 * 还有一点需要注意,使用公平分发,必须关闭自动应答,改为手动应答
 * @author AuD/胡钊
 * @ClassName _2_WorkQueueCase
 * @date 2022/1/4 10:28
 * @Version 1.0
 */
public class _2_WorkQueueCase extends RabbitMQ{


    private final static String WORK_QUEUE = "work_queue";


    /* ===============================================================
     * 对照实验如下:
     * 1.先启动生产者,再依次启动消费者(先1后2)
     * - (合理流程应该是先启动消费者,再启动生产者,因为消费者处理监听状态)
     * - 先有一批消息存在队列"work_queue"中,观察消费者1和消费者2处理情况
     * 2.先启动消费者,再启动生产者,观察消费者1和消费者2处理情况
     * ===============================================================*/

    /**
     * 发送消息
     */
    public void sendMsg(){
        try(Connection connection = connectionFactory.newConnection(); Channel channel = connection.createChannel()) {
            channel.queueDeclare(WORK_QUEUE, true, false, false, null);
            // 消息内容
            String message = "Hello World";
            for (int i = 0; i < 10; i++) {
                String sendMessage = message+"_"+i;
                // 推送消息
                channel.basicPublish("", WORK_QUEUE, null, sendMessage.getBytes());
                System.out.println("Sent '" + sendMessage + "'");
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }


    /**
     * 消费者1
     */
    public void consumer1(){
        doConsumer(1);
    }

    /**
     * 消费者2
     */
    public void consumer2(){
        doConsumer(2);
    }


    /**
     * 消费消息
     * @param sleepTime
     */
    public void doConsumer(int sleepTime){
        try(Connection connection = connectionFactory.newConnection(); Channel channel = connection.createChannel()) {
            // 声明（创建）队列 === 队列名称,是否持久化,是否独占,是否自动del,队列其他参数
            //channel.queueDeclare(WORK_QUEUE, true, false, false, null);

            // 设置了当前信道最大预获取(prefetch)消息数量
            //channel.basicQos(1);

            // 接收消息的回调方法,用于get message以及ACK
            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                try {
                    String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
                    System.out.println(" Received_2 '" + message + "'");
                    // 模拟处理消息所耗费的时间
                    TimeUnit.SECONDS.sleep(sleepTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally {
                    // 手动确认消息已接收
                    //channel.basicAck(delivery.getEnvelope().getDeliveryTag(),false);
                }
            };
            System.out.println(" ------------------- Waiting for messages --------------------");
            while (true){
                // 监听队列 & 消费消息
                channel.basicConsume(WORK_QUEUE, true, deliverCallback, consumerTag -> { });
                // 需要手动确认消息
                //channel.basicConsume(WORK_QUEUE, false, deliverCallback, consumerTag -> { });
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


}
