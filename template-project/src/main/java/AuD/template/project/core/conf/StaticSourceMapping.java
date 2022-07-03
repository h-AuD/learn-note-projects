package AuD.template.project.core.conf;

import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Description: 静态资源访问设置,参考{@link WebMvcAutoConfiguration}
 *
 * @author AuD/胡钊
 * @ClassName StaticSourceMapping
 * @date 2021/7/13 10:23
 * @Version 1.0
 */
@Component
public class StaticSourceMapping implements WebMvcConfigurer {

    private String filePath = "E:\\";

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 将 URI为 "/material/**" 统统都映射在 "E:\" 下,即在"E:\"寻找资源
        // eg./material/123.pnj ===> E:\123.pnj
        // 现在有一个问题,请求pattern "/material/**" 可能具有业务含义的,却用来做静态资源访问,未免显得不妥
        // 一般来说,服务器是由nginx代理,可以由nginx代理处理静态资源,而不必由应用处理这类事情.
        //registry.addResourceHandler("/material/**").addResourceLocations("file:" + filePath);
    }

}
