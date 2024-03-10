package com.voicebot.commondcenter.clientservice.entity;


import com.voicebot.commondcenter.clientservice.enums.AccessType;
import com.voicebot.commondcenter.clientservice.enums.Status;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Digits;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Document(collection = "user")
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User extends AbstractBaseEntity implements Serializable {

    @Transient
    public static final String SEQUENCE_NAME = "user_sequence";

    private Long id;

    @NotBlank( message = "Name should not null or empty.")
    private String name;

    @Indexed(unique = true)
    @NotBlank( message = "Email should not null or empty.")
    @Email(message = "Invalid email address.")
    private String email;

    @Indexed(unique = true)
    @NotBlank( message = "Mobile Number should not null or empty.")
    @Digits(integer = 10, fraction = 0,message = "Mobile Number should be Number only.")
    private String mobileNumber;

    @NotBlank( message = "Email should not null or empty.")
    private String empId;

    private AccessType accessType;

    private Date registrationDate;

    private List<Long> applications;

    private List<Long> services;

    @NotNull( message = "Client should not null or empty.")
    private Long clientId;

    private Status status;

    private Long authenticationId;


}
