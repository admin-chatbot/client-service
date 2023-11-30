package com.voicebot.commondcenter.clientservice.discovery.service.impl;

import com.voicebot.commondcenter.clientservice.discovery.service.ApiInformationExtractor;
import com.voicebot.commondcenter.clientservice.discovery.service.model.ResponseMessage;
import com.voicebot.commondcenter.clientservice.discovery.service.model.ServiceDTO;
import com.voicebot.commondcenter.clientservice.discovery.service.model.ServiceParameterDTO;
import com.voicebot.commondcenter.clientservice.discovery.service.model.SwaggerContent;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.parser.OpenAPIV3Parser;

import java.util.*;
import java.util.stream.Collectors;

@org.springframework.stereotype.Service
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

    private ServiceDTO buildService(String type, String s, Operation operation) {
        ServiceDTO service = null;
        if (operation != null) {
            service = new ServiceDTO();

            service.setEndpoint(s);
            System.out.println("Path :"+s);
            System.out.println("*************************");
            if(operation.getRequestBody()!=null)
                    service.setRequestType(Collections.singletonList(String.join(",", operation.getRequestBody().getContent().keySet())));

            String response = "";

            response = operation.getResponses().values().stream().filter(Objects::nonNull).map(
                    apiResponse ->  apiResponse.getContent()!=null?String.join(",", apiResponse.getContent().keySet()):"").collect(Collectors.joining(","));

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

    private ServiceParameterDTO buildServiceParameter(Parameter parameter) {
        if (parameter==null)
            return null;

        Set<String> types = new HashSet<>();
        if(parameter.getContent()!=null) {
            types = (Set<String>) parameter.getContent().values().stream().flatMap(
                    mediaType -> mediaType.getSchema().getTypes().stream()
            ).collect(Collectors.toSet()
            );
        }
        return ServiceParameterDTO.builder()
                .name(parameter.getName())
                .required(parameter.getRequired())
                .description(parameter.getDescription())
                .in(parameter.getIn())
                .paramType(parameter.getIn())
                .type(String.join(",", types))
                .build();
    }


}
