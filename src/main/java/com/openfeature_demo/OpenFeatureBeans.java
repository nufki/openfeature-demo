package com.openfeature_demo;

import dev.openfeature.contrib.providers.flagd.FlagdOptions;
import dev.openfeature.contrib.providers.flagd.FlagdProvider;
import dev.openfeature.contrib.providers.flipt.FliptProvider;
import dev.openfeature.contrib.providers.flipt.FliptProviderConfig;
import dev.openfeature.sdk.FeatureProvider;
import dev.openfeature.sdk.OpenFeatureAPI;
import dev.openfeature.sdk.exceptions.OpenFeatureError;
import io.flipt.api.FliptClient;
import io.flipt.api.FliptClient.FliptClientBuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class OpenFeatureBeans {

    @Bean 
    @ConditionalOnProperty(name = "demo.featureProvider", havingValue = "flipt")
    public FeatureProvider featureProviderFlipt(){
        FliptClientBuilder fliptClientBuilder = FliptClient.builder().url("http://localhost:8081");
        FliptProviderConfig fliptProviderConfig = FliptProviderConfig.builder().fliptClientBuilder(fliptClientBuilder).build();

        return new FliptProvider(fliptProviderConfig);
    }

    @Bean 
    @ConditionalOnProperty(name = "demo.featureProvider", havingValue = "flagd")
    public FeatureProvider featureProviderFlagd(){
        return new FlagdProvider();
    }

    @Bean
    public OpenFeatureAPI OpenFeatureAPI(FeatureProvider featureProvider) {

        final OpenFeatureAPI openFeatureAPI = OpenFeatureAPI.getInstance();

        // Use flagd as the OpenFeature provider and use default configurations
        try {
            openFeatureAPI.setProviderAndWait(featureProvider);
        } catch (OpenFeatureError e) {
            throw new RuntimeException("Failed to set OpenFeature provider", e);
        }

        return openFeatureAPI;
    }
}
