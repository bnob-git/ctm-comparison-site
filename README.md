# InsureCompare MVP

A car insurance price comparison prototype built with Java/Spring Boot. Demonstrates an end-to-end flow: Landing → Quote Form → Results Comparison → Provider Details → Application Placeholder.

All providers, pricing, and data are **synthetic** — no real insurance products are offered.

## Tech Stack

- **Backend:** Java 20, Spring Boot 3.2, Spring Data JPA, Thymeleaf
- **Database:** H2 (in-memory)
- **UI:** Thymeleaf + Bootstrap 5 (CDN)
- **API Docs:** SpringDoc OpenAPI (Swagger UI)
- **Tests:** JUnit 5, MockMvc

## Quick Start

```bash
cd backend
./mvnw spring-boot:run
```

Then open:
- **App:** http://localhost:8080
- **Swagger UI:** http://localhost:8080/swagger-ui.html
- **H2 Console:** http://localhost:8080/h2-console (JDBC URL: `jdbc:h2:mem:comparedb`, user: `sa`, no password)
- **Admin Panel:** http://localhost:8080/admin

Or use the helper script:
```bash
./scripts/run.sh
```

## Running Tests

```bash
cd backend
./mvnw test
```

## API Endpoints

| Method | Path | Description |
|--------|------|-------------|
| POST | `/api/quotes` | Submit quote request, get ranked results |
| GET | `/api/providers` | List all providers |
| GET | `/api/providers/{id}` | Get provider details |
| GET | `/api/health` | Health check |

### Example Quote Request

```bash
curl -X POST http://localhost:8080/api/quotes \
  -H "Content-Type: application/json" \
  -d '{
    "driverAge": 30,
    "carValue": 15000,
    "postcode": "SW1A 1AA",
    "annualMileage": 10000,
    "claimsInLastFiveYears": 0,
    "coverLevel": "comprehensive"
  }'
```

## Project Structure

```
compare-insurance/
├── backend/                    # Spring Boot application
│   ├── src/main/java/com/compare/
│   │   ├── config/             # Feature flags, ranking config
│   │   ├── controller/
│   │   │   ├── api/            # REST API controllers
│   │   │   └── web/            # Thymeleaf web controllers
│   │   ├── domain/             # Entities and value objects
│   │   ├── dto/                # Request/response DTOs
│   │   ├── repository/         # Spring Data JPA repositories
│   │   ├── seed/               # Data seeder (loads providers.json)
│   │   └── service/            # Business logic (pricing, ranking)
│   └── src/main/resources/
│       ├── data/providers.json # Seed data (8 providers)
│       ├── templates/          # Thymeleaf templates
│       └── static/             # CSS/JS assets
├── docs/                       # Architecture documentation
├── scripts/                    # Dev helper scripts
├── BACKLOG.md                  # 20 scoped tasks for future work
└── NEXT_STEPS.md               # Upgrade & migration path
```

## Providers (Synthetic)

| Provider | Base Rate | Rating | Specialty |
|----------|-----------|--------|-----------|
| SafeDrive | £320 | 4.5 | Roadside assistance, courtesy car |
| QuickCover | £280 | 3.8 | Online-first, budget-friendly |
| BudgetShield | £250 | 3.2 | Low cost, basic coverage |
| PremiumGuard | £420 | 4.8 | Premium features, all extras |
| EcoInsure | £300 | 4.1 | Green/eco-friendly benefits |
| UrbanProtect | £340 | 4.0 | City-optimized coverage |
| CountryMutual | £290 | 4.3 | Rural-focused benefits |
| TechSure | £310 | 4.2 | Telematics and app-based |

## Feature Flags

Toggle via the Admin panel at `/admin`:
- **Experimental Ranking:** Uses a value-for-money metric with adjusted weights

## License

Prototype — not for production use.
