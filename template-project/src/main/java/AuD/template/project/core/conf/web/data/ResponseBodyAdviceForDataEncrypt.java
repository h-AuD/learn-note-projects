package AuD.template.project.core.conf.web.data;

import AuD.template.project.model.UserBasicInfo;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * Description: {@link ResponseBody} 增强逻辑.  <br>
 *
 * @author AuD/胡钊
 * @ClassName ResponseBodyAdviceForDataEncrypt
 * @date 2021/12/1 18:10
 * @Version 1.0
 */
@ControllerAdvice
public class ResponseBodyAdviceForDataEncrypt implements ResponseBodyAdvice<UserBasicInfo> {

    /**
     *
     * @param returnType
     * @param converterType
     * @return
     */
    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        if(returnType.getDeclaringClass().isAssignableFrom(UserBasicInfo.class)){
            return true;
        }
        return false;
    }

    /**
     * 在响应数据之前处理逻辑 --- eg:加密数据
     * @param body
     * @param returnType
     * @param selectedContentType
     * @param selectedConverterType
     * @param request
     * @param response
     * @return
     */
    @Override
    public UserBasicInfo beforeBodyWrite(UserBasicInfo body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        // TODO 敏感数据加密
        return body;
    }
}
