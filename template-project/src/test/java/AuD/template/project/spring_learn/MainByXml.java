package AuD.template.project.spring_learn;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 一个spring应用的入口,通过xml配置来完成
 */
public class MainByXml {

    public static void main(String[] args) {
        /**
         * {@link ClassPathXmlApplicationContext}
         */
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("beans.xml");
    }

}
