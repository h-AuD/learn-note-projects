package AuD.template.project.core.function.datax;

import lombok.Data;

import java.time.LocalTime;
import java.util.Map;

/**
 * Description: TODO
 *
 * @author AuD/胡钊
 * @ClassName DataXTemplateInfo
 * @date 2021/10/18 14:03
 * @Version 1.0
 */
@Data
public class DataXTemplateInfo {

    /*
        {
          "xxx_template": {
            "jobExecuteTrigger": "15:30:00",
            "content": {
              "#KEY1#": "",
              "#KEY2#": "",
              "#KEY3#": ""
            }
          }
        }
     */

    /** JOB执行触发时机 */
    private LocalTime jobExecuteTrigger;

    private Map<String,String> content;


}
