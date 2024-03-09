package com.voicebot.commondcenter.clientservice.utils;

import org.apache.commons.lang3.RandomStringUtils;

public class PasswordUtils {
    public static String generate() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789~`!@#$%^&*()-_=+[{]}\\|;:\'\",<.>/?";
        return  RandomStringUtils.random(9, characters);
    }
}
