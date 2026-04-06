package com.watchmode.api.controller;

import com.watchmode.api.model.*;
import com.watchmode.api.service.WatchmodeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Controller
public class WebController {

    private final WatchmodeService service;

    public WebController(WatchmodeService service) {
        this.service = service;
    }

    @GetMapping("/")
    public String home(Model model) {
        try {
            StatusResponse status = service.getStatus().block();
            model.addAttribute("status", status);
        } catch (Exception ignored) {}
        return "index";
    }

    @GetMapping("/search")
    public String search(@RequestParam(required = false) String q,
                         @RequestParam(defaultValue = "name") String field,
                         @RequestParam(required = false) String types,
                         Model model) {
        model.addAttribute("q", q);
        model.addAttribute("field", field);
        model.addAttribute("types", types);

        if (q != null && !q.isBlank()) {
            try {
                SearchResponse results = service.search(field, q, types).block();
                model.addAttribute("results", results);

                if (results != null && results.titleResults() != null && !results.titleResults().isEmpty()) {
                    Map<Long, String> posterMap = Flux.fromIterable(results.titleResults())
                            .flatMap(t -> service.getTitleDetails(String.valueOf(t.id()), null, null, null)
                                    .map(d -> Map.entry(t.id(), d.poster() != null ? d.poster() : ""))
                                    .onErrorReturn(Map.entry(t.id(), "")))
                            .collectMap(Map.Entry::getKey, Map.Entry::getValue)
                            .block();
                    model.addAttribute("posterMap", posterMap);
                }
            } catch (Exception e) {
                model.addAttribute("error", e.getMessage());
            }
        }
        return "search";
    }

    @GetMapping("/browse")
    public String browse(
            @RequestParam(required = false) String types,
            @RequestParam(required = false) String genres,
            @RequestParam(required = false) String sourceTypes,
            @RequestParam(required = false) String regions,
            @RequestParam(required = false) String sortBy,
            @RequestParam(defaultValue = "1") int page,
            Model model) {

        model.addAttribute("types", types);
        model.addAttribute("genres", genres);
        model.addAttribute("sourceTypes", sourceTypes);
        model.addAttribute("regions", regions);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("page", page);

        try {
            List<Genre> genreList = service.getGenres().block();
            model.addAttribute("genreList", genreList);

            ListTitlesResponse titles = service.listTitles(
                    types, regions, sourceTypes, null, genres, null,
                    null, null, null, null, null, null, null,
                    null, sortBy, page, 12).block();
            model.addAttribute("titles", titles);

            if (titles != null && titles.titles() != null) {
                Map<Long, String> posterMap = Flux.fromIterable(titles.titles())
                        .flatMap(t -> service.getTitleDetails(String.valueOf(t.id()), null, null, null)
                                .map(d -> Map.entry(t.id(), d.poster() != null ? d.poster() : ""))
                                .onErrorReturn(Map.entry(t.id(), "")))
                        .collectMap(Map.Entry::getKey, Map.Entry::getValue)
                        .block();
                model.addAttribute("posterMap", posterMap);
            }
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        return "browse";
    }

    @GetMapping("/titles/{titleId}")
    public String titleDetail(@PathVariable String titleId, Model model) {
        try {
            TitleDetails details = service
                    .getTitleDetails(titleId, "sources,cast-crew,seasons", null, null)
                    .block();
            model.addAttribute("title", details);

            if (details != null && details.cast() != null) {
                // For cast members with a null headshot, fetch the person to get it
                List<CastCrew> castOnly = details.cast().stream()
                        .filter(c -> "Cast".equals(c.type()))
                        .toList();

                Map<Long, String> headshotMap = Flux.fromIterable(castOnly)
                        .filter(c -> c.headshotUrl() == null)
                        .flatMap(c -> service.getPerson(c.personId())
                                .map(p -> Map.entry(c.personId(), p.headshotUrl() != null ? p.headshotUrl() : ""))
                                .onErrorReturn(Map.entry(c.personId(), "")))
                        .collectMap(Map.Entry::getKey, Map.Entry::getValue)
                        .block();

                // Also add the headshots we already have from the cast list
                castOnly.stream()
                        .filter(c -> c.headshotUrl() != null)
                        .forEach(c -> headshotMap.put(c.personId(), c.headshotUrl()));

                model.addAttribute("headshotMap", headshotMap);
            }
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        return "title-detail";
    }

    @GetMapping("/people/{personId}")
    public String personDetail(@PathVariable long personId, Model model) {
        try {
            Person person = service.getPerson(personId).block();
            model.addAttribute("person", person);

            if (person != null && person.knownFor() != null && !person.knownFor().isEmpty()) {
                List<TitleDetails> knownForTitles = Flux.fromIterable(person.knownFor())
                        .flatMap(id -> service.getTitleDetails(String.valueOf(id), null, null, null)
                                .onErrorResume(e -> reactor.core.publisher.Mono.empty()))
                        .collectList()
                        .block();
                model.addAttribute("knownForTitles", knownForTitles);
            }
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        return "person-detail";
    }

    @GetMapping("/releases")
    public String releases(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            Model model) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyyMMdd");
        if (startDate == null || startDate.isBlank()) {
            startDate = LocalDate.now().minusDays(30).format(fmt);
        }
        if (endDate == null || endDate.isBlank()) {
            endDate = LocalDate.now().minusDays(1).format(fmt);
        }
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        try {
            ReleasesResponse releases = service.getReleases(startDate, endDate, 100).block();
            if (releases != null && releases.releases() != null) {
                List<Release> sorted = releases.releases().stream()
                        .sorted(Comparator.comparing(
                                Release::sourceReleaseDate,
                                Comparator.nullsLast(Comparator.reverseOrder())))
                        .toList();
                releases = new ReleasesResponse(sorted);
            }
            model.addAttribute("releases", releases);
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        return "releases";
    }
}
