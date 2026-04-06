package com.watchmode.api.controller;

import com.watchmode.api.model.*;
import com.watchmode.api.service.WatchmodeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api")
@Tag(name = "Reference Data", description = "Sources, regions, networks, genres, and API status")
public class ReferenceDataController {

    private final WatchmodeService service;

    public ReferenceDataController(WatchmodeService service) {
        this.service = service;
    }

    @GetMapping("/status")
    @Operation(summary = "Get API quota and usage for the current month")
    public Mono<StatusResponse> getStatus() {
        return service.getStatus();
    }

    @GetMapping("/sources")
    @Operation(summary = "List all streaming sources / services")
    public Mono<List<Source>> getSources(
            @RequestParam(required = false) String regions,
            @RequestParam(required = false) String types) {
        return service.getSources(regions, types);
    }

    @GetMapping("/regions")
    @Operation(summary = "List all supported countries / regions")
    public Mono<List<Region>> getRegions() {
        return service.getRegions();
    }

    @GetMapping("/networks")
    @Operation(summary = "List all TV networks")
    public Mono<List<Network>> getNetworks() {
        return service.getNetworks();
    }

    @GetMapping("/genres")
    @Operation(summary = "List all genres with their IDs")
    public Mono<List<Genre>> getGenres() {
        return service.getGenres();
    }
}
