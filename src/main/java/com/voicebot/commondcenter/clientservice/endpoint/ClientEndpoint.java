package com.voicebot.commondcenter.clientservice.endpoint;

import com.voicebot.commondcenter.clientservice.entity.Client;
import com.voicebot.commondcenter.clientservice.service.ClientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/client/" )
public class ClientEndpoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClientEndpoint.class);

    @Autowired
    private ClientService clientService;


    @GetMapping
    public ResponseEntity<?> getAllClient() {
        try {
            List<Client> clients = clientService.findAll();
            return ResponseEntity.ok(clients);
        }catch (Exception exception){
            LOGGER.error("",exception);
            return ResponseEntity
                    .internalServerError()
                    .body(exception.getMessage());
        }
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> save(@RequestBody Client client) {
        try {
            LOGGER.info("Client {} ",client);
            Client c =  clientService.save(client);
            return ResponseEntity.ok(c);
        }catch (Exception exception) {
            LOGGER.error("",exception);
            return ResponseEntity
                    .internalServerError()
                    .body(exception.getMessage());
        }
    }
}
