package AuD.template.project.controller.springmvc;

import AuD.component.common.web.ControllerResultInfo;
import AuD.component.common.web.ControllerResultInfoBuilder;
import AuD.template.project.core.function.aop.RequestParameterValidInterceptor;
import AuD.template.project.model.ValidBeanInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.validation.ValidationAutoConfiguration;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.*;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;

import static AuD.template.project.core.constant.StatusCode.PARAMETER_ERROR;

/**
 * Description:请求参数校验case(处理"javax.validation"包下的注解,eg:{@link NotNull}). <br>
 * <p>
 * 1.{@link Valid} & {@link Validator} & {@link Validated}使用. </br>
 * - 其中Valid & Validator 属于"javax.validation"包下(i.e.JSR-303),Validated属于springMVC.
 * - 在method参数前加上{@link Valid} or {@link Validated}注解即可完成参数校验工作,上述注解仅仅对校验类型注解有效(i.e.{@link NotNull}).  <br>
 * - 或者直接使用{@link Validator}定义的API. <br>
 *
 * 2.{@link Valid} or {@link Validated}的区别:
 * - 定义来源不同.    <br>
 * - {@link Validated}注解表示开启Spring的校验机制,支持分组校验;{@link Valid}注解表示开启Hibernate的校验机制(SpringMVC也会处理),不支持分组校验。   <br>
 * - 个人推荐使用{@link Validated},因为开发的环境几乎在spring大环境。 <br>
 *
 * 3.熟悉下 spring boot 参数校验自动配置 {@link ValidationAutoConfiguration}. <br>
 * 注意:  <br>
 * - springboot_2.2版本(包含)中的web-starter中包含上述自动配置,但是2.4(之后)中移除上述依赖. <br>
 * - i.e.需要手动导入依赖,否则校验注解({@link Valid} & {@link Validated})无效. <br>
 *
 * 3.有关参数校验的细节可查阅hibernate官网. <br>
 * </p>
 *
 * @author AuD/胡钊
 * @ClassName ParameterValidController
 * @date 2021/7/21 19:10
 * @Version 1.0
 */
@RestController
@RequestMapping(value = "/param_valid")
public class ParameterValidController {

    /**
     * 参见{@link ValidationAutoConfiguration},注入校验器,用于实验/test参数校验的方法.    <br>
     * -- 自动装配实现类:{@link LocalValidatorFactoryBean}
     */
    @Autowired
    private Validator validator;


    /**
     * {@link Validator}校验器接口原始使用步骤. <br>
     * 1.创建交校验器.    <br>
     * 方式一:手动参见Validator对象,eg:"Validation.buildDefaultValidatorFactory().getValidator()".   <br>
     * 方式二:使用ioc容器自动注入Validator对象(参见{@link ParameterValidController#validator}).    <br>
     * 2.call API({@link Validator#validate(java.lang.Object, java.lang.Class[])}),获取校验结果.  <br>
     * <br>
     * this method is a simple case.  <br>
     * <br>
     * 总结:这种方式将数据校验代码/逻辑 糅合在业务代码中,很明显,违反了设计原则(即解耦). <br>
     * --> 下面的方法/method会进一步简化代码.
     *
     * @param validBeanInfo
     * @return
     */
    @PostMapping("/general_by_validator")
    public ControllerResultInfo validWithoutBindingResult(@RequestBody ValidBeanInfo validBeanInfo){
        // === 下述的工厂类和校验器类也可以使用上述由IOC容器中获取的对象实例代替(仅仅演示手动参见校验器)
        //实例化一个 validator工厂
        //ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        //获取validator实例
        //Validator validator = validatorFactory.getValidator();
        //调用调用，得到校验结果信息 Set
        Set<ConstraintViolation<ValidBeanInfo>> constraintViolationSet = validator.validate(validBeanInfo);
        //StringBuilder组装异常信息
        StringBuilder builder = new StringBuilder();
        //遍历拼装
        constraintViolationSet.forEach(violationInfo -> {
            builder.append(violationInfo.getMessage() + " ");
        });
        if (builder.toString().length() > 0){
            return ControllerResultInfoBuilder.of(PARAMETER_ERROR.getCode(),builder.toString());
        }
        return ControllerResultInfoBuilder.success();
    }


    /**
     * 使用{@link BindingResult}来完成参数校验.  <br>
     * -- 当参数包含BindingResult类型对象,springMVC会将BindingResult对象作为参数传进来(仅限SpringMVC环境下/dispatcherServlet).   <br>
     * -- 若有多个对象需要校验,可以加BindingResult(请另行测试),eg: </br>
     * public void test()(T t, BindingResult result) <br>
     * public void test()(T1 t1, BindingResult result1,T2 t2, BindingResult result2) <br>
     * <br>
     * Note:    <br>
     * 1.这种校验方式很少见,不推荐要这样写,因为与{@link BindingResult}强耦合,并且springMVC支持自动校验(通过注解{@link Valid} or {@link Validated})。<br>
     * 2.如果method带有{@link BindingResult}参数,即使加上{@link Valid},springMVC也不会处理,会交给 BindingResult 处理. <br>
     * <br>
     *
     *
     * @param validBeanInfo
     * @param bindingResult
     * @return
     */
    @PostMapping("/do_by_bindingResult")
    public ControllerResultInfo validWithBindingResult(@RequestBody @Valid ValidBeanInfo validBeanInfo, BindingResult bindingResult){
        // === 判断参数校验是否通过
        if(bindingResult.hasErrors()){
            //校验结果以集合的形式返回，当然也可以获取单个。具体可以查看bindResult的API文档
            List<FieldError> fieldErrorList = bindingResult.getFieldErrors();
            //StringBuilder组装异常信息
            StringBuilder builder = new StringBuilder();
            //遍历拼装
            fieldErrorList.forEach(error -> {
                builder.append(error.getDefaultMessage() + " ");
            });
            return ControllerResultInfoBuilder.of(PARAMETER_ERROR.getCode(),builder.toString());
        }
        return ControllerResultInfoBuilder.success();
    }

    /**
     * 验证请求参数时,在参数前加注解{@link Valid} or {@link Validated} == 注意:classpath下必须包含相关依赖,否则注解无效.  <br>
     * - 如果校验失败,会抛出异常{@link org.springframework.web.bind.MethodArgumentNotValidException}
     * @param validBeanInfo
     * @return
     */
    @PostMapping("/do_by_valid")
    public ControllerResultInfo validWithBindingResultWithoutBindingResult(@RequestBody @Valid ValidBeanInfo validBeanInfo){
        return ControllerResultInfoBuilder.success();
    }

    /**
     * 测试 === 通过AOP来完成参数校验,参见{@link RequestParameterValidInterceptor}
     */
    @PostMapping("/do_by_aop")
    public ControllerResultInfo validParamByAop(@RequestBody ValidBeanInfo validBeanInfo){
        return ControllerResultInfoBuilder.success();
    }




}
