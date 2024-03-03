package com.voicebot.commondcenter.clientservice.entity;


import com.voicebot.commondcenter.clientservice.enums.UserType;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Document(collection = "authentication")
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Authentication extends AbstractBaseEntity implements Serializable {
    @Transient
    public static final String SEQUENCE_NAME = "authentication_sequence";
    @Id
    private Long id;

    @NotBlank(message = "User Name is mandatory.")
    private String userName;

    @NotBlank(message = "Password is mandatory.")
    private String password;

    private String mobileNumber;

    private java.util.Date lastLogin;

    private String token;

    private java.util.Date expire;

    private UserType userType;

    @Transient
    private Long entityId;

    @Transient
    private String name;
}
