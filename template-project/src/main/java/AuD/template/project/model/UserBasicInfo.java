package AuD.template.project.model;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * Description: TODO
 *
 * @author AuD/胡钊
 * @ClassName UserBasicInfo
 * @date 2021/4/13 17:42
 * @Version 1.0
 */
@Data
public class UserBasicInfo {

    /** 自增主键 */
    private int id;

    /** 用户名称 */
    private String name;

    /** 登陆密码 */
    private String password;

    /** 创建时间 */
    private LocalDateTime ctime;

}
