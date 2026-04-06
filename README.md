# Watchmode API — Spring Boot Application

A Spring Boot web application that wraps the [Watchmode API](https://api.watchmode.com) and provides both a REST API and a dark-themed web UI for browsing streaming availability data for movies and TV shows.

---

## Recreating this project with Claude Code

The entire project was built interactively using Claude Code. The prompts below, in order, will recreate it from scratch.

---

### Prompt 1 — Initial project scaffold

```
build a java spring boot application using the api from api.watchmode.com.
It's api specification is available here. https://api.watchmode.com/gateway/openapi.
My api key is <YOUR_API_KEY>
```

This produces the full Maven project structure including:
- `pom.xml` (Spring Boot 3.2, WebFlux, SpringDoc/Swagger, Thymeleaf)
- `application.yml` with API key and base URL
- 16 Java record model classes mapped to the actual API response shapes
- `WatchmodeService` using reactive `WebClient` for all Watchmode endpoints
- REST controllers: reference data, search, titles, people, releases, changes
- Global exception handler returning RFC 9457 `ProblemDetail` responses
- Maven wrapper (`mvnw`) so no global Maven install is required

---

### Prompt 2 — Fix Lombok / Java version incompatibility

```
i get this error java: java.lang.ExceptionInInitializerError
com.sun.tools.javac.code.TypeTag :: UNKNOWN
```

Removes the Lombok dependency (unused since all models are Java records) and sets `<java.version>21</java.version>` so the Java 25 JDK emits Java 21 bytecode compatible with Spring Boot 3.2.

---

### Prompt 3 — Fix Spring Boot class format error in tests

```
ERROR] WatchmodeApiApplicationTests — BeanDefinitionStore Incompatible class format
```

Reverts `<java.version>` from 25 back to 21 so Spring Boot's classpath scanner accepts the compiled test classes.

---

### Prompt 4 — Add a web UI

```
add a web user interface for this application
```

Adds Thymeleaf and builds five pages with a dark cinema-style theme (Bootstrap 5 + Bootstrap Icons via CDN):

| Page | URL |
|------|-----|
| Home | `/` |
| Search | `/search` |
| Browse | `/browse` |
| Title detail | `/titles/{id}` |
| Person detail | `/people/{id}` |
| Releases | `/releases` |

Also adds `WebController`, a shared Thymeleaf layout fragment, and `app.css`.

---

### Prompt 5 — Fix broken images on browse page

```
the browse tab is not showing images properly
```

Discovers that the `list-titles` API endpoint returns no poster URLs (only `id`, `title`, `year`, `imdb_id`, `tmdb_id`, `type`). Removes the non-existent `poster` field from `TitleSummary` and replaces broken `<img>` tags with styled dark placeholders showing the title name and year.

---

### Prompt 6 — Display IMDb score

```
can you display the imdb score
```

- On the **title detail page**: adds a yellow IMDb badge showing `user_rating` (which maps 1:1 to the IMDb score, e.g. 9.3/10 for Breaking Bad), confirmed by calling the API directly.
- On **browse cards**: adds a small yellow `IMDb` badge linking to the title's IMDb page (the list endpoint doesn't return ratings so a direct link is the next best thing).

---

### Prompt 7 — Fix title detail page JSON error

```
the titles page does not work and shows this error JSON decoding error:
Cannot construct instance of `com.watchmode.api.model.Genre`...
no int/Int-argument constructor/factory method to deserialize from Number value (4)
```

Fixes `TitleDetails` — the `genres` and `networks` fields are plain integer ID arrays (`List<Integer>`), not objects, despite the model initially mapping them to `List<Genre>` and `List<Network>`.

---

### Prompt 8 — Show real poster images on browse page

```
the browse page still does not show the images of the movies
```

The list endpoint has no poster data. Fetches title details for all 12 cards **in parallel** using `Flux.fromIterable().flatMap()` to retrieve poster URLs, then passes a `Map<Long, String>` (titleId → posterUrl) to the template. Page size reduced from 24 → 12 to keep parallel fetches reasonable.

---

### Prompt 9 — Show real poster images on search page

```
the search screen is not displaying images of the titles
```

Same root cause as browse. Removes the non-existent `image_url` field from `SearchResult`, then applies the same parallel detail-fetch approach to populate a `posterMap` for search result cards.

---

### Prompt 10 — Fix person detail page JSON error

```
this error shows on the people details screen JSON decoding error:
Cannot deserialize value of type `java.lang.Integer` from String "m"
```

`Person` model was completely wrong. Fixes three issues found by calling the API directly:

| Field | Was | Actual API |
|-------|-----|------------|
| `gender` | `Integer` | `String` (`"m"` / `"f"`) |
| `known_for` | `List<TitleSummary>` | `List<Long>` (ID array) |
| `professions` | single `List<String>` | three separate fields: `main_profession`, `secondary_profession`, `tertiary_profession` |

Also fetches `known_for` title details in parallel to show poster images on the person page.

---

### Prompt 11 — Fix SpEL error on title page

```
org.springframework.expression.spel.SpelEvaluationException: EL1004E:
Method call: Method subList(...) cannot be found on type org.thymeleaf.expression.Lists
```

`#lists.subList()` in Thymeleaf doesn't accept SpEL expressions as arguments. Replaces:
```html
th:each="sid : ${#lists.subList(title.similarTitles, 0, T(java.lang.Math).min(8, ...))}"
```
with:
```html
th:each="sid, iter : ${title.similarTitles}" th:if="${iter.index < 8}"
```

---

### Prompt 12 — Fix missing cast headshot images on title page

```
images of people are not appearing on the titles page
```

The `cast-crew` append returns `headshot_url: null` for many cast members. Solution: after loading the title details, identifies cast members with null headshots and fetches their person records in parallel. Merges the results into a `headshotMap` (personId → headshotUrl) passed to the template, ensuring every cast card shows the best available photo.

---

### Prompt 13 — Fix Netty macOS DNS warning

```
Unable to load io.netty.resolver.dns.macos.MacOSDnsServerAddressStreamProvider,
fallback to system defaults. Check whether you have a dependency on
'io.netty:netty-resolver-dns-native-macos'.
```

Adds the native Netty DNS resolver for Apple Silicon to `pom.xml`:
```xml
<dependency>
    <groupId>io.netty</groupId>
    <artifactId>netty-resolver-dns-native-macos</artifactId>
    <classifier>osx-aarch_64</classifier>
</dependency>
```

---

## Running the application

```bash
./mvnw spring-boot:run
```

| URL | Description |
|-----|-------------|
| `http://localhost:8080/` | Web UI home |
| `http://localhost:8080/browse` | Browse titles with filters |
| `http://localhost:8080/search?q=breaking+bad` | Search |
| `http://localhost:8080/releases` | New & upcoming releases |
| `http://localhost:8080/swagger-ui.html` | REST API docs (Swagger UI) |

## Tech stack

- Java 21 bytecode (JDK 25 compatible)
- Spring Boot 3.2.4
- Spring WebFlux (`WebClient`) for non-blocking Watchmode API calls
- Spring MVC + Thymeleaf for the web UI
- Bootstrap 5 + Bootstrap Icons (CDN)
- SpringDoc OpenAPI 2 (Swagger UI)
- Maven Wrapper (no global Maven required)
