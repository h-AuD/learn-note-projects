package AuD.template.project.core.exception;

import AuD.component.common.web.AbstractServerException;
import AuD.template.project.core.constant.StatusCode;

import static AuD.template.project.core.constant.StatusCode.PARAMETER_ERROR;

/**
 * Description: 参数异常
 *
 * @author AuD/胡钊
 * @ClassName ParameterException
 * @date 2021/12/7 14:04
 * @Version 1.0
 */
public class ParameterException extends AbstractServerException {

    /**
     * 常量(参数异常常量)
     */
    public static final ParameterException PARAMETER_EXCEPTION = new ParameterException(PARAMETER_ERROR.getCode(), PARAMETER_ERROR.getMessage());

    /**
     * 参数异常({@link StatusCode#PARAMETER_ERROR}).    <br>
     *
     * 构造器设置包访问权限. -- 参考{@link SignatureFailException}注释内容
     *
     * @param errorCode
     * @param errorMsg
     */
    ParameterException(final int errorCode, final String errorMsg) {
        super(errorCode, errorMsg);
    }

    /**
     * 构建自定义errorMsg的{@code ParameterException}对象。-- 可以根据各自(this)需求是否需要设置该方法.
     *
     * @param errorMsg
     * @return
     */
    public static ParameterException of(final String errorMsg){
        return new ParameterException(PARAMETER_ERROR.getCode(),errorMsg);
    }
}
