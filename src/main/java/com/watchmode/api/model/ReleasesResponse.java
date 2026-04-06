package com.watchmode.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record ReleasesResponse(
        @JsonProperty("releases") List<Release> releases
) {}
