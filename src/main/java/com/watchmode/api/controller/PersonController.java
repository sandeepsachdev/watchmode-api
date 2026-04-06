package com.watchmode.api.controller;

import com.watchmode.api.model.Person;
import com.watchmode.api.service.WatchmodeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/people")
@Tag(name = "People", description = "Get biographical data about actors, directors, and crew")
public class PersonController {

    private final WatchmodeService service;

    public PersonController(WatchmodeService service) {
        this.service = service;
    }

    @GetMapping("/{personId}")
    @Operation(summary = "Get details for a person (Watchmode person ID, starts with 7)")
    public Mono<Person> getPerson(@PathVariable long personId) {
        return service.getPerson(personId);
    }
}
