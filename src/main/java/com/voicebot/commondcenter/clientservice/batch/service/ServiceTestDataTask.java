package com.voicebot.commondcenter.clientservice.batch.service;


import com.voicebot.commondcenter.clientservice.entity.Application;
import com.voicebot.commondcenter.clientservice.entity.Client;
import com.voicebot.commondcenter.clientservice.entity.Service;
import com.voicebot.commondcenter.clientservice.entity.ServiceLog;
import com.voicebot.commondcenter.clientservice.enums.ServiceLogStatus;
import com.voicebot.commondcenter.clientservice.service.ApplicationService;
import com.voicebot.commondcenter.clientservice.service.ClientService;
import com.voicebot.commondcenter.clientservice.service.ServiceLogService;
import com.voicebot.commondcenter.clientservice.service.impl.ServiceServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Random;

@Component
public class ServiceTestDataTask {

    @Autowired
    private ServiceLogService serviceLogService;

    @Autowired
    private ServiceServiceImpl serviceService;

    @Autowired
    private ApplicationService applicationService;

    @Autowired
    private ClientService clientService;

    Random random = new Random(3);
    @Scheduled(cron = "*/45 */45 * * * *")
    public void task() {

        List<Client> clients = clientService.findAll();
        if(clients!=null){
            for (Client client:clients) {
                System.out.println("Client Name :"+ client.getClientName());
                List<Application> applications = applicationService.findByClint(client.getId());
                if (applications!=null){
                    for (Application application:applications) {
                        System.out.println("Application Name :"+ application.getName());
                        List<Service> services = serviceService.findAllByApplicationId(application.getId());
                        if (services!=null) {
                            for (Service service:services) {
                                if (service!=null) {
                                    System.out.println("Service Name :"+ service.getName());
                                    int success = random.nextInt(3);
                                    int fail = random.nextInt(3);

                                    System.out.println("SUCCESS "+ success);
                                    System.out.println("FAILED "+ fail);

                                    int successCount = 0;
                                    for (int i=0;i<=success;i++) {
                                        ServiceLog serviceLog = getServiceLog(client, application, service,ServiceLogStatus.SUCCESS);

                                        serviceLog.setCreatedTimestamp(new Date(System.currentTimeMillis()));
                                        serviceLog.setCreatedUserId("ServiceTestDataTask");

                                       ServiceLog serviceLog1 = serviceLogService.save(serviceLog);
                                       successCount++;

                                    }

                                    System.out.println("Service Log Added For Success: "+ successCount);

                                    int failCount = 0;
                                    for (int i=0;i<fail;i++) {
                                        ServiceLog serviceLog = getServiceLog(client, application, service,ServiceLogStatus.FAIL);

                                        serviceLog.setCreatedTimestamp(new Date(System.currentTimeMillis()));
                                        serviceLog.setCreatedUserId("ServiceTestDataTask");


                                        ServiceLog serviceLog1 = serviceLogService.save(serviceLog);

                                    }
                                    System.out.println("Service Log Added For Failed: "+ failCount);
                                }
                            }
                        }
                    }
                }
            }
        }


        ServiceLog serviceLog = ServiceLog.builder()
                .serviceEndpoint("")
                .build();
        System.out.println("ServiceTestDataTask");
    }

    private static ServiceLog getServiceLog(Client client, Application application, Service service,ServiceLogStatus status) {
        return ServiceLog.builder()
                .serviceEndpoint(service.getEndpoint())
                .serviceName(service.getName())
                .method(service.getMethod())
                .status(status)
                .response("This is test response")
                .application(application.getId())
                .client(client.getId())
                .user("TEST")
                .build();
    }

}
