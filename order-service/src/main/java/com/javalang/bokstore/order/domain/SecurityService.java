package com.javalang.bokstore.order.domain;

import org.springframework.stereotype.Service;

@Service
public class SecurityService {

    public String getLoginUsername() {
        return "user";
    }
}
