package com.atul.sportsmanagement.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@AllArgsConstructor
public class MailContentBuilder {

    private final TemplateEngine templateEngine;

    public String build(String message) {
        Context context = new Context();
        context.setVariable("message", message);
        return templateEngine.process("mailTemplate", context);
    }
    public String buildHtml(String name,String team,String event,String url) {
        Context context = new Context();
        context.setVariable("name",name);
        context.setVariable("team",team);
        context.setVariable("event",event);
        context.setVariable("url",url);

        return templateEngine.process("memberRequest", context);
    }

}