package com.voicebot.commondcenter.clientservice.discovery.service;



import com.voicebot.commondcenter.clientservice.discovery.service.model.SwaggerContent;

@org.springframework.stereotype.Service
public interface ApiInformationExtractor<I> {



    public void loadDocs(String url);

    public I loadDocsFromString(String url);

    public SwaggerContent buildServices() throws Exception;




}
