package AuD.template.project.controller.rabbitmq;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

/**
 * Description: rabbitMq使用case,参见{@link RabbitAutoConfiguration}
 *
 * @author AuD/胡钊
 * @ClassName RabbitMqCaseController
 * @date 2021/12/27 13:32
 * @Version 1.0
 */
@RestController
@RequestMapping(value = "/rabbit")
public class RabbitMqCaseController {

    /**
     * 简单队列
     */
    private final static String SIMPLE_QUEUE ="simple_queue";

    @Autowired
    private RabbitTemplate rabbitTemplate;


    /* ==================== below is rabbitmq listener handler ========================== */

    /**
     *
     * @param hello
     * @throws InterruptedException
     */
    @RabbitListener(queues = SIMPLE_QUEUE)
    @RabbitHandler
    public void handle(String hello) throws InterruptedException {
        System.out.println("Re:"+hello+"  t->"+ LocalDateTime.now());
        // === 休眠5秒,用于模拟业务处理时间
        TimeUnit.SECONDS.sleep(5);
    }

    /**
     * rabbitmq相关bean配置,eg:queue、exchange、exchangeBind. <br>
     * 上述类型的配置主要用于声明,i.e.{@link Channel#queueDeclare(java.lang.String, boolean, boolean, boolean, java.util.Map)},
     * 声明的逻辑/代码参见{@link org.springframework.amqp.rabbit.core.RabbitAdmin}.  <br>
     *
     * note:queue、exchange、exchangeBind等声明,在代码层面上即显得臃肿,又很有必要 -- So,是否可以通过某种简单的配置(eg.environment)来完成. <br>
     */
    @Configuration
    class RabbitMqComponentConfig{

        /**
         * 声明一个简单队列,即单个消费者 --  this queue is in package "org.springframework.amqp.core"
         * @return
         */
        @Bean(value = SIMPLE_QUEUE)
        public Queue simpleQueue(){
            return new Queue(SIMPLE_QUEUE);
        }

    }


}
