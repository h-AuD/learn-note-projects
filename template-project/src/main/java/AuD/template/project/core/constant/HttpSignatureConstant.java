package AuD.template.project.core.constant;

import AuD.template.project.core.conf.web.filter.SignatureFilter;

/**
 * Description: http request 签名失败相关常量. <br>
 * PS:{@link HttpSignatureConstant}
 *
 * @author AuD/胡钊
 * @ClassName MtsConstantConfig
 * @date 2021/2/25 15:06
 * @Version 1.0
 */
public interface HttpSignatureConstant {

    /**
     * 请求头中 "签名" 参数名称
     */
    String SIGNATURE_PARAM_NAME= "signature";

    /**
     * 签名失败时,设置请求中属性名称,参见{@link SignatureFilter}
     */
    String REQUEST_ATTRIBUTE_NAME_SIGNATURE_FAIL = "signature:fail";

    /**
     * 签名失败时,请求转发路径
     */
    String DISPATCHER_PATH_SIGNATURE_FAIL = "/error_internal/signature/fail";


}
