package AuD.template.project.spring_learn.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * 配置类A,并且带有包扫描功能
 */
@Configuration
@ComponentScan("AuD.template.project.spring_learn.beans")
public class ConfigA {
}
