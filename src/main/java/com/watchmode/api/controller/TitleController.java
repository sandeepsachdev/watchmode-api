package com.watchmode.api.controller;

import com.watchmode.api.model.*;
import com.watchmode.api.service.WatchmodeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/titles")
@Tag(name = "Titles", description = "Browse and get details about movies and TV shows")
public class TitleController {

    private final WatchmodeService service;

    public TitleController(WatchmodeService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Paginated list of titles matching optional filters")
    public Mono<ListTitlesResponse> listTitles(
            @RequestParam(required = false) String types,
            @RequestParam(required = false) String regions,
            @RequestParam(required = false) String sourceTypes,
            @RequestParam(required = false) String sourceIds,
            @RequestParam(required = false) String genres,
            @RequestParam(required = false) String networkIds,
            @RequestParam(required = false) String languages,
            @RequestParam(required = false) String releaseDateStart,
            @RequestParam(required = false) String releaseDateEnd,
            @RequestParam(required = false) Double userRatingLow,
            @RequestParam(required = false) Double userRatingHigh,
            @RequestParam(required = false) Integer criticScoreLow,
            @RequestParam(required = false) Integer criticScoreHigh,
            @RequestParam(required = false) Long personId,
            @RequestParam(required = false) String sortBy,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int limit) {
        return service.listTitles(types, regions, sourceTypes, sourceIds, genres, networkIds,
                languages, releaseDateStart, releaseDateEnd, userRatingLow, userRatingHigh,
                criticScoreLow, criticScoreHigh, personId, sortBy, page, limit);
    }

    @GetMapping("/{titleId}")
    @Operation(summary = "Get full details for a title (Watchmode, IMDB, or TMDB ID accepted)")
    public Mono<TitleDetails> getTitleDetails(
            @PathVariable String titleId,
            @Parameter(description = "Comma-separated: sources, seasons, episodes, cast-crew")
            @RequestParam(required = false) String appendToResponse,
            @RequestParam(required = false) String language,
            @RequestParam(required = false) String regions) {
        return service.getTitleDetails(titleId, appendToResponse, language, regions);
    }

    @GetMapping("/{titleId}/sources")
    @Operation(summary = "Get all streaming sources where the title is available")
    public Mono<List<TitleSource>> getTitleSources(
            @PathVariable String titleId,
            @RequestParam(required = false) String regions) {
        return service.getTitleSources(titleId, regions);
    }

    @GetMapping("/{titleId}/seasons")
    @Operation(summary = "Get all seasons for a TV series")
    public Mono<List<Season>> getTitleSeasons(@PathVariable String titleId) {
        return service.getTitleSeasons(titleId);
    }

    @GetMapping("/{titleId}/episodes")
    @Operation(summary = "Get all episodes including per-episode streaming sources")
    public Mono<List<Episode>> getTitleEpisodes(@PathVariable String titleId) {
        return service.getTitleEpisodes(titleId);
    }

    @GetMapping("/{titleId}/cast-crew")
    @Operation(summary = "Get cast and crew for a title")
    public Mono<List<CastCrew>> getTitleCastCrew(
            @PathVariable String titleId,
            @RequestParam(required = false) String language) {
        return service.getTitleCastCrew(titleId, language);
    }
}
