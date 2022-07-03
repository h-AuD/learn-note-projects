package AuD.template.project.core.conf;

import AuD.template.project.core.function.schedule.ScheduleTaskSerialExecExpGroup;
import org.springframework.boot.autoconfigure.web.client.RestTemplateAutoConfiguration;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.client.RestTemplate;

/**
 * Description: 配置所需要的bean
 *
 * @author AuD/胡钊
 * @ClassName ConfigBeanForThisApplication
 * @date 2021/1/19 11:47
 * @Version 1.0
 */
@Configuration
public class BeanConfig {

    /**
     * 参见{@link RestTemplateAutoConfiguration}
     * <p> this autoConfiguration only config {@link RestTemplateBuilder},
     * 可以通过restTemplateBuilder构建自定义(eg.自定义构建属性)的{@link RestTemplate}对象. </br>
     *
     * @param restTemplateBuilder
     * @return
     */
    //@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    //@Bean
    public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder){
        /** =======================================================================================================
         * RestTemplate restTemplate = new RestTemplate();
         * restTemplate.getMessageConverters().forEach((httpMessageConverter -> {
         *  if(httpMessageConverter.getClass()== StringHttpMessageConverter.class){
         *      ((StringHttpMessageConverter) httpMessageConverter).setDefaultCharset(StandardCharsets.UTF_8);
         *   }
         * }));
         * return restTemplate;
         * 上述为构建 {@link RestTemplate}对象一般做法,i.e.直接new对象.
         * <p> 注意:上述方式创建的restTemplate对象中的消息转换器默认编码是ISO-xxx
         * =========================================================================================================*/
        return restTemplateBuilder.build();
    }

    /**
     * 定时任务(Schedule)异步执行配置. </br>
     * 具体详情参见{@link ScheduleTaskSerialExecExpGroup.ScheduleAsyncExecuteConfig}. <br>
     * <br>
     * 讨论:"SchedulingConfigurer"配置是放在某个配置类(eg.this_class:总配置类、通用配置类)下?
     * 还是单独开一个具体功能配置类(eg.ScheduleTaskSerialExecExpGroup.ScheduleAsyncExecuteConfig)?    <br>
     * -- 个人推荐单独开一个配置类,目的为了统一管理,因为SchedulingConfigurer就是用于服务定时任务,不具有通用性.    <br>
     *
     * @return SchedulingConfigurer
     */
    //@Bean
    public SchedulingConfigurer scheduleAsyncExecuteConfig(){
        // 线程池线程数量,推荐通过外部来设置,此处为了方便,所以才写死的(i.e.硬编码)
        final int POOL_SIZE = 10;
        return taskRegistrar -> {
            ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
            threadPoolTaskScheduler.setPoolSize(POOL_SIZE);
            threadPoolTaskScheduler.setThreadNamePrefix("scheduled-task-pool-");
            threadPoolTaskScheduler.initialize();
            taskRegistrar.setTaskScheduler(threadPoolTaskScheduler);
        };
    }


}
