package AuD.template.project;

import AuD.template.project.core.conf.properties.BasicPropertyConfig;
import AuD.template.project.core.conf.properties.CustomPropertiesConfig;
import AuD.template.project.service.UserBasicInfoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TemplateProjectApplicationTests {


    @Autowired
    private BasicPropertyConfig basicPropertyConfig;

    @Autowired
    private CustomPropertiesConfig customPropertiesConfig;

    @Autowired
    private UserBasicInfoService userBasicInfoService;

    @Test
    void contextLoads() {
    }

    /**
     * 测试对象属性注入,参见{@link BasicPropertyConfig} & {@link CustomPropertiesConfig}
     */
    @Test
    void objFieldInjection_test(){
        System.out.println(basicPropertyConfig+"\n"+customPropertiesConfig);
    }

    /**
     * 测试事务回滚机制.{@link UserBasicInfoService}
     */
    @Test
    void transactionRollback_test(){
        userBasicInfoService.transactionRollback(null);
    }

}
