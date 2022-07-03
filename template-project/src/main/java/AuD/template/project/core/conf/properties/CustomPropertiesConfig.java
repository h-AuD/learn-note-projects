package AuD.template.project.core.conf.properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Description: {@link ConfigurationProperties}自定义属性配置(i.e."ConfigurationProperties"注解使用). <br>
 *
 * Note:    <br>
 * 1.ConfigurationProperties单独使用存在语法问题,因为需要该注解标注的类或方法(eg.@bean)必须在spring容器中存在(i.e.容器内有对应的bean).
 * 常常结合{@link EnableConfigurationProperties}一起使用,常见于自动配置类或者配置类(eg:{@link DataSourceAutoConfiguration}),
 * 或者 使用{@link Component}/{@link Bean}.  <br>
 * -- "EnableConfigurationProperties" & "Component"可同时存在(!!!不要这样做!!!).  <br>
 * -- 原因:EnableConfigurationProperties实则是import一个ImportBeanDefinitionRegistrar,在注册bean的时候有判断BD是否存在的逻辑.  <br>
 *
 * 2.不同于{@link Value},ConfigurationProperties通过注入属性到对象中,i.e."prefix.fieldName=value".    <br>
 * -- fieldName可以是任意类型(包含父类),eg.基本数据类型、对象引用类型(自身内部类 或 外部类).    <br>
 *
 *
 * @author AuD/胡钊
 * @ClassName CustomPropertiesConfig
 * @date 2021/9/14 16:19
 * @Version 1.0
 */
@ConfigurationProperties(prefix = "custom.multi")
@Component
//@EnableConfigurationProperties(CustomPropertiesConfig.class)
public class CustomPropertiesConfig extends SupperCustomPropertiesConfig{

    /**
     * map属性注入. <br>
     * eg:  <br>
     * custom.multi.common.key1=v1  <br>
     * custom.multi.common.key2=v2  <br>
     * -- 最终common会包含key1 & key2。
     */
    private Map<String,String> common;

    /**
     * 简单的对象ref_type(from this inner-class,note:if ref is from this,ref must be public & static).   <br>
     * - 对象引用类型,如果该对象是来自自身,那么必须为静态内部类.    <br>
     * eg:  <br>
     * custom.multi.innerClazz.host=value  <br>
     */
    private CustomStaticInnerClazz innerClazz;

    /**
     * 简单的ref_type(来自第三方/jar).  <br>
     * eg:  <br>
     * custom.multi.dataSourceProperties.username=value  <br>
     */
    private DataSourceProperties dataSourceProperties;

    /**
     * map属性注入,其中value为ref_type. <br>
     * eg:  <br>
     * custom.multi.innerClazzMap.keyName.host=value
     */
    private Map<String,CustomStaticInnerClazz> innerClazzMap;

    /**
     * 对象集合,其对象为内部类.    <br>
     * eg:  <br>
     * custom.multi.innerClazzList[0].host=value
     */
    private List<CustomStaticInnerClazz> innerClazzList;

    /**
     * 字符串集合设置,可以设置如下格式:    <br>
     * key = v1,v2,v3   <br>
     * - i.e.可以通过逗号设置,默认会分解成list(默认规则是逗号).  <br>
     */
    private List<String> strList;


    /*====================== getter & setter(must set_up) ================================ */

    public Map<String, String> getCommon() {
        return common;
    }

    public void setCommon(Map<String, String> common) {
        this.common = common;
    }

    public CustomStaticInnerClazz getInnerClazz() {
        return innerClazz;
    }

    public void setInnerClazz(CustomStaticInnerClazz innerClazz) {
        this.innerClazz = innerClazz;
    }

    public DataSourceProperties getDataSourceProperties() {
        return dataSourceProperties;
    }

    public void setDataSourceProperties(DataSourceProperties dataSourceProperties) {
        this.dataSourceProperties = dataSourceProperties;
    }

    public Map<String, CustomStaticInnerClazz> getInnerClazzMap() {
        return innerClazzMap;
    }

    public void setInnerClazzMap(Map<String, CustomStaticInnerClazz> innerClazzMap) {
        this.innerClazzMap = innerClazzMap;
    }

    public List<CustomStaticInnerClazz> getInnerClazzList() {
        return innerClazzList;
    }

    public void setInnerClazzList(List<CustomStaticInnerClazz> innerClazzList) {
        this.innerClazzList = innerClazzList;
    }

    public List<String> getStrList() {
        return strList;
    }

    public void setStrList(List<String> strList) {
        this.strList = strList;
    }

    /**
     * 必须为 "public static",否则注入失败.
     */
    public static class CustomStaticInnerClazz{

        private String host;

        private Integer port=22;

        private String pws;



        /*====================== getter & setter(must set_up) ================================ */

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public Integer getPort() {
            return port;
        }

        public void setPort(Integer port) {
            this.port = port;
        }

        public String getPws() {
            return pws;
        }

        public void setPws(String pws) {
            this.pws = pws;
        }
    }


}
