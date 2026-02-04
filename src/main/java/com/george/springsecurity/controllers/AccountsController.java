package com.george.springsecurity.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/accounts")
public class AccountsController {

    @PreAuthorize("hasAnyAuthority('VIEW_ACCOUNT','VIEW_CARDS')")
    @GetMapping
    public Map<String, String> accounts() {
        //business logic
        return Collections.singletonMap("msj", "accounts");
    }
}
