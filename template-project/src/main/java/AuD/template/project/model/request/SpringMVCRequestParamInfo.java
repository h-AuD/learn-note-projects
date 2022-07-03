package AuD.template.project.model.request;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Description: 用于测试springMVC请求参数的数据模型.
 *
 * @author AuD/胡钊
 * @ClassName SpringMVCRequestParamInfo
 * @date 2021/11/25 18:28
 * @Version 1.0
 */
@Data
public class SpringMVCRequestParamInfo {

    private String name;

    private Integer age;

    private List<String> names;

    private LocalDateTime ctime;

}
