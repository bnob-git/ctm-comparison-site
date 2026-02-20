#!/usr/bin/env bash
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_DIR="$(dirname "$SCRIPT_DIR")"
BACKEND_DIR="$PROJECT_DIR/backend"

echo "=== InsureCompare MVP ==="
echo "Starting Spring Boot application..."
echo ""
echo "App:        http://localhost:8080"
echo "Swagger:    http://localhost:8080/swagger-ui.html"
echo "H2 Console: http://localhost:8080/h2-console"
echo "Admin:      http://localhost:8080/admin"
echo ""

cd "$BACKEND_DIR"
./mvnw spring-boot:run
