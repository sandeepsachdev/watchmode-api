package com.watchmode.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record TitleDetails(
        @JsonProperty("id") long id,
        @JsonProperty("title") String title,
        @JsonProperty("plot_overview") String plotOverview,
        @JsonProperty("type") String type,
        @JsonProperty("runtime_minutes") Integer runtimeMinutes,
        @JsonProperty("year") Integer year,
        @JsonProperty("end_year") Integer endYear,
        @JsonProperty("release_date") String releaseDate,
        @JsonProperty("imdb_id") String imdbId,
        @JsonProperty("tmdb_id") Integer tmdbId,
        @JsonProperty("tmdb_type") String tmdbType,
        @JsonProperty("genres") List<Integer> genres,
        @JsonProperty("genre_names") List<String> genreNames,
        @JsonProperty("user_rating") Double userRating,
        @JsonProperty("critic_score") Integer criticScore,
        @JsonProperty("us_rating") String usRating,
        @JsonProperty("poster") String poster,
        @JsonProperty("backdrop") String backdrop,
        @JsonProperty("original_language") String originalLanguage,
        @JsonProperty("similar_titles") List<Long> similarTitles,
        @JsonProperty("networks") List<Integer> networks,
        @JsonProperty("network_names") List<String> networkNames,
        @JsonProperty("relevance_percentile") Double relevancePercentile,
        @JsonProperty("sources") List<TitleSource> sources,
        @JsonProperty("seasons") List<Season> seasons,
        @JsonProperty("cast") List<CastCrew> cast
) {}
