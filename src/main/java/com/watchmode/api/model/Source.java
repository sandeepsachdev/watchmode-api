package com.watchmode.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record Source(
        @JsonProperty("id") int id,
        @JsonProperty("name") String name,
        @JsonProperty("type") String type,
        @JsonProperty("logo_100px") String logo100px,
        @JsonProperty("ios_appstore_url") String iosAppstoreUrl,
        @JsonProperty("android_playstore_url") String androidPlaystoreUrl,
        @JsonProperty("android_scheme") String androidScheme,
        @JsonProperty("ios_scheme") String iosScheme,
        @JsonProperty("regions") List<String> regions
) {}
