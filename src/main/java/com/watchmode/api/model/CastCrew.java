package com.watchmode.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CastCrew(
        @JsonProperty("person_id") long personId,
        @JsonProperty("type") String type,
        @JsonProperty("full_name") String fullName,
        @JsonProperty("headshot_url") String headshotUrl,
        @JsonProperty("role") String role,
        @JsonProperty("episode_count") Integer episodeCount,
        @JsonProperty("order") Integer order
) {}
