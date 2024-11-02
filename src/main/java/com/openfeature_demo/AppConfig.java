package com.openfeature_demo;

import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    private boolean enabled;
    private String variant;
    private double allocation;
    

    // Getters and setters
    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }

    public String getVariant() { return variant; }
    public void setVariant(String variant) { this.variant = variant; }

    public double getAllocation() { return allocation; }
    public void setAllocation(double allocation) { this.allocation = allocation; }
}
