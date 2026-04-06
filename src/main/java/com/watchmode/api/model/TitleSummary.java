package com.watchmode.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TitleSummary(
        @JsonProperty("id") long id,
        @JsonProperty("title") String title,
        @JsonProperty("year") Integer year,
        @JsonProperty("imdb_id") String imdbId,
        @JsonProperty("tmdb_id") Integer tmdbId,
        @JsonProperty("tmdb_type") String tmdbType,
        @JsonProperty("type") String type
) {}
