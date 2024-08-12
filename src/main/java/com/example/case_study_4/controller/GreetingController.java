package com.example.case_study_4.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Locale;

@RestController
public class GreetingController {

    @Autowired
    private MessageSource messageSource;

    @GetMapping("/api/greet")
    public String greet(@RequestParam(name = "name", defaultValue = "Guest") String name) {
        String greetingMessage = messageSource.getMessage("greeting", new Object[]{name}, LocaleContextHolder.getLocale());
        return greetingMessage;
    }
}
