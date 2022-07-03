package AuD.template.project.model;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;

/**
 * Description: valid 注解验证
 *
 * @author AuD/胡钊
 * @ClassName ValidBean
 * @date 2021/7/21 14:39
 * @Version 1.0
 */
@Data
public class ValidBeanInfo {

    @NotNull(message = "name must not null")
    private String name;

    @Max(value = 300,message = "年龄不符合实际")
    private Integer age;

}
