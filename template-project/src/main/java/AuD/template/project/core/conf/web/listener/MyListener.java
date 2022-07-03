package AuD.template.project.core.conf.web.listener;

import org.springframework.boot.web.servlet.ServletComponentScan;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * Description: {@link WebListener} 注解是Web容器中的三大组件之一(@WebListener、@Servlet、@WebFilter)
 *
 * 参考{@link ServletComponentScan}
 *
 * @author AuD/胡钊
 * @ClassName MyListener
 * @date 2021/4/28 9:49
 * @Version 1.0
 */
@WebListener
public class MyListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {

    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}
