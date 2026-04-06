package com.watchmode.api.controller;

import com.watchmode.api.model.ReleasesResponse;
import com.watchmode.api.service.WatchmodeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/releases")
@Tag(name = "Releases", description = "Recently released or upcoming streaming titles")
public class ReleaseController {

    private final WatchmodeService service;

    public ReleaseController(WatchmodeService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Get recent or upcoming releases on major streaming services")
    public Mono<ReleasesResponse> getReleases(
            @Parameter(description = "YYYYMMDD or YYYYMMDDHHMMSS (default: 30 days ago)")
            @RequestParam(required = false) String startDate,
            @Parameter(description = "YYYYMMDD or YYYYMMDDHHMMSS (default: 30 days ahead)")
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) Integer limit) {
        return service.getReleases(startDate, endDate, limit);
    }
}
