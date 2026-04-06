package com.watchmode.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record ChangesResponse(
        @JsonProperty("titles") List<Long> titles,
        @JsonProperty("total_results") int totalResults,
        @JsonProperty("total_pages") int totalPages,
        @JsonProperty("page") int page
) {}
