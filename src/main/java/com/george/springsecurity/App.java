package com.george.springsecurity;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class App {
    public static void main(String[] args) {
        var original = "Hello, World!";
        var encodedString = Base64.getEncoder()
                .encodeToString(original.getBytes(StandardCharsets.UTF_8));
        System.out.println("Encoded String: " + encodedString);
    }
}
