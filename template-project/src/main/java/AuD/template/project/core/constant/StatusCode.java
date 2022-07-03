package AuD.template.project.core.constant;

/**
 * Description: 返回码列表
 *
 * @author AuD/胡钊
 * @ClassName StatusCode
 * @date 2021/1/19 11:09
 * @Version 1.0
 */
public enum StatusCode {

    /** 表示API入参数错误 */
    PARAMETER_ERROR(2030,"parameter is error,please check param"),
    /** API签名失败 */
    SIGNATURE_FAIL(4001,"signature fail"),
    /** 服务器内部错误,eg.SQL执行出现异常... */
    SERVER_ERROR(9999,"server occur error"),
    ;

    private Integer code;
    private String message;

    StatusCode(Integer code, String message){
        this.code = code;
        this.message = message;
    }

    /* getter */
    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
