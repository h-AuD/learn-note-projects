package AuD.template.project.test;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Description: 编码,解码 测试
 *
 * @author AuD/胡钊
 * @ClassName Encode_test
 * @date 2021/4/27 9:51
 * @Version 1.0
 */
@Slf4j
public class Encode_test {

    /**
     * 字符编码
     * - 英文字母和数字不受编码格式影响 (可测)
     * @throws IOException
     */
    @Test
    public void strEncode() throws IOException {
        String source = "log4j==胡钊";
        // 将字符编码 === 使用UTF-8
        byte[] bytes = source.getBytes(StandardCharsets.UTF_8);

        // 使用不同的编码格式解码 ====
        System.out.println(new String(bytes, "GBK"));
        System.out.println(new String(bytes, "UTF-8"));

        System.out.println(System.getProperties());
    }
}
