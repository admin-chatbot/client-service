package com.voicebot.commondcenter.clientservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories
public class ClientServiceApplication {



	public static void main(String[] args) {
		SpringApplication.run(ClientServiceApplication.class, args);
	}

}
