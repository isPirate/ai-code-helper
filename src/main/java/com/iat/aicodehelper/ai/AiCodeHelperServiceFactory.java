package com.iat.aicodehelper.ai;

import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.StreamingChatModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.spring.AiService;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AiCodeHelperServiceFactory {

    @Resource
    private ChatModel deepseekChatModel;
    @Resource
    private StreamingChatModel deepseekStreamingChatModel;

    @Bean
    public AiCodeHelperService create() {
        AiCodeHelperService service = AiServices.builder(AiCodeHelperService.class)
                .chatModel(deepseekChatModel)
                .streamingChatModel(deepseekStreamingChatModel)
                .chatMemoryProvider(memoryId -> MessageWindowChatMemory.withMaxMessages(10))
                .build();
        return service;
    }

}
