package AuD.template.project.core.function.aop;

import AuD.component.common.web.ControllerResultInfoBuilder;
import AuD.template.project.controller.springmvc.ParameterValidController;
import AuD.template.project.core.conf.GlobalExceptionHandler;
import AuD.template.project.model.ValidBeanInfo;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.servlet.DispatcherServlet;

import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validator;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import static AuD.template.project.core.constant.StatusCode.PARAMETER_ERROR;

/**
 * Description: test通过AOP来控制/校验(API)参数(i.e.校验 javax.validation.constraints下的注解).  <br>
 * 不推荐这样做,理由如下:  <br>
 * 1.springBoot/springMVC本身提供了API参数校验功能(eg.{@link Valid} & {@link Validator} 组合全局异常{@link GlobalExceptionHandler#handleMethodArgumentNotValidException(MethodArgumentNotValidException)})。  <br>
 * 2.若要拦截,则需要使用到环绕通知(需要返回),然而{@link Around}用起来需要一定的技术能力(如果使用不恰当,可能会导致灾难).   <br>
 *
 * @author AuD/胡钊
 * @ClassName RequestValidInterceptor
 * @date 2021/7/21 14:52
 * @Version 1.0
 */
@Component
@Aspect
public class RequestParameterValidInterceptor {

    // == 注入校验器
    @Autowired
    private Validator validator;

    // === 信息分隔符
    private String SEPARATOR = " & ";

    /**
     * 拦截{@link ParameterValidController#validParamByAop(ValidBeanInfo)}method. <br>
     * 遗留一个小问题:如果某个method parameter带有{@link Valid} or {@link Validated},并且该方法又在this拦截范围内,那么逻辑该怎么走了? <br>
     * result:先走{@link Valid}(or {@link Validated}),在走AOP ==== 原因参见{@link DispatcherServlet}生命周期,进入controller_method之前有个参数组件的逻辑.    <br>
     */
    @Pointcut("execution(* AuD.template.project.controller.springmvc.ParameterValidController.validParamByAop(..))")
    public void interceptorValid(){

    }

    /**
     * 环绕通知 慎用,原因在于它会拦截目标全部流程(before、doing、after),如果有非预期情况,那么可能会出现不可预测的结果. <br>
     * eg:TODO 如果需要对方法参数做处理
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around("interceptorValid()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        List<Object> parameterList = Arrays.asList(joinPoint.getArgs());
//        //实例化一个 validator工厂
//        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
//        //获取validator实例
//        Validator validator = validatorFactory.getValidator();
        StringBuilder stringBuilder = new StringBuilder();
        parameterList.forEach(parameter -> append(parameter,stringBuilder));
        if(stringBuilder.length()>0){
            return ControllerResultInfoBuilder.of(PARAMETER_ERROR.getCode(),stringBuilder.toString());
        }
        Object result = joinPoint.proceed();
        return result;
    }


    private void append(Object parameter, StringBuilder stringBuilder){
        // === 标志信息,表示是否是第一次,如果是,则stringBuilder需要以对象(parameter)className开头
        AtomicBoolean flag = new AtomicBoolean(true);
        Set<ConstraintViolation<Object>> validate = validator.validate(parameter);
        validate.forEach(validateInfo ->{
            if(flag.get()){
                stringBuilder.append(parameter.getClass().getSimpleName()).append(":").append(validateInfo.getMessage()+ SEPARATOR);
                flag.set(false);
            }else {
                stringBuilder.append(validateInfo.getMessage()+ SEPARATOR);
            }
        });
        // 删除最后一个 separator
        stringBuilder.delete(stringBuilder.lastIndexOf(SEPARATOR),stringBuilder.length()).append(" ");
    }

}
