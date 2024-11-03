package com.openfeature_demo.controllers;

import com.openfeature_demo.openfeature.RaiContext;
import dev.openfeature.sdk.Client;
import dev.openfeature.sdk.MutableContext;
import dev.openfeature.sdk.OpenFeatureAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/unprotected-api")
public class OpenFeatureController {
    private final OpenFeatureAPI openFeatureAPI;

    @Autowired
    public OpenFeatureController(OpenFeatureAPI OFApi) {
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

    @GetMapping("/isSpecialFeatureEnabled")
    public String isSpecialFeatureEnabled(@RequestHeader(value = "Authentication") String contract) {
        final Client client = openFeatureAPI.getClient();

        // Create a context with the user token....
        MutableContext context = new RaiContext(contract);
        boolean feature = client.getBooleanValue("special-feature", false, context);
        return "Feature enabled: " + feature;
    }
}
