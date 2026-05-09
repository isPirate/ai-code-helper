package com.iat.aicodehelper.ai;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.spring.AiService;
import reactor.core.publisher.Flux;

import java.util.List;

public interface AiCodeHelperService {

    @SystemMessage(fromResource = "system-prompt.txt")
    String chat(String userMsg);

    @SystemMessage(fromResource = "system-prompt.txt")
    Report chatWithJsonResponse(String userMsg);

    record Report(String name, List<String> content){};

    // 流式对话
    @SystemMessage(fromResource = "system-prompt.txt")
    Flux<String> chatStream(@MemoryId String memoryId, @UserMessage String userMessage);

}
