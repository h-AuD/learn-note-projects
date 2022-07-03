package AuD.template.project.core.conf.web.data;

import AuD.template.project.model.UserBasicInfo;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;

import java.lang.reflect.Type;

/**
 * Description: {@link RequestBody}增强,这里仅仅处理ValidBeanInfo类型.
 *
 * @author AuD/胡钊
 * @ClassName RequestBodyAdviceForValidBeanInfo
 * @date 2021/12/1 17:50
 * @Version 1.0
 */
@ControllerAdvice   // 该注解必须带上,否则当前配置无效
public class RequestBodyAdviceForUserBasicInfo extends RequestBodyAdviceAdapter {

    /**
     * 是否支持处理,可以通过 methodParameter 等参数来判断,是否需要处理.
     * @param methodParameter
     * @param targetType
     * @param converterType
     * @return
     */
    @Override
    public boolean supports(MethodParameter methodParameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        // ==== 判断目标类型是否为"ValidBeanInfo"类型
        if(((Class) targetType).isAssignableFrom(UserBasicInfo.class)){
            return true;
        }else {
            return false;
        }
    }

    /**
     * 在springMVC封装请求体后会call,即可以对 body 做些额外处理,比如某些特殊属性赋值处理.
     *
     * @param body
     * @param inputMessage
     * @param parameter
     * @param targetType
     * @param converterType
     * @return
     */
    @Override
    public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        // ==== 给id赋值
        ((UserBasicInfo)body).setId(0);
        return super.afterBodyRead(body,inputMessage,parameter,targetType,converterType);
    }
}
