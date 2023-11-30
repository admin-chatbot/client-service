package com.voicebot.commondcenter.clientservice.utils;

import org.springframework.stereotype.Service;
import java.net.*;
import java.io.*;

@Service
public class URLReader {

    public String getContent(String url) throws IOException {
        StringBuilder stringBuffer = new StringBuilder();
        URL oracle = new URL(url);
        BufferedReader in = new BufferedReader(
                new InputStreamReader(oracle.openStream()));

        String inputLine;
        while ((inputLine = in.readLine()) != null)
            stringBuffer.append(inputLine);
        in.close();

        return stringBuffer.toString();
    }

}
