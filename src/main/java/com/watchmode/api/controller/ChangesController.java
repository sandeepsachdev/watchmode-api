package com.watchmode.api.controller;

import com.watchmode.api.model.ChangesResponse;
import com.watchmode.api.service.WatchmodeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/changes")
@Tag(name = "Changes", description = "Track newly added titles and data changes (paid plans)")
public class ChangesController {

    private final WatchmodeService service;

    public ChangesController(WatchmodeService service) {
        this.service = service;
    }

    @GetMapping("/new-titles")
    @Operation(summary = "Get IDs of new titles added to Watchmode within a date range")
    public Mono<ChangesResponse> getNewTitles(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) String types,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "250") int limit) {
        return service.getNewTitles(startDate, endDate, types, page, limit);
    }

    @GetMapping("/sources-changed")
    @Operation(summary = "Get IDs of titles whose streaming availability changed")
    public Mono<ChangesResponse> getTitlesSourcesChanged(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) String types,
            @RequestParam(required = false) String regions,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "250") int limit) {
        return service.getTitlesSourcesChanged(startDate, endDate, types, regions, page, limit);
    }

    @GetMapping("/details-changed")
    @Operation(summary = "Get IDs of titles whose metadata changed")
    public Mono<ChangesResponse> getTitlesDetailsChanged(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) String types,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "250") int limit) {
        return service.getTitlesDetailsChanged(startDate, endDate, types, page, limit);
    }
}
