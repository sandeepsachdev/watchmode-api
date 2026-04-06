package com.watchmode.api.controller;

import com.watchmode.api.model.AutocompleteResponse;
import com.watchmode.api.model.SearchResponse;
import com.watchmode.api.service.WatchmodeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/search")
@Tag(name = "Search", description = "Search for titles and people")
public class SearchController {

    private final WatchmodeService service;

    public SearchController(WatchmodeService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Search for titles or people by name or external ID")
    public Mono<SearchResponse> search(
            @Parameter(description = "Field to search: name, imdb_id, tmdb_movie_id, tmdb_tv_id, tmdb_person_id")
            @RequestParam String searchField,
            @Parameter(description = "Search term or external ID")
            @RequestParam String searchValue,
            @RequestParam(required = false) String types) {
        return service.search(searchField, searchValue, types);
    }

    @GetMapping("/autocomplete")
    @Operation(summary = "Autocomplete search optimised for typeahead UIs")
    public Mono<AutocompleteResponse> autocomplete(
            @RequestParam String searchValue,
            @Parameter(description = "1=titles+people, 2=titles, 3=movies, 4=TV, 5=people")
            @RequestParam(required = false) Integer searchType) {
        return service.autocompleteSearch(searchValue, searchType);
    }
}
