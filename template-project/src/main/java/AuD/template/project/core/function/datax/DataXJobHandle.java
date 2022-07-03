package AuD.template.project.core.function.datax;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;

/**
 * Description: TODO
 *
 * @author AuD/胡钊
 * @ClassName DataXJobHandle
 * @date 2021/10/19 11:42
 * @Version 1.0
 */
@Component
public class DataXJobHandle {

    @Autowired
    private DataXExecutor dataXExecutor;

    /**
     *
     */
    @Autowired
    private ObjectMapper objectMapper;


    /**
     *
     * @param dataXTemplateContentStructureFile
     * @throws IOException
     */
    public void handle(File dataXTemplateContentStructureFile) throws IOException {
        handle(objectMapper.readValue(dataXTemplateContentStructureFile, new TypeReference<Map<String, DataXTemplateInfo>>() {}));
    }


    private void handle(Map<String, DataXTemplateInfo> mapping){
        // === 存放处理失败的job容器
        Map<String,DataXTemplateInfo> retry = new ConcurrentHashMap<>(mapping.size());
        mapping.forEach((templateNameWithoutExtension,dataXTemplateInfo) -> {
            if(!doHandle(templateNameWithoutExtension,dataXTemplateInfo)){
                // == TODO 需要一个处理失败报警机制,可以使用监听器来完成
                retry.put(templateNameWithoutExtension,dataXTemplateInfo);
            }
        });
        // === 失败重试机制 TODO 重试次数是否需要限制,当前重试job是否会持续到下一次定时任务中?
        while (!retry.isEmpty()){
            retry.forEach((templateNameWithoutExtension, dataXTemplateInfo) -> {
                if(doHandle(templateNameWithoutExtension,dataXTemplateInfo)){
                    retry.remove(templateNameWithoutExtension);
                }
            });
        }
    }

    private boolean doHandle(String templateNameWithoutExtension,DataXTemplateInfo dataXTemplateInfo){
        Boolean aBoolean = false;
        try {
            // === get_function会发生阻塞,其阻塞时间为最长的那一个
            aBoolean = dataXExecutor.processing(templateNameWithoutExtension,dataXTemplateInfo,()-> new HashMap<>()).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return aBoolean;
    }


}
