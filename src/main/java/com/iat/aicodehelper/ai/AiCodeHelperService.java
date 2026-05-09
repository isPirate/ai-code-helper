package com.iat.aicodehelper.ai;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.spring.AiService;

import java.util.List;

public interface AiCodeHelperService {

    @SystemMessage(fromResource = "system-prompt.txt")
    String chat(String userMsg);

    @SystemMessage(fromResource = "system-prompt.txt")
    Report chatWithJsonResponse(String userMsg);

    record Report(String name, List<String> content){};
}
