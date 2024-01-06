package com.voicebot.commondcenter.clientservice.endpoint;

import com.voicebot.commondcenter.clientservice.dto.ResponseBody;
import com.voicebot.commondcenter.clientservice.entity.Client;
import com.voicebot.commondcenter.clientservice.entity.Login;
import com.voicebot.commondcenter.clientservice.exception.EmailAlreadyRegistered;
import com.voicebot.commondcenter.clientservice.exception.InvalidUserNameAndPassword;
import com.voicebot.commondcenter.clientservice.service.ClientService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(path = "/api/v1/auth/",produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin
@Tag(name = "Authentication", description = "Authentication APIs")
public class AuthenticationEndpoint {

    @Autowired
    private ClientService clientService;

    @PostMapping(path = "register/",consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = String.class),mediaType = MediaType.APPLICATION_JSON_VALUE) }),
            @ApiResponse(responseCode = "400", content = { @Content(schema = @Schema(implementation = String.class),mediaType = MediaType.APPLICATION_JSON_VALUE) }) ,
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema(implementation = String.class),mediaType = MediaType.APPLICATION_JSON_VALUE) })
    })
    private ResponseEntity<?> register(@Valid @RequestBody Client client) {
        try {
           Client registeredClient =   clientService.register(client);
            return ResponseEntity.ok(ResponseBody.builder()
                    .message("Client is successfully registered with us.")
                    .code(HttpStatus.OK.value())
                    .data(registeredClient)
                    .build());
        }catch (EmailAlreadyRegistered emailAlreadyRegistered) {
            return  ResponseEntity.badRequest().body(emailAlreadyRegistered.getMessage());
        }
        catch (Exception exception) {
            return ResponseEntity.internalServerError().body(exception.getMessage());
        }

    }


    @PostMapping(path = "login/",consumes = MediaType.APPLICATION_JSON_VALUE)
    private ResponseEntity<?> login(@Valid @RequestBody Login login) {
        try {
            Client client = clientService.login(login);

            Client partialClient = Client.builder()
                    .id(client.getId())
                    .token(client.getToken())
                    .email(client.getEmail())
                    .clientName(client.getClientName())
                    .build();
            return  ResponseEntity.ok().body(ResponseBody.builder().data(partialClient).build());
        }catch (InvalidUserNameAndPassword invalidUserNameAndPassword){
            return  ResponseEntity.badRequest().body(invalidUserNameAndPassword.getMessage());
        }catch (Exception exception) {
            exception.printStackTrace();
            return ResponseEntity.internalServerError().body(exception.getMessage());
        }
    }


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}
