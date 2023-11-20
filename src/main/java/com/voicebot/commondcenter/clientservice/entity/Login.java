package com.voicebot.commondcenter.clientservice.entity;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Login {

    @Email(message = "Please provide valid email.")
    private String email;

    @NotBlank(message = "Password is mandatory.")
    private String password;
}
