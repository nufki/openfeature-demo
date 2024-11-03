package com.openfeature_demo.controllers;

import com.openfeature_demo.openfeature.RaiContext;
import dev.openfeature.sdk.MutableContext;
import dev.openfeature.sdk.OpenFeatureAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api")
public class OpenFeatureProtectedController {
    private final OpenFeatureAPI openFeatureAPI;

    @Autowired
    public OpenFeatureProtectedController(OpenFeatureAPI OFApi) {
        this.openFeatureAPI = OFApi;
    }

    @GetMapping("/isNewFeatureEnabled")
    public String isNewFeatureEnabled() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        MutableContext context = new RaiContext(principal);
        boolean featureEnabled = openFeatureAPI.getClient().getBooleanValue("newFeature", false, context);
        return "Feature enabled: " + featureEnabled;
    }
}
