package AuD.template.project.core.conf.properties;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Description: 基本属性配置,即通过{@link Value}设置属性值,包含simple、List、Map注入。<br>
 *
 * @author AuD/胡钊
 * @ClassName PropertyConfig
 * @date 2021/4/14 12:32
 * @Version 1.0
 */
@Component
@Data
public class BasicPropertyConfig {

    /**
     * 1.{@link Value} 基本用法 == @Value("${config-name}")
     */
    @Value("${basic.pro.config.name}")
    private String name;

    @Value("${basic.pro.config.age}")
    private Integer age;

    /**
     * 2.集合配置 == @Value("#{'${key}'.split(',')}").  <br>
     * eg.配置文件(spring-environment)中设置如下属性: <br>
     * - basic.pro.config.list=1,2,3,4 <br>
     * - 其中","是自定义的风格发,对应上述表达式中的"split"。<br>
     */
    @Value("#{'${basic.pro.config.list}'.split(',')}")
    private List<String> list;

    /**
     * 3.map配置 == @Value("#{${key}}")   <br>
     * eg.配置文件(spring-environment)中设置如下属性: <br>
     * - basic.pro.config.map={"key1":"value1","key2":18,"key3":{"ikey1":"iv1"}}. <br>
     *
     */
    @Value("#{${basic.pro.config.map}}")
    private Map<String,Object> map;

    @Override
    public String toString() {
        return "BasicPropertyConfig{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", list=" + list +
                ", map=" + map +
                '}';
    }
}
