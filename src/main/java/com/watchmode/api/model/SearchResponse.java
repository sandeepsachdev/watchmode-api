package com.watchmode.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record SearchResponse(
        @JsonProperty("title_results") List<SearchResult> titleResults,
        @JsonProperty("people_results") List<SearchResult> peopleResults
) {}
