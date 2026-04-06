package com.watchmode.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Region(
        @JsonProperty("country") String country,
        @JsonProperty("name") String name,
        @JsonProperty("flag_url") String flagUrl,
        @JsonProperty("data_tier") int dataTier,
        @JsonProperty("plan_enabled") boolean planEnabled
) {}
