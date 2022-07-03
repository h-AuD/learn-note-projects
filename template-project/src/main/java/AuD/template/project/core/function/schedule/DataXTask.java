package AuD.template.project.core.function.schedule;

import AuD.template.project.core.function.datax.DataXJobHandle;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

/**
 * Description: datax job 处理定时任务. <br>
 *
 * <p>
 *     PS: !!! 定时任务组件,只需要配置相关定时配置即可,关于具体执行逻辑,推荐使用 !!! <br>
 * </p>
 *
 * @author AuD/胡钊
 * @ClassName DataXTask
 * @date 2021/7/7 13:43
 * @Version 1.0
 */
@Slf4j
@Component
public class DataXTask {


    @Autowired
    private DataXJobHandle dataXJobHandle;


    /** ============= 按照不同时间频率执行的job ===================== */
    /**
     * 每天早上5点执行,每隔一个小时执行一次
     *
     * @throws IOException
     */
    @Scheduled(cron = "0 0 05/1 * * ?")
    public void handle4hours() throws IOException {

        dataXJobHandle.handle(new File(""));

    }

    /**
     * 每天早上5点30分执行,每日执行一次
     * @throws IOException
     */
    @Scheduled(cron = "0 30 05 */1 * ?")
    public void handle4day() throws IOException {
        dataXJobHandle.handle(new File(""));
    }


}
