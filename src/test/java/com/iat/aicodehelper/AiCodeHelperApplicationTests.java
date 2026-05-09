package com.iat.aicodehelper;

import com.iat.aicodehelper.ai.AiCodeHelper;
import com.iat.aicodehelper.ai.AiCodeHelperService;
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

    @Resource
    private AiCodeHelperService aiCodeHelperService;

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

    @Test
    void chatServiceWithSystemPrompt() {
        String aiMessage = aiCodeHelperService.chat("你好,你是谁?你会编程吗?");
        log.info("AI response text: {}", aiMessage);
    }

    @Test
    void chatServiceWithMemory() {
        String aiMessage = aiCodeHelperService.chat("你好,我是曾经的王，King.");
        System.out.println(aiMessage);
        System.out.println("=====================================================================");
        aiMessage = aiCodeHelperService.chat("我是谁？");
        System.out.println(aiMessage);
    }

    @Test
    void chatServiceWithJsonResponse() {
        String userMsg = "你好,我是曾经的王，King,学习编程两年半，请帮我执行学习报告";
        AiCodeHelperService.Report report = aiCodeHelperService.chatWithJsonResponse(userMsg);
        System.out.println(report);
    }
}
