package AuD.template.project.controller.exception;

import AuD.component.common.web.AbstractServerException;
import AuD.component.common.web.ControllerResultInfo;
import AuD.component.common.web.ControllerResultInfoBuilder;
import AuD.template.project.core.conf.GlobalExceptionHandler;
import AuD.template.project.core.conf.web.filter.SignatureFilter;
import AuD.template.project.core.exception.ParameterException;
import AuD.template.project.core.exception.SignatureFailException;
import AuD.template.project.mapper.UserBasicInfoErrorMapper;
import AuD.template.project.model.UserBasicInfo;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import static AuD.template.project.core.constant.StatusCode.PARAMETER_ERROR;
import static AuD.template.project.core.constant.StatusCode.SERVER_ERROR;


/**
 * Description: 异常处理 ==== 交由{@link GlobalExceptionHandler}处理.  <br>
 *
 *
 * @author AuD/胡钊
 * @ClassName ExceptionController
 * @date 2021/4/27 18:06
 * @Version 1.0
 */
@RestController
@RequestMapping(value = "/error_internal")
public class ExceptionHandleController {

    /**
     * 执行错误SQL mapper.<br>
     * ps:这里仅仅为了方便测试,才如此注入(在controller中注入mapper).<br>
     */
    private UserBasicInfoErrorMapper userBasicInfoErrorMapper;

    /**
     * 主要用于测试servlet转发,参见{@link SignatureFilter}
     * === 不推荐这样做.  <br>
     * 先判断请求中是否带有目标属性(eg.'signature:fail'),根据result做相关的处理: <br>
     * - true === 抛异常,交由全局异常处理器({@link GlobalExceptionHandler})处理   <br>
     * - false === return即可(不做处理)   <br>
     * @param request
     */
    @RequestMapping(value = "/signature/fail")
    public void handleSignatureFailByServletForward(HttpServletRequest request){
        Object exception = request.getAttribute("signature:fail");
        if(exception!=null && exception instanceof SignatureFailException){
            throw ((SignatureFailException)exception);
        }
        return;
    }

    /**
     * 验证全局异常处理器{@link GlobalExceptionHandler}.    <br>
     * 通过参数"route"来路由到不同的异常,全局异常采用的精准匹配,i.e.抛出A异常,优先查找A异常handler,依次向上(父类)查找.    <br>
     * @param route
     * @return
     */
    @PostMapping(value = "/global_exception")
    public ControllerResultInfo handleGlobalException(@RequestParam(required = false) int route){
        throwServerInternalException(route);
        return ControllerResultInfoBuilder.success();
    }

    /**
     * 如果捕获异常(try..catch..),则全局异常不会生效 === 原因在于代理(AOP)
     *
     * @return
     */
    @PostMapping(value = "/global_exception_catch")
    public ControllerResultInfo handleTryCatchException(){
        try {
            throw AbstractServerException.of(PARAMETER_ERROR.getCode(), PARAMETER_ERROR.getMessage());
        }catch (Exception e){
            // ==== nothing
        }
        return ControllerResultInfoBuilder.success();
    }

    /**
     * 抛出异常,通过参数 "route" 来路由不同的异常信息.   <br>
     * eg:  <br>
     * 当route==1,由{@link GlobalExceptionHandler#handleParameterException}处理.    <br>
     * -- ParameterException继承自AbstractServerException,并且GlobalExceptionHandler有AbstractServerException处理器. <br>
     *
     * 当route==2,由{@link GlobalExceptionHandler#handleException}处理. <br>
     * -- GlobalExceptionHandler没有有RuntimeException处理器,但是有Exception处理器,说明向上查找.  <br>
     *
     * @param route 路由值
     */
    private void throwServerInternalException(int route){
        switch (route){
            case 1:
                throw ParameterException.PARAMETER_EXCEPTION;
                //break;    // 对于抛出异常情况下,break是存在语法错误
            case 2:
                throw new RuntimeException("====== 出错咯 ======");
            default:
                throw AbstractServerException.of(SERVER_ERROR.getCode(),SERVER_ERROR.getMessage());
        }
    }

    /* ================== 以下验证执行错误SQL出现的异常 ======================== */

    /**
     * 执行的SQL的table不存在. <br>
     *
     * -- 由{@link GlobalExceptionHandler#handleSQLValidExceptionAsBadSqlGrammar}处理.    <br>
     *
     * @return
     */
    @GetMapping(value = "/error_sql_001")
    public ControllerResultInfo exeErrorSql001(){
        userBasicInfoErrorMapper.errorSqlAsTableNotExists(new UserBasicInfo());
        return ControllerResultInfoBuilder.success();
    }

    /**
     * 执行的SQL的table field不存在
     *
     * -- 由{@link GlobalExceptionHandler#handleSQLValidExceptionAsBadSqlGrammar}处理.    <br>
     *
     * @return
     */
    @GetMapping(value = "/error_sql_002")
    public ControllerResultInfo exeErrorSql002(){
        userBasicInfoErrorMapper.errorSqlAsFieldNotExists("AuD");
        return ControllerResultInfoBuilder.success();
    }

    /**
     * 违反DDL约束
     *
     * -- 由{@link GlobalExceptionHandler#handleSQLValidExceptionAsDuplicateKey} & {@link GlobalExceptionHandler#handleSQLValidExceptionAsBadSqlGrammar}处理.    <br>
     *
     * @return
     */
    @GetMapping(value = "/error_sql_003")
    public ControllerResultInfo exeErrorSql003(@RequestParam String name){
        final UserBasicInfo userBasicInfo = new UserBasicInfo();
        userBasicInfo.setName(name);
        userBasicInfoErrorMapper.errorSqlAsFieldConstraint(userBasicInfo);
        return ControllerResultInfoBuilder.success();
    }

}
