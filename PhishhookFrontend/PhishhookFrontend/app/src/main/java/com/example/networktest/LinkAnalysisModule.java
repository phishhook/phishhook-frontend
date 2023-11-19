package com.example.networktest;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class LinkAnalysisModule {


    private String mlApiUrl = "http://ec2-34-226-215-44.compute-1.amazonaws.com:80";

    private final RestTemplate restTemplate;

    public LinkAnalysisModule(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String linkAnalysis(String url) {
        // Make a request to the machine learning API
        String apiUrl = mlApiUrl + "?url=" + url;
        return restTemplate.getForObject(apiUrl, String.class);
    }
}
