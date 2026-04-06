package com.watchmode.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TitleSource(
        @JsonProperty("source_id") int sourceId,
        @JsonProperty("name") String name,
        @JsonProperty("type") String type,
        @JsonProperty("region") String region,
        @JsonProperty("web_url") String webUrl,
        @JsonProperty("ios_url") String iosUrl,
        @JsonProperty("android_url") String androidUrl,
        @JsonProperty("format") String format,
        @JsonProperty("price") Double price,
        @JsonProperty("seasons") Integer seasons,
        @JsonProperty("episodes") Integer episodes,
        @JsonProperty("leaving") Integer leaving,
        @JsonProperty("added") Integer added
) {}
