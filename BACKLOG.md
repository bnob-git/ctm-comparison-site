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

### 7. Add global exception handler — EASY
Create `@ControllerAdvice` with handlers for validation errors, 404s, and 500s.

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

### 13. Add caching for provider data — EASY
Use `@Cacheable` on `ProviderService.getAllProviders()` with Spring Cache abstraction.

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
