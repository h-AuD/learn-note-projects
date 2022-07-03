package AuD.template.project.core.conf;

import AuD.component.common.web.AbstractServerException;
import AuD.component.common.web.ControllerResultInfo;
import AuD.component.common.web.ControllerResultInfoBuilder;
import AuD.template.project.controller.springmvc.ParameterValidController;
import AuD.template.project.core.exception.ParameterException;
import AuD.template.project.core.exception.SignatureFailException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static AuD.template.project.core.constant.StatusCode.PARAMETER_ERROR;
import static AuD.template.project.core.constant.StatusCode.SERVER_ERROR;


/**
 * Description: 全局异常处理 === {@link RestControllerAdvice}. <br>
 * -- 异常匹配采取精准匹配模式.即如果抛 XxxException,则匹配XxxException,如果没有匹配到对应的ExceptionHandler,则依次匹配其super ExceptionHandler. <br>
 * -- eg:XxxException extends RuntimeException,如果存在XxxException handler,进入该handler,否则依次向上查找.    <br>
 *
 * @author AuD/胡钊
 * @ClassName GlobalExceptionHandler
 * @date 2021/4/16 10:13
 * @Version 1.0
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /* 异常信息格式 */
    private String format = "{}:异常信息:{}";


    /**
     * 处理{@link Exception}
     * @return
     */
    @ExceptionHandler(Exception.class)
    public ControllerResultInfo handleException(Exception e){
        log.error(format,LocalDateTime.now(),e.getMessage());
        return ControllerResultInfoBuilder.fail(e.getMessage());
    }


    /**
     * 处理API签名失败异常
     * @param signatureFailException
     * @return
     */
    @ExceptionHandler(value = SignatureFailException.class)
    public ControllerResultInfo handleSignatureFailException(SignatureFailException signatureFailException){
        return ControllerResultInfoBuilder.error(signatureFailException);
    }

    /**
     * 处理参数异常
     * @param parameterException
     * @return
     */
    @ExceptionHandler(value = ParameterException.class)
    public ControllerResultInfo handleParameterException(ParameterException parameterException){
        return ControllerResultInfoBuilder.error(parameterException);
    }

    /**
     * 处理通用的server exception
     * @param e
     * @return
     */
    @ExceptionHandler(value = AbstractServerException.class)
    public ControllerResultInfo handleCommonServerException(AbstractServerException e){
        return ControllerResultInfoBuilder.error(e);
    }

    /**
     * API(接口),请求体参数校验失败异常处理器. <br>
     * note: <br>
     * 1.{@link MethodArgumentNotValidException}是spring内定义的异常类,用于参数校验,当校验失败时,才会抛出此异常. <br>
     * -- 参数校验相关信息参见{@link ParameterValidController}内注释和case.
     * 2.下面注释内容("constraintViolationException({@link ConstraintViolationException})")实际上是hibernate内定义的异常. <br>
     * -- 当违反hibernate定义的约束(eg.NotNull...)才会抛出此异常.在没有使用hibernate或者JPA等相关组件时,this exception不需要捕获的. <br>
     *
     * @param e
     * @return
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ControllerResultInfo handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        // e.printStackTrace();
        log.error(format,LocalDateTime.now(),e.getMessage());
        List<ObjectError> allErrors = e.getBindingResult().getAllErrors();
        if (allErrors.isEmpty()) {
            return ControllerResultInfoBuilder.of(PARAMETER_ERROR.getCode(),"check your request param");
        }
        String message = allErrors.stream().map(s -> s.getDefaultMessage()).collect(Collectors.joining(";"));
        return ControllerResultInfoBuilder.of(PARAMETER_ERROR.getCode(),message);
    }


    /**
     * {@link ConstraintViolationException}是属于JSR-303(i.e."javax.validation"包下定义的class),
     * 如果没有使用相对应API/工具(eg.没有使用hibernate),是不会抛这个异常,所以在没有条件的情况下(没有使用hibernate等相关API),
     * 不需要有这个"ExceptionHandler".    <br>
     * @param e
     * @return
     */
    //@ExceptionHandler({ConstraintViolationException.class})
    //public ControllerResultInfo constraintViolationException(ConstraintViolationException e) {
    //    e.printStackTrace();
    //    log.error("coming in GlobalException:{}",e);
    //    Set<ConstraintViolation<?>> constraintViolations = e.getConstraintViolations();
    //    if (constraintViolations.isEmpty()) {
    //        return ControllerResultInfoBuilder.of(40002,"check your request param");
    //    }
    //    StringBuilder errorMsg = new StringBuilder();
    //    constraintViolations.forEach(item -> errorMsg.append(item.getMessage()).append(","));
    //    return ControllerResultInfoBuilder.of(40002,errorMsg.toString());
    //}

    /* ===================== 以下是SQL 相关异常处理器 ======================
     * 1.SQLException:(package java.sql)
     * - 通常的数据库厂商自定义的异常会继承它,建议简单了解下它的子类.
     * -
     * 2.DuplicateKeyException
     * - DB table约束异常,eg:违反UK
     *
     * 3.DataIntegrityViolationException
     * - 违反数据完整性.
     * eg:字段超过table设置字段长度、为允许为null的字段设置null值
     *
     * 4.BadSqlGrammarException
     * - 错误sql语法异常:eg.操作一个不存在的表或者字段
     * ==================================================================*/
    /**
     * 处理SQLException.  <br>
     * -- "java.sql"包下定义的class,通常各个数据库厂商设计的JDBC驱动程序(jar包)中异常继承自{@link SQLException}.    <br>
     * <br>
     * 几乎所有的DB操作相关异常都可以用"SQLException"来表示.  <br>
     * -- 但是在spring环境下,不会抛出SQLException,因为SQLException被Spring捕获,并抛出spring自定义的异常(eg.{@link DuplicateKeyException}),
     * 这些异常来自package "org.springframework.dao" & "org.springframework.jdbc".    <br>
     *
     * @param sqlException
     * @return
     */
    @ExceptionHandler(SQLException.class)
    public ControllerResultInfo handleGeneralSqlException(SQLException sqlException){
        log.error(format,LocalDateTime.now(),sqlException.getMessage());
        // ==== 返回一个合理的信息,其中msg建议更具实际情况自定义,或者直接用一个通用msg
        return ControllerResultInfoBuilder.of(SERVER_ERROR.getCode(),SERVER_ERROR.getMessage());
    }

    /**
     * DuplicateKeyException处理器. <br>
     *
     * @param duplicateKeyException 字段重复约束(eg.unique key) -- package(org.springframework.dao)
     * @return
     */
    @ExceptionHandler(value = DuplicateKeyException.class)
    public ControllerResultInfo handleSQLValidExceptionAsDuplicateKey(DuplicateKeyException duplicateKeyException){
        log.error("{}",duplicateKeyException.getMessage());
        return ControllerResultInfoBuilder.of(SERVER_ERROR.getCode(),"duplicate Key:"+duplicateKeyException.getMessage());
    }

    /**
     * DataIntegrityViolationException处理器.
     *
     * @param dataIntegrityViolationException 违反数据完整性 -- package(org.springframework.dao)
     * @return
     */
    @ExceptionHandler(value = DataIntegrityViolationException.class)
    public ControllerResultInfo handleSQLValidExceptionAsBadSqlGrammar(DataIntegrityViolationException dataIntegrityViolationException){
        log.error("{}",dataIntegrityViolationException.getMessage());
        return ControllerResultInfoBuilder.of(SERVER_ERROR.getCode(),"duplicate Key:"+dataIntegrityViolationException.getMessage());
    }

    /**
     * BadSqlGrammarException处理器.
     *
     * @param badSqlGrammarException -- package(org.springframework.jdbc)
     * @return
     */
    @ExceptionHandler(value = BadSqlGrammarException.class)
    public ControllerResultInfo handleSQLValidExceptionAsBadSqlGrammar(BadSqlGrammarException badSqlGrammarException){
        log.error("{}",badSqlGrammarException.getMessage());
        return ControllerResultInfoBuilder.of(SERVER_ERROR.getCode(),"duplicate Key:"+badSqlGrammarException.getMessage());
    }




}
