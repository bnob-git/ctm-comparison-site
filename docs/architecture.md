# Architecture Overview

## System Design

InsureCompare is a server-rendered monolithic Spring Boot application that demonstrates a car insurance price comparison flow.

```
┌─────────────────────────────────────────────────┐
│                   Browser                        │
│  (Thymeleaf HTML + Bootstrap 5 + vanilla JS)     │
└─────────────┬───────────────────────┬───────────┘
              │ HTML pages            │ REST API
              ▼                       ▼
┌─────────────────────────────────────────────────┐
│              Spring Boot Application             │
│                                                  │
│  ┌──────────────┐    ┌──────────────────┐       │
│  │ Web          │    │ API Controllers   │       │
│  │ Controllers  │    │ (REST + Swagger)  │       │
│  └──────┬───────┘    └────────┬─────────┘       │
│         │                     │                  │
│         ▼                     ▼                  │
│  ┌──────────────────────────────────────┐       │
│  │           Service Layer               │       │
│  │  QuoteService → PricingEngine         │       │
│  │               → RankingService        │       │
│  │  ProviderService                      │       │
│  └──────────────────┬───────────────────┘       │
│                     │                            │
│                     ▼                            │
│  ┌──────────────────────────────────────┐       │
│  │         Repository Layer              │       │
│  │  ProviderRepository (JPA)             │       │
│  │  CoverageOptionRepository (JPA)       │       │
│  └──────────────────┬───────────────────┘       │
│                     │                            │
│                     ▼                            │
│  ┌──────────────────────────────────────┐       │
│  │         H2 In-Memory Database         │       │
│  │  (seeded from providers.json)         │       │
│  └──────────────────────────────────────┘       │
└─────────────────────────────────────────────────┘
```

## Key Design Decisions

1. **Thymeleaf over React** — Single-command startup, no CORS, fastest path to demo-ready UX. Migration path documented in NEXT_STEPS.md.

2. **Interface-based services** — `PricingEngine` and `RankingService` are interfaces with swappable implementations, enabling feature flags and A/B testing.

3. **Feature flags via config** — `FeatureFlagConfig` is a mutable `@Configuration` bean, togglable at runtime via the admin panel.

4. **Deterministic pricing** — Random noise is seeded by provider ID + driver age, ensuring reproducible results for testing.

5. **Dual controller layer** — Web controllers serve Thymeleaf pages; API controllers serve JSON. Both call the same service layer. This enables future frontend migration without backend changes.

## Data Flow: Quote Request

1. User fills form → POST to `QuoteWebController` (or `QuoteApiController`)
2. `QuoteService` loads all providers from `ProviderRepository`
3. `PricingEngine.calculateQuotes()` computes price per provider
4. `RankingService.rank()` scores and sorts results
5. Results returned to view (or JSON response)

## Domain Model

- **Provider** (JPA entity) — name, baseRate, riskMultiplier, fixedFee, rating, features
- **CoverageOption** (JPA entity) — coverLevel, coverMultiplier, linked to Provider
- **QuoteRequest** (value object) — driverAge, carValue, postcode, mileage, claims, coverLevel
- **QuoteResult** (value object) — computed prices, score, badges
