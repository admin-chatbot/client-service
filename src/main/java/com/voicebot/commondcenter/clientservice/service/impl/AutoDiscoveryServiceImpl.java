package com.voicebot.commondcenter.clientservice.service.impl;

import com.voicebot.commondcenter.clientservice.discovery.service.ApiInformationExtractor;
import com.voicebot.commondcenter.clientservice.discovery.service.impl.OpenApiInformationExtractor;
import com.voicebot.commondcenter.clientservice.discovery.service.impl.SwaggerApiInformationExtractor;
import com.voicebot.commondcenter.clientservice.discovery.service.model.ServiceDTO;
import com.voicebot.commondcenter.clientservice.discovery.service.model.ServiceDocsType;
import com.voicebot.commondcenter.clientservice.discovery.service.model.ServiceParameterDTO;
import com.voicebot.commondcenter.clientservice.discovery.service.model.SwaggerContent;
import com.voicebot.commondcenter.clientservice.entity.Service;
import com.voicebot.commondcenter.clientservice.entity.ServiceParameter;
import com.voicebot.commondcenter.clientservice.service.AutoDiscoveryService;
import com.voicebot.commondcenter.clientservice.utils.URLReader;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.net.URL;
import java.util.*;

@org.springframework.stereotype.Service
public class AutoDiscoveryServiceImpl implements AutoDiscoveryService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AutoDiscoveryServiceImpl.class);

    @Autowired
    private URLReader urlReader;

    private String baseURL;

    @Override
    public List<Service> discover(URL url ) throws Exception {
        ServiceDocsType docsType = findDocsType(String.valueOf(url));

        ApiInformationExtractor<?> apiInformationExtractor = null;
        if(docsType.equals(ServiceDocsType.OPENAPI))
            apiInformationExtractor = new OpenApiInformationExtractor();
        else if(docsType.equals(ServiceDocsType.SWAGGER))
            apiInformationExtractor = new SwaggerApiInformationExtractor();

        SwaggerContent swaggerContent = null;

        if(apiInformationExtractor!=null) {
            apiInformationExtractor.loadDocs(String.valueOf(url));
            swaggerContent = apiInformationExtractor.buildServices();
        }

        if(swaggerContent!=null) {
            this.baseURL = swaggerContent.getBasePath();
            if (swaggerContent.getServices()!=null)
                return swaggerContent.getServices()
                        .stream()
                            .filter(Objects::nonNull)
                            .map(this::mapper)
                        .toList();
        }
        return null;
    }

    @Override
    public List<Service> discover(String contents ) {
        return null;
    }

    @Override
    public ServiceDocsType findDocsType(String url) throws IOException {
        String urlContents = getDocsJsonAsString(url);
        if(StringUtils.contains( urlContents,"openapi"))
            return ServiceDocsType.OPENAPI;

        if(StringUtils.contains( urlContents,"swagger"))
            return ServiceDocsType.SWAGGER;

        return null;
    }

    @Override
    public String getDocsJsonAsString(String url) throws IOException {
        return urlReader.getContent(url);
    }

    private Service mapper(ServiceDTO serviceDTO) {
        ArrayList<ServiceParameter> serviceParameters = null;
        if(serviceDTO.getParameters()!=null)
            serviceParameters = new ArrayList<>(serviceDTO
                    .getParameters()
                    .stream()
                        .filter(Objects::nonNull)
                        .map(this::parameterMapper)
                    .toList());


        Set<String> requestTypesSet = new HashSet<>();
        if (serviceDTO.getRequestType()!=null) {
            serviceDTO.getRequestType().forEach(s -> {
                String[] reqTypes = StringUtils.split(s,",");
                requestTypesSet.addAll(Arrays.asList(reqTypes));
            });
        }


        Set<String> responseTypesSet = new HashSet<>();
        if (serviceDTO.getResponseType()!=null){
            System.out.println(serviceDTO.getResponseType().size());
            serviceDTO.getResponseType().forEach(s -> {
                String[] reqTypes = StringUtils.split(s,",");
                responseTypesSet.addAll(Arrays.asList(reqTypes));
            });
        }


        System.out.println("RequestTypesSet :"+requestTypesSet);
        System.out.println("ResponseTypesSet :"+responseTypesSet);

         return Service.builder()
                     .name(serviceDTO.getName())
                     .authorization(serviceDTO.getAuthorization())
                     .requestType( new ArrayList<>(requestTypesSet))
                     .method(serviceDTO.getMethod())
                     .serviceParameters(serviceParameters)
                     .summary(serviceDTO.getSummary())
                     .endpoint(this.baseURL+ serviceDTO.getEndpoint())
                     .responseType( new ArrayList<>(responseTypesSet))
                     .responseForInvalidRequest(serviceDTO.getResponseForInvalidRequest())
                 .build();
    }

    private ServiceParameter parameterMapper(ServiceParameterDTO parametersDTOs) {
        if(parametersDTOs==null)
            return null;

        return ServiceParameter.builder()
                    .in(parametersDTOs.getIn())
                    .description(parametersDTOs.getDescription())
                    .type(parametersDTOs.getType())
                    .required(parametersDTOs.getRequired())
                    .paramType(parametersDTOs.getParamType())
                    .name(parametersDTOs.getName())
                .build();

    }
}
