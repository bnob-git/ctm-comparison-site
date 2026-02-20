# Next Steps — Upgrade & Migration Path

## 1. Java Version Upgrades

### Current: Java 17 + Spring Boot 3.2
- **Java 21 upgrade:** Switch `<java.version>` to 21 in `pom.xml`. Benefit: virtual threads, pattern matching, record patterns.
- **Spring Boot 3.3+:** Update parent version. Check for deprecations in `application.yml`.
- **Records for DTOs:** Replace `QuoteRequestDto`, `QuoteResultDto`, and `QuoteResult` with Java records to eliminate boilerplate.

### Steps
1. Update `pom.xml` → `<java.version>21</java.version>`
2. Update Spring Boot parent to latest 3.x
3. Run `./mvnw test` to verify compatibility
4. Convert DTOs to records incrementally
5. Enable virtual threads: `spring.threads.virtual.enabled=true`

## 2. Frontend Migration to TypeScript/React

### Phase 1: Add React alongside Thymeleaf
1. Create `/frontend` directory with Vite + React + TypeScript
2. Add `tsconfig.json` with strict mode
3. Proxy API calls to `localhost:8080` in Vite dev config
4. Build React pages one at a time, starting with the Results page

### Phase 2: Replace Thymeleaf pages
1. Landing page → React component
2. Quote form → React with react-hook-form + zod validation
3. Results page → React with client-side sorting/filtering
4. Provider detail → React component
5. Admin panel → React with feature flag toggles

### Phase 3: Remove Thymeleaf
1. Remove Thymeleaf dependency from `pom.xml`
2. Remove `templates/` directory
3. Configure Spring Boot to serve React build from `static/`
4. Or deploy frontend separately with CORS configuration

### Suggested TypeScript Stack
- **Framework:** React 18+ with Vite
- **Language:** TypeScript (strict mode)
- **Styling:** Tailwind CSS
- **Components:** shadcn/ui
- **Icons:** Lucide React
- **Forms:** react-hook-form + zod
- **State:** React Query (TanStack Query) for server state
- **Routing:** React Router v6

### tsconfig.json Placeholder
```json
{
  "compilerOptions": {
    "target": "ES2020",
    "lib": ["ES2020", "DOM", "DOM.Iterable"],
    "module": "ESNext",
    "moduleResolution": "bundler",
    "strict": true,
    "noUnusedLocals": true,
    "noUnusedParameters": true,
    "jsx": "react-jsx",
    "outDir": "./dist",
    "rootDir": "./src"
  },
  "include": ["src"]
}
```

## 3. Backend Modularization

### Current: Monolith
### Target: Modular monolith, then microservices

1. **Extract pricing module:** `com.compare.pricing` with its own `PricingEngine` interface
2. **Extract provider module:** `com.compare.provider` with repository + service
3. **Extract ranking module:** `com.compare.ranking` with strategy pattern
4. **Add Spring Modulith:** Use `@ApplicationModule` annotations for boundary enforcement
5. **Event-driven communication:** Use Spring Application Events between modules

### Future: Microservices
- Quote Service (handles requests, orchestrates)
- Provider Service (manages provider data)
- Pricing Service (calculates prices)
- API Gateway (routes, auth)

## 4. Database Migration

1. **Flyway/Liquibase:** Add migration scripts instead of `ddl-auto: create-drop`
2. **PostgreSQL:** Switch from H2 to PostgreSQL for production
3. **Redis cache:** Cache provider data and recent quotes

## 5. Security Hardening

1. Add Spring Security with basic auth for admin endpoints
2. Rate limiting on `/api/quotes`
3. CSRF protection for web forms
4. Input sanitization
5. CORS configuration for separate frontend

## 6. Observability

1. Spring Boot Actuator endpoints
2. Micrometer metrics + Prometheus
3. Structured JSON logging (Logback + logstash-encoder)
4. Distributed tracing with Micrometer Tracing
