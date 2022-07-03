package AuD.template.project.core.function.datax;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.function.Supplier;

/**
 * Description: DataX组件执行器. <br>
 *
 *
 *
 * @author AuD/胡钊
 * @ClassName DataXComponent
 * @date 2021/6/24 16:41
 * @Version 1.0
 */
@Slf4j
@Component
public class DataXExecutor {

    @Value("${datax.py.location}")
    private String dataXPyLocation;

    @Value("${datax.job.path}")
    private String dataXJobDir;

    @Value("${datax.handle.common.log.path}")
    private String dataXProcessCommonLogPath;

    /*
     * 1. datax.py位置 /data/datax/bin/datax.py
     * 2. datax执行结果日志目录 /data/databridge/datax_log/common & /data/databridge/datax_log/error
     */
    /** 执行datax脚本,入参需要传入待执行的脚本位置 */
    @Async
    public Future<Boolean> processing(String templateNameWithoutExtension,DataXTemplateInfo dataXTemplateInfo,Supplier<Map<String,String>> supplier){
        Process process = null;
        File jobFile = null;
        boolean result = false;
        try {
            jobFile = generateJob(templateNameWithoutExtension,dataXTemplateInfo,supplier);
            /* python datax.py /data/datax/job/csv2sqlserver.json */
            String command = "python "+ dataXPyLocation+" "+jobFile.getAbsolutePath();
            log.info("================= job_{} 开始执行,执行命令:{} ===============",templateNameWithoutExtension,command);
            process = Runtime.getRuntime().exec(command);
            // 生成日志
            String logPath = dataXProcessCommonLogPath + File.separator + templateNameWithoutExtension.replace("_template", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))+".log");
            handleLogFile(process.getInputStream(),logPath);
            process.waitFor();
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("执行job({})发生异常,异常信息:{}",templateNameWithoutExtension,e.getMessage());
        }finally {
            if (process != null){
                process.destroy();
            }
            if (jobFile.exists()){
                jobFile.delete();
            }
            log.info("================= job_{} 执行结束,执行结果:{} (true:success,false:fail) ===============",templateNameWithoutExtension,result);
            return AsyncResult.forValue(result);
        }
    }



    private File generateJob(String templateNameWithoutExtension, DataXTemplateInfo dataXTemplateInfo, Supplier<Map<String,String>> supplier) throws IOException {
        Map<String,String> contentMap = dataXTemplateInfo.getContent();
        contentMap.putAll(supplier.get());
        String job = dataXJobDir+File.separator+templateNameWithoutExtension.replace("_template",".json");
        File jobFile = new File(job);
        if(jobFile.exists()){
            jobFile.delete();
        }
        // === 读取job模板文件内容,生成job文件,循环效率太低了
        List<String> list = Files.readAllLines(Paths.get(dataXJobDir + File.separator + templateNameWithoutExtension + ".json"), StandardCharsets.UTF_8);
        List<String> list2 = new ArrayList<>(list.size());
        list.forEach(line -> contentMap.forEach((s, s2) -> list2.add(line.indexOf(s)!=-1?line.replace(s,s2):line)));
        Files.write(Paths.get(job),list2, StandardCharsets.UTF_8);
        return jobFile;
    }



    /** 处理进程日志保存文件 */
    private void handleLogFile(InputStream inputStream, String logPath) throws IOException {
        if (inputStream != null) {
            Files.copy(inputStream,Paths.get(logPath));
            log.info("======== datax执行日志_{}输出完成 ==============",logPath);
        }
    }

}
