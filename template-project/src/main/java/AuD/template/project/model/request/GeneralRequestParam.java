package AuD.template.project.model.request;

import lombok.Data;

/**
 * Description: 通用的请求参数
 *
 * @author AuD/胡钊
 * @ClassName GeneralRequestParam
 * @date 2021/12/1 17:10
 * @Version 1.0
 */
@Data
public class GeneralRequestParam {

    /** 请求时间戳(单位s) */
    private long timestamp=0;

    /** 随机字符 */
    private String nonce_str;

    /** 序列号 */
    private String serial_no;

    /** 用户标识 */
    private long uid = 0L;

}
