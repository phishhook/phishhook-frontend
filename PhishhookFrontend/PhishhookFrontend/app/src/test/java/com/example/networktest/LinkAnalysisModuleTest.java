package com.example.networktest;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = LinkAnalysisModule.class)
public class LinkAnalysisModuleTest {

    @Autowired
    private LinkAnalysisModule linkAnalysisModule;

    @MockBean
    private RestTemplate restTemplate;

    @Test
    public void testLinkAnalysis() {
        // Mock the behavior of the RestTemplate
        when(restTemplate.getForObject(any(String.class), any(Class.class)))
                .thenReturn("Mocked machine learning result");

        // Perform link analysis
        String result = linkAnalysisModule.linkAnalysis("https://example.com");

        // Verify the result
        assertEquals("Mocked machine learning result", result);
    }
}
