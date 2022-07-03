package AuD.template.project.core.conf;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Description: jackson配置,配置序列化 & 反序列化中{@link LocalDateTime}类型数据设置. <br>
 *
 * @author AuD/胡钊
 * @ClassName JacksonDataConfig
 * @date 2021/9/24 12:24
 * @Version 1.0
 */
//@Configuration
public class JacksonDataConfig {

    /**==========================================================================================
     * jackson配置
     * 注:this config主要用于配置json日期格式化,适用于JDK8 {@link LocalDateTime}等对象.
     * - 但是其实这些配置没有必要,请参考{@link JacksonAutoConfiguration}.
     * ==========================================================================================*/




    /**
     * jackson默认不支持JDK8时间类型序列化和反序列化,即.对应序列化 {"ctime":"2021-11-11 11:11:11"}是不支持的(抛异常).  <br>
     * 解决方式: <br>
     * 1.注册模型{@link Module}对象(eg.this function). <br>
     * - 参考{@link JacksonAutoConfiguration}悉知,在配置{@link ObjectMapper},会从容器中查找Module类型的bean,然后注册到ObjectMapper中. <br>
     * 2.call API "objectMapper.findAndRegisterModules()".
     * 3.实现{@link Jackson2ObjectMapperBuilderCustomizer}接口,并且注册到spring容器中,
     * 通过参数{@link Jackson2ObjectMapperBuilder}设置findModulesViaServiceLoader=true <br>
     *
     * -- 其中方式 1 & 3 基于spring环境完成的.并且它们仅仅让ObjectMapper支持JDK8时间类型而已,并没有提供格式化. <br>
     * -- eg:序列化LocalDateTime的结果是"2021-11-11T11:11:11",即类似call toString(),并且反序列化仅仅支持相同的格式,对于"2021-11-11 11:11:11"会反序列化失败. <br>
     *
     * --------------but ---------------- <br>
     * 经测试,发现:在实体类属性上添加{@link JsonFormat},eg: "@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") private LocalDateTime ctime;"
     * 可以实现序列化和反序列化的需求.即.就算没有配置"javaTimeModule",实体属性上添加注解JsonFormat也可以完成相应的需求,对JDK8时间类型的支持和格式化. <br>
     * -- 注:如果属性类型为{@link LocalDateTime},并且配置@JsonFormat(pattern = "yyyy-MM-dd"),则会抛异常(from InvalidFormatException),但是异常信息确实"不能解析LocalDateTime".
     * 关于这点与mybatis不同的,mybatis可以自动实现datetime --> date 转换,即DB字段类型为datetime,而实体类型为LocalDate,但是转换结果是OK的. <br>
     *
     * @return
     */
    //@Bean
    public JavaTimeModule javaTimeModule(){
        String localDateTimeFormat = "yyyy-MM-dd HH:mm:ss";
        String localDateFormat="yyyy-MM-dd";
        String localTimeFormat = "HH:mm:ss";

        JavaTimeModule javaTimeModule = new JavaTimeModule();

        javaTimeModule.addSerializer(LocalDateTime.class,
                new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(localDateTimeFormat)));
        javaTimeModule.addSerializer(LocalDate.class,
                new LocalDateSerializer(DateTimeFormatter.ofPattern(localDateFormat)));
        javaTimeModule.addSerializer(LocalTime.class,
                new LocalTimeSerializer(DateTimeFormatter.ofPattern(localTimeFormat)));

        // ==== 设置反序列化格式的类型只会保持一个,因为内部通过Map.put(key,value)实现的,上述同理(i.e.序列化也是如此)
        // ==== eg.下面add2个LocalDateTime,导致第一个会被覆盖.
        javaTimeModule.addDeserializer(LocalDateTime.class,
                new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(localDateTimeFormat)));
        //javaTimeModule.addDeserializer(LocalDateTime.class,new LocalDateTimeDeserializer(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        javaTimeModule.addDeserializer(LocalDate.class,
                new LocalDateDeserializer(DateTimeFormatter.ofPattern(localDateFormat)));
        javaTimeModule.addDeserializer(LocalTime.class,
                new LocalTimeDeserializer(DateTimeFormatter.ofPattern(localTimeFormat)));

        // ==== 注册module
        //ObjectMapper objectMapper = new ObjectMapper();
        //objectMapper.registerModule(javaTimeModule);

        return javaTimeModule;
    }

}
