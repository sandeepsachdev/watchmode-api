package com.watchmode.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Release(
        @JsonProperty("id") long id,
        @JsonProperty("title") String title,
        @JsonProperty("type") String type,
        @JsonProperty("tmdb_id") Integer tmdbId,
        @JsonProperty("imdb_id") String imdbId,
        @JsonProperty("season_number") Integer seasonNumber,
        @JsonProperty("poster_url") String posterUrl,
        @JsonProperty("source_release_date") String sourceReleaseDate,
        @JsonProperty("source_id") int sourceId,
        @JsonProperty("source_name") String sourceName,
        @JsonProperty("is_original") Boolean isOriginal
) {}
