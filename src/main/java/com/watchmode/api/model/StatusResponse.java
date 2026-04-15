package com.watchmode.api.model;

public record StatusResponse(
        int quota,
        int quotaUsed
) {}
