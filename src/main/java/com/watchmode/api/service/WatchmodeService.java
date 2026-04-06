package com.watchmode.api.service;

import com.watchmode.api.config.WatchmodeProperties;
import com.watchmode.api.model.*;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.function.Function;


@Service
public class WatchmodeService {

    private final WebClient webClient;
    private final String apiKey;

    public WatchmodeService(WebClient watchmodeWebClient, WatchmodeProperties props) {
        this.webClient = watchmodeWebClient;
        this.apiKey = props.key();
    }

    // -------------------------------------------------------------------------
    // Reference / configuration endpoints
    // -------------------------------------------------------------------------

    public Mono<StatusResponse> getStatus() {
        return get("/status/", StatusResponse.class, b -> b);
    }

    @Cacheable(value = "sources", key = "#regions + ':' + #types")
    public Mono<List<Source>> getSources(String regions, String types) {
        Mono<List<Source>> result = getList("/sources/", new ParameterizedTypeReference<List<Source>>() {}, b -> {
            if (regions != null) b = b.queryParam("regions", regions);
            if (types != null)   b = b.queryParam("types", types);
            return b;
        });
        return result.cache();
    }

    @Cacheable("regions")
    public Mono<List<Region>> getRegions() {
        Mono<List<Region>> result = getList("/regions/", new ParameterizedTypeReference<List<Region>>() {}, b -> b);
        return result.cache();
    }

    @Cacheable("networks")
    public Mono<List<Network>> getNetworks() {
        Mono<List<Network>> result = getList("/networks/", new ParameterizedTypeReference<List<Network>>() {}, b -> b);
        return result.cache();
    }

    @Cacheable("genres")
    public Mono<List<Genre>> getGenres() {
        Mono<List<Genre>> result = getList("/genres/", new ParameterizedTypeReference<List<Genre>>() {}, b -> b);
        return result.cache();
    }

    // -------------------------------------------------------------------------
    // Search endpoints
    // -------------------------------------------------------------------------

    public Mono<SearchResponse> search(String searchField, String searchValue, String types) {
        return get("/search/", SearchResponse.class, b -> {
            b = b.queryParam("search_field", searchField)
                 .queryParam("search_value", searchValue);
            if (types != null) b = b.queryParam("types", types);
            return b;
        });
    }

    public Mono<AutocompleteResponse> autocompleteSearch(String searchValue, Integer searchType) {
        return get("/autocomplete-search/", AutocompleteResponse.class, b -> {
            b = b.queryParam("search_value", searchValue);
            if (searchType != null) b = b.queryParam("search_type", searchType);
            return b;
        });
    }

    // -------------------------------------------------------------------------
    // Title list endpoint
    // -------------------------------------------------------------------------

    public Mono<ListTitlesResponse> listTitles(
            String types, String regions, String sourceTypes, String sourceIds,
            String genres, String networkIds, String languages,
            String releaseDateStart, String releaseDateEnd,
            Double userRatingLow, Double userRatingHigh,
            Integer criticScoreLow, Integer criticScoreHigh,
            Long personId, String sortBy, int page, int limit) {

        return get("/list-titles/", ListTitlesResponse.class, b -> {
            if (types != null)           b = b.queryParam("types", types);
            if (regions != null)         b = b.queryParam("regions", regions);
            if (sourceTypes != null)     b = b.queryParam("source_types", sourceTypes);
            if (sourceIds != null)       b = b.queryParam("source_ids", sourceIds);
            if (genres != null)          b = b.queryParam("genres", genres);
            if (networkIds != null)      b = b.queryParam("network_ids", networkIds);
            if (languages != null)       b = b.queryParam("languages", languages);
            if (releaseDateStart != null) b = b.queryParam("release_date_start", releaseDateStart);
            if (releaseDateEnd != null)   b = b.queryParam("release_date_end", releaseDateEnd);
            if (userRatingLow != null)   b = b.queryParam("user_rating_low", userRatingLow);
            if (userRatingHigh != null)  b = b.queryParam("user_rating_high", userRatingHigh);
            if (criticScoreLow != null)  b = b.queryParam("critic_score_low", criticScoreLow);
            if (criticScoreHigh != null) b = b.queryParam("critic_score_high", criticScoreHigh);
            if (personId != null)        b = b.queryParam("person_id", personId);
            if (sortBy != null)          b = b.queryParam("sort_by", sortBy);
            return b.queryParam("page", page).queryParam("limit", limit);
        });
    }

    // -------------------------------------------------------------------------
    // Title detail endpoints
    // -------------------------------------------------------------------------

    @Cacheable(value = "titleDetails", key = "#titleId + ':' + #appendToResponse + ':' + #language + ':' + #regions")
    public Mono<TitleDetails> getTitleDetails(String titleId, String appendToResponse,
                                              String language, String regions) {
        return get("/title/" + titleId + "/details/", TitleDetails.class, b -> {
            if (appendToResponse != null) b = b.queryParam("append_to_response", appendToResponse);
            if (language != null)         b = b.queryParam("language", language);
            if (regions != null)          b = b.queryParam("regions", regions);
            return b;
        }).cache();
    }

    public Mono<List<TitleSource>> getTitleSources(String titleId, String regions) {
        return getList("/title/" + titleId + "/sources/", new ParameterizedTypeReference<>() {}, b -> {
            if (regions != null) b = b.queryParam("regions", regions);
            return b;
        });
    }

    public Mono<List<Season>> getTitleSeasons(String titleId) {
        return getList("/title/" + titleId + "/seasons/", new ParameterizedTypeReference<>() {}, b -> b);
    }

    public Mono<List<Episode>> getTitleEpisodes(String titleId) {
        return getList("/title/" + titleId + "/episodes/", new ParameterizedTypeReference<>() {}, b -> b);
    }

    public Mono<List<CastCrew>> getTitleCastCrew(String titleId, String language) {
        return getList("/title/" + titleId + "/cast-crew/", new ParameterizedTypeReference<>() {}, b -> {
            if (language != null) b = b.queryParam("language", language);
            return b;
        });
    }

    // -------------------------------------------------------------------------
    // Person endpoint
    // -------------------------------------------------------------------------

    @Cacheable(value = "personDetails", key = "#personId")
    public Mono<Person> getPerson(long personId) {
        return get("/person/" + personId + "/", Person.class, b -> b).cache();
    }

    // -------------------------------------------------------------------------
    // Release endpoints
    // -------------------------------------------------------------------------

    public Mono<ReleasesResponse> getReleases(String startDate, String endDate, Integer limit) {
        return get("/releases/", ReleasesResponse.class, b -> {
            if (startDate != null) b = b.queryParam("start_date", startDate);
            if (endDate != null)   b = b.queryParam("end_date", endDate);
            if (limit != null)     b = b.queryParam("limit", limit);
            return b;
        });
    }

    // -------------------------------------------------------------------------
    // Changes endpoints
    // -------------------------------------------------------------------------

    public Mono<ChangesResponse> getNewTitles(String startDate, String endDate,
                                              String types, int page, int limit) {
        return get("/changes/new_titles/", ChangesResponse.class, b -> {
            if (startDate != null) b = b.queryParam("start_date", startDate);
            if (endDate != null)   b = b.queryParam("end_date", endDate);
            if (types != null)     b = b.queryParam("types", types);
            return b.queryParam("page", page).queryParam("limit", limit);
        });
    }

    public Mono<ChangesResponse> getTitlesSourcesChanged(String startDate, String endDate,
                                                          String types, String regions,
                                                          int page, int limit) {
        return get("/changes/titles_sources_changed/", ChangesResponse.class, b -> {
            if (startDate != null) b = b.queryParam("start_date", startDate);
            if (endDate != null)   b = b.queryParam("end_date", endDate);
            if (types != null)     b = b.queryParam("types", types);
            if (regions != null)   b = b.queryParam("regions", regions);
            return b.queryParam("page", page).queryParam("limit", limit);
        });
    }

    public Mono<ChangesResponse> getTitlesDetailsChanged(String startDate, String endDate,
                                                          String types, int page, int limit) {
        return get("/changes/titles_details_changed/", ChangesResponse.class, b -> {
            if (startDate != null) b = b.queryParam("start_date", startDate);
            if (endDate != null)   b = b.queryParam("end_date", endDate);
            if (types != null)     b = b.queryParam("types", types);
            return b.queryParam("page", page).queryParam("limit", limit);
        });
    }

    // -------------------------------------------------------------------------
    // Private helpers
    // -------------------------------------------------------------------------

    private <T> Mono<T> get(String path, Class<T> responseType,
                             Function<UriBuilder, UriBuilder> queryCustomizer) {
        return webClient.get()
                .uri(b -> queryCustomizer.apply(b.path(path))
                        .queryParam("apiKey", apiKey)
                        .build())
                .retrieve()
                .bodyToMono(responseType);
    }

    private <T> Mono<List<T>> getList(String path, ParameterizedTypeReference<List<T>> ref,
                                       Function<UriBuilder, UriBuilder> queryCustomizer) {
        return webClient.get()
                .uri(b -> queryCustomizer.apply(b.path(path))
                        .queryParam("apiKey", apiKey)
                        .build())
                .retrieve()
                .bodyToMono(ref);
    }
}
