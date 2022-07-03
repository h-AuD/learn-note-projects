package AuD.template.project;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@SpringBootApplication
// ==== 与MyBatisAutoConfiguration有所冲突,具体原因参加源码逻辑,个人推荐使用"@MapperScan",理由:忽略(主要见源码,其中的包扫描自定义过滤器)
@MapperScan("AuD.template.project.mapper")
/** "@EnableTransactionManagement" 对比 {@link org.springframework.boot.autoconfigure.transaction.TransactionAutoConfiguration} */
@EnableTransactionManagement
@ServletComponentScan
// 开启AOP,参考 "AopAutoConfiguration",当引入AOP-starter依赖后,下面的注解没有必要
//@EnableAspectJAutoProxy
@EnableScheduling   // 开启spring定时任务处理器
@EnableAsync    // 开启异步处理
public class TemplateProjectApplication {

    /**===================================================================================
     * {@link ServletComponentScan} 注解说明:
     *
     **==================================================================================*/

    public static void main(String[] args) {
        SpringApplication.run(TemplateProjectApplication.class, args);
    }

}
