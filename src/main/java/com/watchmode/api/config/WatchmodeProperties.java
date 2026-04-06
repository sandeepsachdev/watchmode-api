package com.watchmode.api.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "watchmode.api")
public record WatchmodeProperties(String baseUrl, String key) {}
