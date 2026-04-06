package com.watchmode.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Genre(
        @JsonProperty("id") int id,
        @JsonProperty("name") String name,
        @JsonProperty("tmdb_id") Integer tmdbId
) {}
