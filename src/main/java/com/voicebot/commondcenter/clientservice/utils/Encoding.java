package com.voicebot.commondcenter.clientservice.utils;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import org.bson.Document;
import java.net.URLEncoder;

public class Encoding {

    public static void main(String [] args){

        try{
            String username = URLEncoder.encode("jitendrasagoriya", "UTF-8");
            String password = URLEncoder.encode("V01ceCh@tb0t", "UTF-8");
            System.out.println(password);

        } catch(Exception e){
            System.err.println(e.getCause());

        }
    }
}

