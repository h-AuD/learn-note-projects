package AuD.template.project.core.function.schedule;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.annotation.ScheduledAnnotationBeanPostProcessor;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

/**
 * Description: spring 定时任务规则验证(job是否并行执行? OR 同一个任务/schedule_function在未执行完毕,并且满足执行条件下,是否会再一次执行?).   <br>
 * -- this class 采用的内部类配置,其中内部类可以是static、private、protected(具体参见{@code ConfigurationClassParser#processMemberClasses}).  <br>
 *
 * @author AuD/胡钊
 * @ClassName ScheduleTaskSerialExecExpGroup
 * @date 2021/12/1 9:43
 * @Version 1.0
 */
@Configuration
public class ScheduleTaskSerialExecExpGroup {

    /**
     * spring提供的一个扩展接口{@link SchedulingConfigurer},用于配置定时任务(eg.设置异步执行).    <br>
     * <br>
     *
     * spring Schedule定时任务执行默认是采取串行执行,eg:有2个schedule function,都设置在相同的时间点执行,默认是先执行其中一个后,再执行下一个. <br>
     * -- 其中schedule_task执行的顺序是如何控制的?   <br>
     * -- 假如上述2个schedule_task分别在同一个class中 和 在不用的class中,执行的效果是一样的吗?  <br>
     * ====> 上述问题的答案在{@link ScheduledAnnotationBeanPostProcessor}中,即通过注解{@link EnableScheduling}import的组件.  <br>
     * <br>
     *
     * 设计spring定时任务的对照组实验({@link ScheduleTaskSerialExecExpGroup_1} & {@link ScheduleTaskSerialExecExpGroup_2}):  <br>
     * group_1有如下测试内容: <br>
     * 1.在固定时间点执行的schedule_task1 & schedule_task1(自身对比).  <br>
     * 2.以某个固定频率执行的schedule_task,该task执行时间大于设置的频率.  <br>
     * group_2有如下测试内容: <br>
     * 1.在固定时间点执行的schedule_task(group_1_1对比).   <br>
     * ====> 上述实验都需要分别在{@link ScheduleAsyncExecuteConfig}配置生效和不生效的场景下执行.    <br>
     * <br>
     *
     * result:  <br>
     * 默认情况下:每个schedule是串行执行的,但是可以通过扩展{@code SchedulingConfigurer}来设置异步执行,但是每个schedule_task自身始终都是串行执行.  <br>
     * -- 说明:在设置并行场景下,某个schedule设置为固定时间频率执行(eg.每5min执行一次),但是该方法执行时间超过设置的时间频率(eg.方法执行需要耗时10min),
     * 那么方法在没有执行完成,是不会进入下一次执行.   <br>
     *
     */
    @Configuration
    protected static class ScheduleAsyncExecuteConfig implements SchedulingConfigurer{

        @Override
        public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
            ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
            // ==== 设置线程池线程数量
            threadPoolTaskScheduler.setPoolSize(10);
            // ==== 设置线程名称前缀
            threadPoolTaskScheduler.setThreadNamePrefix("scheduled-task-pool-");
            threadPoolTaskScheduler.initialize();
            scheduledTaskRegistrar.setTaskScheduler(threadPoolTaskScheduler);
        }
    }

    /* ======================================== 分割线 ================================= */

    /**
     * Description:实验对照组(experiment control group). <br>
     * -- 验证spring定时任务执行情况(eg.是否为串行执行)  <br>
     */
    //@Component
    private class ScheduleTaskSerialExecExpGroup_1 {

        /**
         * 实验对照组1_编号1:  <br>
         * 1.自身对比 ==== group_1_No.1 & group_1_No.2都设置了相同的执行点,分别观察在 串行 & 并行 情况下的执行过程.    <br>
         * 2.外部对比 ==== group_1_No.1 & group_2_No.1都设置了相同的执行点,分别观察在 串行 & 并行 情况下的执行过程.    <br>
         */
        @Scheduled(cron = "${schedule.task.serial.experiment.control.group.fixed.timer}")
        public void experimentControlGroup1No1() throws InterruptedException {
            System.out.println(LocalDateTime.now() + "experiment_group_1_No.1 beginning...(currentThreadName:" + Thread.currentThread().getName() + ")");
            // === 模拟业务执行耗时(5s)
            TimeUnit.SECONDS.sleep(5);
            System.out.println(LocalDateTime.now() + "experiment_group_1_No.1 end...(currentThreadName:" + Thread.currentThread().getName() + ")");
        }


        /**
         * 实验对照组1_编号2:
         */
        @Scheduled(cron = "${schedule.task.serial.experiment.control.group.fixed.timer}")
        public void experimentControlGroup1No2() throws InterruptedException {
            System.out.println(LocalDateTime.now() + "experiment_group_1_No.1 beginning...(currentThreadName:" + Thread.currentThread().getName() + ")");
            // === 模拟业务执行耗时(10s)
            TimeUnit.SECONDS.sleep(10);
            System.out.println(LocalDateTime.now() + "experiment_group_1_No.1 end...(currentThreadName:" + Thread.currentThread().getName() + ")");
        }

        /**
         * 固定时间频率(1s)下执行情况. <br>
         * 方法执行时间设置为3s,在并行场景下,验证是否会被重复执行(eg.第一个次进入该方法,1s后是否又会进入该方法?).   <br>
         * ---------
         * result:每个"schedule"方法,都是串行执行,i.e.当前方法执行完毕后,才会进入下一次.
         */
        @Scheduled(cron = "${schedule.task.serial.experiment.control.group.fre.timer}")
        public void experimentFixedFrequencyExeByPerSecond() throws InterruptedException {
            System.out.println("task1-testFixedTimer beginning......" + System.currentTimeMillis() + "  " + Thread.currentThread().getName());
            // === 模拟业务执行耗时(3s)
            TimeUnit.SECONDS.sleep(3);
            System.out.println("task1-testFixedTimer end......" + System.currentTimeMillis() + "  " + Thread.currentThread().getName());
        }
    }


    /**
     * Description:实验对照组(experiment control group). <br>
     * -- 用于做对比实验.  <br>
     */
    //@Component
    private class ScheduleTaskSerialExecExpGroup_2 {
        /**
         * 实验对照组2_编号1:
         */
        @Scheduled(cron = "${schedule.task.serial.experiment.control.group.fixed.timer}")
        public void experimentControlGroup2No1() throws InterruptedException {
            System.out.println(LocalDateTime.now() + "experiment_group_2_No.1 beginning...(currentThreadName:" + Thread.currentThread().getName() + ")");
            // === 模拟业务执行耗时(5s)
            TimeUnit.SECONDS.sleep(5);
            System.out.println(LocalDateTime.now() + "experiment_group_2_No.1 end...(currentThreadName:" + Thread.currentThread().getName() + ")");
        }
    }

}
