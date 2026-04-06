package com.watchmode.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record AutocompleteResponse(
        @JsonProperty("results") List<AutocompleteResult> results
) {}
