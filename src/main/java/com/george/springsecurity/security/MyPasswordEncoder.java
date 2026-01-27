package com.george.springsecurity.security;

import org.jspecify.annotations.Nullable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

//@Component
public class MyPasswordEncoder /*implements PasswordEncoder */{
    /*
    public String encode(CharSequence rawPassword) {
        return String.valueOf(rawPassword.toString().hashCode());
    }
    */

    /*
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        var passwordAsString= String.valueOf(rawPassword.toString().hashCode());
        return encodedPassword.equals(passwordAsString);
    }

*/
}
