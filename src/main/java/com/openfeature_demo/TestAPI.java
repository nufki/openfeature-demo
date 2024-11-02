package com.openfeature_demo;

import dev.openfeature.sdk.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@RestController
public class TestAPI {
    private final OpenFeatureAPI openFeatureAPI;

    @Autowired
    public TestAPI(OpenFeatureAPI OFApi) {
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

    @GetMapping("/isColorYellow")
    public String isColorYellow(@RequestParam(value = "color", defaultValue = "black") String color) {
        final Client client = openFeatureAPI.getClient();
        MutableContext context = new MutableContext();
        context.add("color", color);
        boolean feature = client.getBooleanValue("isColorYellow", false, context);
        return "Feature enabled: " + feature;
    }

    @GetMapping("/isNewFeatureEnabled")
    public String isNewFeatureEnabled(@RequestHeader(value = "group", defaultValue = "NoAccessGroup") String group) {
        final Client client = openFeatureAPI.getClient();

        // Create a context with the user group information from token....
        MutableContext context = new MutableContext();
        context.add("group", group);
        boolean feature = client.getBooleanValue("newFeature", false, context);
        return "Feature enabled: " + feature;

    }
}
