package com.liupeidong.spring.conf;

import com.liupeidong.spring.utils.MD5;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class CustomPasswordEncoder implements PasswordEncoder {

    @Override
    public String encode(CharSequence rawPassword) {
        return MD5.md5Password(rawPassword.toString());
    }

    @Override
    public boolean matches(CharSequence rawPassword, String recordPassword) {
        return MD5.md5Password(rawPassword.toString()).equals(recordPassword.toLowerCase()) ||
                rawPassword.toString().equalsIgnoreCase(MD5.md5Password(recordPassword)) ||
                rawPassword.toString().equalsIgnoreCase(recordPassword);
    }
}
