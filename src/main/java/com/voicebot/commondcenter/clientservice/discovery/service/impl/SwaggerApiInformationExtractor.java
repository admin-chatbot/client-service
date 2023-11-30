package com.voicebot.commondcenter.clientservice.discovery.service.impl;

import com.voicebot.commondcenter.clientservice.discovery.service.ApiInformationExtractor;
import com.voicebot.commondcenter.clientservice.discovery.service.model.ResponseMessage;
import com.voicebot.commondcenter.clientservice.discovery.service.model.ServiceDTO;
import com.voicebot.commondcenter.clientservice.discovery.service.model.SwaggerContent;

import io.swagger.models.Swagger;
import io.swagger.parser.SwaggerParser;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

@org.springframework.stereotype.Service
public class SwaggerApiInformationExtractor implements ApiInformationExtractor<Swagger> {

    private Swagger swagger;



    @Override
    public void loadDocs(String url) {
        if(swagger==null)
            swagger = new SwaggerParser().read(url);
    }

    @Override
    public Swagger loadDocsFromString(String url) {
        return null;
    }

    @Override
    public SwaggerContent buildServices() throws Exception {
        if (swagger==null)
            throw new Exception("Swagger is null.");

        SwaggerContent swaggerContent = new SwaggerContent();
        swaggerContent.setApiVersion(swagger.getInfo().getVersion());
        swaggerContent.setBasePath("http://"+swagger.getHost()+swagger.getBasePath());
        swaggerContent.setProduces(swagger.getProduces());

        List<ServiceDTO> services = new ArrayList<>();

        swagger.getPaths().forEach((s, path) -> {

            ServiceDTO service = new ServiceDTO();
            service.setEndpoint(s);

            System.out.println("Path :"+s);
            System.out.println("*************************");
            path.getOperationMap().forEach((httpMethod, operation) -> {
                service.setMethod(httpMethod.name());
                service.setName(operation.getOperationId());
                service.setResponseType(operation.getProduces());
                service.setResponseForInvalidRequest(new ArrayList<>());
                service.setSummary(operation.getSummary());
                service.setRequestType(operation.getConsumes());

                operation.getResponses().forEach((s1, response) -> {
                    if(!StringUtils.isEmpty(s1))
                        service.getResponseForInvalidRequest().add(ResponseMessage.builder().code(s1).message(response.getDescription()).build());
                });


                service.setParameters(new ArrayList<>());
                operation.getParameters().forEach(parameter -> {

                    service.getParameters().add(SwaggerParameterExtractor.fetchParameter(parameter));

                });

            });
            services.add(service);
        });
        swaggerContent.setServices(services);
        return swaggerContent;
    }

    public static void main(String[] args) throws Exception {
        SwaggerApiInformationExtractor swaggerApiInformationExtractor = new SwaggerApiInformationExtractor();
        swaggerApiInformationExtractor.loadDocs("D:\\VOICE-BOT\\swagger2.json");
        System.out.println( swaggerApiInformationExtractor.buildServices());
    }
}
