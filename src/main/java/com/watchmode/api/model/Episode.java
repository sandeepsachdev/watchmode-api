package com.watchmode.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record Episode(
        @JsonProperty("id") long id,
        @JsonProperty("name") String name,
        @JsonProperty("episode_number") int episodeNumber,
        @JsonProperty("season_number") int seasonNumber,
        @JsonProperty("air_date") String airDate,
        @JsonProperty("runtime") Integer runtime,
        @JsonProperty("overview") String overview,
        @JsonProperty("sources") List<TitleSource> sources
) {}
