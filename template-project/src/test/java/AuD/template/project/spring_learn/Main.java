package AuD.template.project.spring_learn;

import AuD.template.project.spring_learn.config.ConfigA;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 *
 */
public class Main {

    public static void main(String[] args) {

        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(ConfigA.class);

    }

}
