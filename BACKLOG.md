# Backlog

Scoped tasks for iterative improvement. Each task is labeled by difficulty and category.

---

## UI Polish

### 1. Add loading spinner on quote form submission — EASY
Show a spinner/overlay when the user clicks "Compare Quotes" to indicate processing.

### 2. Add responsive mobile styles — EASY
Improve card layout and form spacing for mobile viewports (<768px).

### 3. Add provider logo placeholders — EASY
Display colored initials or icons for each provider to make results visually distinct.

### 4. Add "compare selected" feature — MEDIUM
Let users check 2–3 providers and view a side-by-side comparison table.

### 5. Add toast notifications — EASY
Show success/error toasts on admin actions (feature flag toggle) instead of page-level alerts.

---

## Backend Improvements

### 6. Convert DTOs to Java records — EASY
Replace `QuoteRequestDto`, `QuoteResultDto`, and `QuoteResult` with Java `record` types.

### 7. ~~Add global exception handler~~ — DONE
~~Create `@ControllerAdvice` with handlers for validation errors, 404s, and 500s.~~
Implemented as `GlobalExceptionHandler` with `@RestControllerAdvice`. Handles `MethodArgumentNotValidException`, `ConstraintViolationException`, and generic `Exception`.

### 8. Add request/response logging interceptor — MEDIUM
Log incoming requests and outgoing responses with timing, using a `HandlerInterceptor`.

### 9. Add quote history storage — MEDIUM
Persist quote requests and results in H2 with a `QuoteHistory` entity. Add GET `/api/quotes/{id}` endpoint.

### 10. Add provider CRUD API — MEDIUM
Create POST/PUT/DELETE endpoints for providers behind an admin flag.

### 11. Externalize pricing factors to config — EASY
Move age/mileage/claims factor tables from `DefaultPricingEngine` to `application.yml`.

### 12. Add postcode-based risk factor — MEDIUM
Map postcode prefixes to synthetic risk zones and apply a location multiplier in pricing.

---

## Performance

### 13. ~~Add caching for provider data~~ — DONE
~~Use `@Cacheable` on `ProviderService.getAllProviders()` with Spring Cache abstraction.~~
Implemented with `@Cacheable("providers")` on `getAllProviders()` and `@Cacheable("provider")` on `getProviderById()`. `CacheConfig` enables caching via `@EnableCaching`.

### 14. Add async quote calculation — MEDIUM
Use `@Async` or `CompletableFuture` to calculate quotes per provider in parallel.

### 15. Add database indices — EASY
Add index on `providers.name` and `coverage_options.provider_id` for faster lookups.

---

## Security Hardening

### 16. Add Spring Security with basic auth for admin — MEDIUM
Protect `/admin` endpoints with username/password authentication.

### 17. Add rate limiting on quote API — MEDIUM
Use Bucket4j or a servlet filter to limit `/api/quotes` to N requests per minute per IP.

### 18. Add CSRF protection for Thymeleaf forms — EASY
Enable CSRF tokens in forms (Spring Security default) and verify tokens server-side.

### 19. Add input sanitization — EASY
Sanitize string inputs (postcode) to prevent XSS in Thymeleaf output.

---

## TypeScript Migration

### 20. Scaffold React + TypeScript frontend — MEDIUM
Create `/frontend` with Vite + React + TypeScript. Add proxy config for API calls.

### 21. Migrate Results page to React — MEDIUM
Build the results comparison view as a React component consuming `POST /api/quotes`.

### 22. Migrate Quote Form to React — MEDIUM
Build the form with react-hook-form + zod validation, calling the REST API.

### 23. Add E2E tests with Playwright — HARD
Set up Playwright to test the full flow: landing → form → results → detail → apply.

---

## Testing & Quality

### 24. Add integration tests with TestContainers — HARD
Full Spring Boot integration tests with real H2 database, verifying seed data and API flow.

### 25. Add OpenAPI contract tests — MEDIUM
Validate that API responses match the OpenAPI spec using springdoc + assertj.

---

## Completed Features

### 26. ~~Geolocated Maps Feature~~ — DONE
Interactive Leaflet.js maps showing provider locations on quote results and provider detail pages.

**What was delivered:**
- **Backend Architecture:** Introduced DTO layer (`ProviderDto`, `ProviderLocationDto`, `CoverageOptionDto`), shared mapper abstraction (`ProviderMapper`, `QuoteMapper`), abstract ranking base class (Template Method pattern), service layer fix (`QuoteService` → `ProviderService`), caching (`@Cacheable`), global exception handler (`@RestControllerAdvice`)
- **Data Layer:** Added `latitude`/`longitude` to `Provider` entity, seeded UK coordinates for all 8 providers, threaded lat/lng through `QuoteResult` and `QuoteResultDto`
- **Services:** `GeocodingService` (UK postcode prefix → coordinates lookup, ~30 outward codes), `DistanceUtil` (Haversine formula)
- **Feature Flag:** `mapEnabled` toggle in `FeatureFlagConfig`, admin panel toggle switch
- **Map API:** `MapApiController` at `/api/map/providers` (with postcode+radius filtering) and `/api/map/geocode`
- **Frontend:** Leaflet.js multi-marker map on results page (user + provider markers, fit bounds), single-marker map on provider detail page, "Distance: Nearest" client-side sort option, conditional rendering via `th:if="${mapEnabled}"`
- **Jira:** DEV-42 through DEV-53 (12 tickets under Epic DEV-41)
