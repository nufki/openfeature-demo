package com.openfeature_demo;

import dev.openfeature.sdk.Client;
import dev.openfeature.sdk.OpenFeatureAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RestHello {
    private final OpenFeatureAPI openFeatureAPI;

    @Autowired
    public RestHello(OpenFeatureAPI OFApi) {
        this.openFeatureAPI = OFApi;
    }

    @GetMapping("/hello")
    public String getHello() {
        final Client client = openFeatureAPI.getClient();

        // Evaluate welcome-message feature flag
        if (client.getBooleanValue("welcome-message", false)) {
            return "Hello, welcome to this OpenFeature-enabled website!";
        }

        return "Hello!";
    }

    @GetMapping("/retries")
    public String getRetries() {
        final Client client = openFeatureAPI.getClient();
        int maxRetries = client.getIntegerValue("max-retries", 5);

        switch (maxRetries) {
            case 3:
                return "Low retries configured (3 attempts).";
            case 5:
                return "Medium retries configured (5 attempts).";
            case 10:
                return "High retries configured (10 attempts).";
            default:
                return "Default retries configured (" + maxRetries + " attempts).";
        }
    }
}
