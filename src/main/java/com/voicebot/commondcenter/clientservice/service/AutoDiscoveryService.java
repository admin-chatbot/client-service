package com.voicebot.commondcenter.clientservice.service;

import com.voicebot.commondcenter.clientservice.discovery.service.model.ServiceDocsType;
import com.voicebot.commondcenter.clientservice.entity.Service;

import java.io.IOException;
import java.net.URL;
import java.util.List;

@org.springframework.stereotype.Service
public interface AutoDiscoveryService {

    List<Service> discover(URL url ) throws Exception;

    List<Service> discover(String contents );

    ServiceDocsType findDocsType(String url) throws IOException;

    String getDocsJsonAsString(String url) throws IOException;
}
