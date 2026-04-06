package com.watchmode.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Network(
        @JsonProperty("id") int id,
        @JsonProperty("name") String name,
        @JsonProperty("origin_country") String originCountry,
        @JsonProperty("tmdb_id") Integer tmdbId
) {}
