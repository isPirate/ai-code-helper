package com.iat.aicodehelper;

import com.iat.aicodehelper.ai.AiCodeHelper;
import dev.langchain4j.data.message.ImageContent;
import dev.langchain4j.data.message.TextContent;
import dev.langchain4j.data.message.UserMessage;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AiCodeHelperApplicationTests {

    private static final Logger log = LoggerFactory.getLogger(AiCodeHelperApplicationTests.class);
    @Resource
    private AiCodeHelper aiCodeHelper;

    @Test
    void chat() {
        String response = aiCodeHelper.chat("你是什么模型");
        log.info("AI response text: {}", response);
    }

    @Test
    void chatWithImage() {
        UserMessage userMessage = UserMessage.from(TextContent.from("描述图片："),
                ImageContent.from("https://www.codefather.cn/logo.png"));
        aiCodeHelper.chatWithImage(userMessage);
    }

    @Test
    void chatWithSystemPrompt() {
        aiCodeHelper.chatWithSystemPrompt("你好,你是谁");
    }
}
