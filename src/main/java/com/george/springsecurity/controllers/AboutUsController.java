package com.george.springsecurity.controllers;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/about_us")
public class AboutUsController {

    @GetMapping
    public Map<String, String> about() {
        //business logic
        return Collections.singletonMap("msj", "about");
    }
}
