package com.watchmode.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Season(
        @JsonProperty("id") long id,
        @JsonProperty("poster_url") String posterUrl,
        @JsonProperty("name") String name,
        @JsonProperty("number") int number,
        @JsonProperty("air_date") String airDate,
        @JsonProperty("episode_count") int episodeCount
) {}
