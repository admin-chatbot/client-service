package com.voicebot.commondcenter.clientservice.exception;

public class ServiceNotFoundException extends Exception{
    public ServiceNotFoundException() {
        super("Service not found.");
    }

    public ServiceNotFoundException(String message) {
        super("Service not found. " + message);
    }
}
