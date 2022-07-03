package AuD.template.project.controller.springmvc;

import AuD.component.common.web.ControllerResultInfo;
import AuD.component.common.web.ControllerResultInfoBuilder;
import AuD.template.project.model.request.GeneralRequestParam;
import AuD.template.project.model.request.SpringMVCRequestParamInfo;
import AuD.template.project.model.ValidBeanInfo;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * Description: SpringMVC注解使用相关case. <br>
 * 1.{@link RequestMapping}     <br>
 *
 * 2.{@link RequestBody}    <br>
 *
 * 3.{@link RequestParam}   <br>
 *
 * 4.{@link ResponseBody}   <br>
 *
 * @author AuD/胡钊
 * @ClassName SpringMVCAboutController
 * @date 2021/10/28 16:42
 * @Version 1.0
 */
@RestController
@RequestMapping(value = "/springMVC_annotation")
// ==== 全局配置,即this controller下所有请求参数必须包含"key1","key2","key3"请求参数.
//@RequestMapping(value = "/springMVC_annotation",params = {"key1","key2","key3"})
public class SpringMVCAboutController {

    /**
     * 1.一个controller API是不允许有多个{@link RequestBody}存在的,
     * 否则抛异常:I/O error while reading input message; nested exception is java.io.IOException: Stream closed.  <br>
     * 原因:因为一个request中只包含一个request body,理解了这个,就会明白Spring MVC不支持多个@RequestBody. === 请另行测试 <br>
     * <br>
     * Note:    <br>
     * 1.queryParam 前面不可以加上{@link RequestParam},否则请求会报错(status:400,error:"Bad Request"),错误信息如下: <br>
     * Resolved [org.springframework.web.bind.MissingServletRequestParameterException:
     * Required request parameter 'queryParam' for method parameter type ValidBeanInfo is not present].  <br>
     * -- 务必仔细阅读{@link RequestParam}上注释内容.  <br>
     * -- {@code RequestParam}仅仅标识某个具体的参数(不可用来标识对象),
     * 其中参数来源:query parameters,form data and parts in multipart requests(摘自{@code RequestParam}上注释内容),
     * 其中对象由springMVC参数handle自动封装(具体参考SpringMVC处理请求流程). <br>
     *
     * @param validBeanInfo 请求体数据(json)
     * @param queryParam    请求参数(用于接收http请求参数),由于此处使用json格式接收,所以queryParam只能接收http中query_param
     * @return
     */
    @PostMapping(value = "/requestBody_case")
    public ControllerResultInfo requestBodyUsing(@RequestBody @Validated ValidBeanInfo validBeanInfo,GeneralRequestParam queryParam){
        System.out.println("@RequestBody param(validBeanInfo) info:"+validBeanInfo);
        System.out.println("query param(queryParam) info:"+queryParam);
        return ControllerResultInfoBuilder.success();
    }

    /**
     * 以表单形式提交数据,i.e.request_header "Content-Type: application/x-www-form-urlencoded".   <br>
     * eg:  <br>
     * url --> https://localhost:8080/path?name=张三&age=20&names=张三    <br>
     * header --> Content-Type: application/x-www-form-urlencoded    <br>
     * form --> name=xxx&age=18&k1=v1&k2=v2&names=李四,王五  <br>
     * result: requestParamInfo{name='张三,xxx',age=20,names=['张三','李四,王五']} ; queryParam{name='张三,xxx',age=20}
     * <br>
     * 对于表单提交方式,当API参数是Obj类型时,SpringMVC会将query parameters,form data,按照对象属性名进行组装.    <br>
     * -- age(int类型),由于query param 和 form中都存在,但是age最终会被赋值一次(query param优先,eg:上述case).   <br>
     * -- name(String类型),由于query param 和 form中都存在,但是name最终都会被赋值,以逗号分隔(query param优先,eg:上述case). <br>
     * -- names(List类型),同上. <br>
     *
     * @param requestParamInfo
     * @param queryParam
     * @return
     */
    @PostMapping(value = "/form_case")
    public ControllerResultInfo formRequest(SpringMVCRequestParamInfo requestParamInfo,
                                            ValidBeanInfo queryParam,
                                            @RequestParam String name,
                                            @RequestParam int age){
        System.out.println("request param info:"+requestParamInfo);
        System.out.println("query param info:"+queryParam);
        System.out.println("param(name):"+name+",param(age):"+age);
        return ControllerResultInfoBuilder.success();
    }


}
