package com.backend.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.backend.model.Message;

@Service
public class ExternalApiService {
	private final RestClient restClient;

    public ExternalApiService(RestClient.Builder builder) {
        this.restClient = builder.baseUrl("http://18.218.229.53:5000/chat").build();
    }

    public String getDataFromExternalApi() {
        return restClient.get()
                .uri("/data")
                .retrieve()
                .body(String.class);
    }
    
    public String addMessage(Message mess) {
        return restClient.post()
                .uri("/message") // The path will be appended to the base URL
                .body(mess) // The Java object is automatically serialized to JSON
                .retrieve()
                .body(String.class); // Specify the expected response type
    }

}
