package com.watchmode.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AutocompleteResult(
        @JsonProperty("id") long id,
        @JsonProperty("name") String name,
        @JsonProperty("type") String type,
        @JsonProperty("year") Integer year,
        @JsonProperty("imdb_id") String imdbId,
        @JsonProperty("tmdb_id") Integer tmdbId,
        @JsonProperty("tmdb_type") String tmdbType,
        @JsonProperty("image_url") String imageUrl,
        @JsonProperty("relevance") Double relevance
) {}
