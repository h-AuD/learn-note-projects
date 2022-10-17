package AuD.template.project.core.exception;



import AuD.common.function.web.AbstractServerException;

import static AuD.template.project.core.constant.StatusCode.SIGNATURE_FAIL;

/**
 * Description: 签名失败异常. <br>
 *
 * @author AuD/胡钊
 * @ClassName SignatureFailException
 * @date 2021/4/27 16:45
 * @Version 1.0
 */
public class SignatureFailException extends AbstractServerException {

    /**
     * 常量(签名失败).
     */
    public static final SignatureFailException SIGNATURE_FAIL_EXCEPTION = new SignatureFailException(SIGNATURE_FAIL.getCode(),SIGNATURE_FAIL.getMessage());

    /**
     * 设置包访问权限. <br>
     * 1.禁止直接外部new对象。<br>
     * 2.既然是{@link AbstractServerException}类型,那么其code & msg具有唯一性或者场景性,即某个code值对应某个异常. <br>
     * -- eg.签名失败异常:code(40001),msg("signature fail"). <br>
     *
     * @param errorCode
     * @param errorMsg
     */
    SignatureFailException(final int errorCode,final String errorMsg) {
        super(errorCode, errorMsg);
    }



}
