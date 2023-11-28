package com.voicebot.commondcenter.clientservice.discovery.service.impl;

import com.voicebot.commondcenter.clientservice.discovery.service.model.ServiceParameter;
import io.swagger.models.parameters.*;

public class SwaggerParameterExtractor {



    public static ServiceParameter fetchParameter(Parameter parameter) {
        if(parameter instanceof BodyParameter){
            BodyParameter bodyParameter = (BodyParameter) parameter;
            return createParameter(bodyParameter,null);
        } else if (parameter instanceof RefParameter) {
            RefParameter refParameter = (RefParameter)parameter;
            return createParameter(refParameter,null);
        } else if (parameter instanceof CookieParameter) {
            CookieParameter cookieParameter = (CookieParameter)parameter;
            return createParameter(cookieParameter,cookieParameter.getType());
        } else if (parameter instanceof FormParameter) {
            FormParameter formParameter = (FormParameter)parameter;
            return createParameter(formParameter,formParameter.getType());
        } else if (parameter instanceof HeaderParameter) {
            HeaderParameter headerParameter = (HeaderParameter)parameter;
            return createParameter(headerParameter,headerParameter.getType());
        } else if (parameter instanceof PathParameter) {
            PathParameter pathParameter = (PathParameter)parameter;
            return createParameter(pathParameter,pathParameter.getType());
        } else if (parameter instanceof QueryParameter) {
            QueryParameter queryParameter = (QueryParameter)parameter;
            return createParameter(queryParameter,queryParameter.getType());
        }
        return null;
    }




    private static  ServiceParameter createParameter(Parameter parameter,
                                                     String type) {
        return ServiceParameter.builder()
                .name(parameter.getName())
                .required(parameter.getRequired())
                .discription(parameter.getDescription())
                .in(parameter.getIn())
                .paramType(parameter.getIn())
                .type(type)
                .build();
    }
}
