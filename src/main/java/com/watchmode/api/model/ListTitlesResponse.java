package com.watchmode.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record ListTitlesResponse(
        @JsonProperty("titles") List<TitleSummary> titles,
        @JsonProperty("total_results") int totalResults,
        @JsonProperty("total_pages") int totalPages,
        @JsonProperty("page") int page
) {}
