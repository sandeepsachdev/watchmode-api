package com.watchmode.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record StatusResponse(
        @JsonProperty("quota") int quota,
        @JsonProperty("quota_used") int quotaUsed
) {}
