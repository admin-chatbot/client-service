package com.voicebot.commondcenter.clientservice.exception;

public class TokenNotFoundException extends Exception{
    public TokenNotFoundException() {
        super("Token not found.");
    }

    public TokenNotFoundException(String token) {
        super(token);
    }
}
