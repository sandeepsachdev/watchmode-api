package com.watchmode.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record Person(
        @JsonProperty("id") long id,
        @JsonProperty("full_name") String fullName,
        @JsonProperty("first_name") String firstName,
        @JsonProperty("last_name") String lastName,
        @JsonProperty("tmdb_id") Integer tmdbId,
        @JsonProperty("imdb_id") String imdbId,
        @JsonProperty("main_profession") String mainProfession,
        @JsonProperty("secondary_profession") String secondaryProfession,
        @JsonProperty("tertiary_profession") String tertiaryProfession,
        @JsonProperty("date_of_birth") String dateOfBirth,
        @JsonProperty("date_of_death") String dateOfDeath,
        @JsonProperty("place_of_birth") String placeOfBirth,
        @JsonProperty("gender") String gender,
        @JsonProperty("headshot_url") String headshotUrl,
        @JsonProperty("known_for") List<Long> knownFor,
        @JsonProperty("relevance_percentile") Double relevancePercentile
) {}
