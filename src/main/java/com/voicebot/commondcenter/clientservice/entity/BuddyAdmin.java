package com.voicebot.commondcenter.clientservice.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Date;

@Document(collection = "buddyadmin")
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BuddyAdmin extends AbstractBaseEntity implements Serializable {

    @Transient
    public static final String SEQUENCE_NAME = "buddy_admin_sequence";
    @Id
    private Long id;

    @NotBlank(message = "Name should not null or empty.")
    private String name;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", locale = "en-PH")
    private Date registerDate;

    @Indexed(unique = true)
    @Email(message = "Please provide valid email.")
    private String email;

    private String address;

    @NotBlank(message = "Contact Details is mandatory.")
    private String contactNumber;

    @NotBlank(message = "Id should not null or empty.")
    private Long authenticationId;
}
