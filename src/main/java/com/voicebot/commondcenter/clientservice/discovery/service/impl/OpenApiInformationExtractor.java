package com.voicebot.commondcenter.clientservice.discovery.service.impl;

import com.voicebot.commondcenter.clientservice.discovery.service.ApiInformationExtractor;
import com.voicebot.commondcenter.clientservice.discovery.service.model.ResponseMessage;
import com.voicebot.commondcenter.clientservice.discovery.service.model.Service;
import com.voicebot.commondcenter.clientservice.discovery.service.model.ServiceParameter;
import com.voicebot.commondcenter.clientservice.discovery.service.model.SwaggerContent;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.parser.OpenAPIV3Parser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class OpenApiInformationExtractor implements ApiInformationExtractor<OpenAPI> {

    private OpenAPI openAPI;

    @Override
    public void loadDocs(String url) {
        if (openAPI == null)
            openAPI = new OpenAPIV3Parser().read(url);

    }

    @Override
    public OpenAPI loadDocsFromString(String url) {
        return null;
    }

    @Override
    public SwaggerContent buildServices() throws Exception {
        if (openAPI==null)
            throw new Exception("OpenApi is null.");

        SwaggerContent swaggerContent = new SwaggerContent();
        swaggerContent.setApiVersion(openAPI.getOpenapi());
        swaggerContent.setBasePath(openAPI.getServers().get(0).getUrl());

        swaggerContent.setServices(new ArrayList<>());
        openAPI.getPaths().forEach((s, pathItem) -> {
            if(pathItem.getGet()!=null)
              swaggerContent.getServices().add(buildService("GET",s,pathItem.getGet()));
            if(pathItem.getPost()!=null)
                swaggerContent.getServices().add(buildService("POST",s,pathItem.getGet()));
            if(pathItem.getPut()!=null)
                swaggerContent.getServices().add(buildService("PUT",s,pathItem.getGet()));
            if(pathItem.getDelete()!=null)
                swaggerContent.getServices().add(buildService("DELETE",s,pathItem.getGet()));
        });


        return swaggerContent;
    }

    private Service buildService(String type,String s,Operation operation) {
        Service service = null;
        if (operation != null) {
            service = new Service();

            service.setEndpoint(s);
            System.out.println("Path :"+s);
            System.out.println("*************************");
            service.setRequestType(Collections.singletonList(String.join(",", operation.getRequestBody().getContent().keySet())));
            String response = operation.getResponses().values().stream().map(
                    apiResponse -> String.join(",", apiResponse.getContent().keySet())).collect(Collectors.joining(","));
            service.setResponseType(Collections.singletonList(response));
            service.setMethod(type);
            service.setName(operation.getOperationId());
            service.setSummary(operation.getSummary());
            service.setResponseForInvalidRequest(new ArrayList<>());

            List<ResponseMessage> responseMessages = operation.getResponses().entrySet().stream().map(stringApiResponseEntry -> ResponseMessage.builder().
                    code(stringApiResponseEntry.getKey())
                    .message(stringApiResponseEntry.getValue().getDescription())
                    .build()).toList();
            service.getResponseForInvalidRequest().addAll( responseMessages );

            service.setParameters(new ArrayList<>());

            service.getParameters().addAll(  operation.getParameters().stream().map(this::buildServiceParameter).toList());
        }
        return service;
    }

    private ServiceParameter buildServiceParameter(Parameter parameter) {
        if (parameter==null)
            return null;

        Set<String> types = (Set<String>) parameter.getContent().values().stream().flatMap(
                mediaType -> mediaType.getSchema().getTypes().stream()
        ).collect(Collectors.toSet()
        );

        return ServiceParameter.builder()
                .name(parameter.getName())
                .required(parameter.getRequired())
                .discription(parameter.getDescription())
                .in(parameter.getIn())
                .paramType(parameter.getIn())
                .type(String.join(",", types))
                .build();
    }

    public static void main(String[] args) throws Exception {
        OpenApiInformationExtractor openApiInformationExtractor = new OpenApiInformationExtractor();
        openApiInformationExtractor.loadDocs("D:\\VOICE-BOT\\swagger.json");
        System.out.println( openApiInformationExtractor.buildServices());
    }
}
