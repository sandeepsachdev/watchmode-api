package com.watchmode.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record StatusResponse(
        @JsonProperty("account_quota") int quota,
        @JsonProperty("quota_used") int quotaUsed
) {}
