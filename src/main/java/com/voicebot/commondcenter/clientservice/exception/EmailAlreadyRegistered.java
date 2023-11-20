package com.voicebot.commondcenter.clientservice.exception;

public class EmailAlreadyRegistered extends Exception{
    public EmailAlreadyRegistered() {
        super("Email is already registered with us.");
    }
}
