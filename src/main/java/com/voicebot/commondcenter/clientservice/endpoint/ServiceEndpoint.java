package com.voicebot.commondcenter.clientservice.endpoint;

import com.voicebot.commondcenter.clientservice.entity.Client;
import com.voicebot.commondcenter.clientservice.entity.Service;
import com.voicebot.commondcenter.clientservice.service.ClientService;
import com.voicebot.commondcenter.clientservice.service.ServiceService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/service/" )
@Tag(name = "Service", description = "Service Management APIs")
public class ServiceEndpoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceEndpoint.class);

    @Autowired
    private ServiceService serviceService;


    @GetMapping(path="{clientId}/")
    public ResponseEntity<?> getAllServiceByClientId(@PathVariable(name="clientId") String clientId) {
        try {
            List<Service> services = serviceService.findAllByClientId(Long.getLong(clientId));
            return ResponseEntity.ok(services);
        }catch (Exception exception){
            LOGGER.error("",exception);
            return ResponseEntity
                    .internalServerError()
                    .body(exception.getMessage());
        }
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> save(@RequestBody @Valid Service service) {

        try {
            LOGGER.info("Client {} ",service);
            Service c =  serviceService.save(service);
            return ResponseEntity.ok(c);
        }catch (Exception exception) {
            LOGGER.error("",exception);
            return ResponseEntity
                    .internalServerError()
                    .body(exception.getMessage());
        }
    }
}
