package com.example.errorstory.ai;

import org.springframework.stereotype.Component;
import java.net.InetAddress;

@Component
public class InternetChecker {

    public boolean isInternetAvailable() {
        try {
            InetAddress address = InetAddress.getByName("google.com");
            return address.isReachable(3000);
        } catch (Exception e) {
            return false;
        }
    }
}
