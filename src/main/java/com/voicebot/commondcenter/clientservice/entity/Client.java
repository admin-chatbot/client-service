package com.voicebot.commondcenter.clientservice.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.voicebot.commondcenter.clientservice.enums.Status;
import com.voicebot.commondcenter.clientservice.utils.ObjectIdJsonSerializer;
import jakarta.validation.constraints.*;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Document(collection = "client")
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Client extends AbstractBaseEntity implements  Serializable {

    @Transient
    public static final String SEQUENCE_NAME = "client_sequence";

    @Id
    private Long id;

    @NotBlank(message = "Client Name should not null or empty.")
    private String clientName;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", locale = "en-PH")
    private Date registerDate;

    @Indexed(unique = true)
    @Email(message = "Please provide valid email.")
    private String email;

    @NotBlank(message = "Password is mandatory.")
    private String password;

    @NotBlank(message = "Address is mandatory.")
    private String address;

    @NotBlank(message = "Contact Details is mandatory.")
    private String contactNumber;

    private Double turnover;

    private int employeeCount;

    private Status status;

    private String gstNumber;

    private List<ContactPerson> contactPerson;

    private java.util.Date lastLogin;

    private String token;

    private java.util.Date expire;

    @Transient
    private ArrayList<Service> services;

}
