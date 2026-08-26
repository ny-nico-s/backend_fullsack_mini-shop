# MiniShop Light – Backend

Kleine Produktverwaltung: Produkte gehören zu einer Kategorie, werden in PostgreSQL gespeichert und über eine REST-API ausgeliefert.

## Technik

| | |
|---|---|
| Java | 21 (Gradle Toolchain) |
| Framework | Spring Boot 4.1 – Web, Data JPA |
| Datenbank | PostgreSQL 16 im Docker-Container |
| Build | Gradle 9.5 mit Kotlin DSL |
| Tests | JUnit 5, H2 In-Memory-Datenbank |

## Voraussetzungen

- Docker Desktop
- JDK 21 (lädt Gradle bei Bedarf selbst herunter)

## Starten

Datenbank hochfahren:

```bash
docker compose up -d
```

Anwendung starten:

```bash
./gradlew bootRun
```

Läuft auf http://localhost:8080. Beim ersten Start legt Hibernate die Tabellen an, `data.sql` füllt 2 Kategorien und 6 Produkte ein.

Datenbank stoppen:

```bash
docker compose down
```

Die Daten liegen in einem Docker-Volume und überleben den Neustart. Mit `docker compose down -v` werden sie gelöscht.

## Tests

```bash
./gradlew test
```

Zehn Tests, laufen gegen eine H2-Datenbank im Arbeitsspeicher. Docker muss dafür nicht laufen. Der Report liegt danach unter `build/reports/tests/test/index.html`.

Kompletter Build inklusive Tests:

```bash
./gradlew build
```

## API

Basis-URL: `/api`

| Methode | Pfad | Zweck | Antwort |
|---|---|---|---|
| GET | `/api/categories` | Alle Kategorien | 200 |
| POST | `/api/categories` | Kategorie anlegen | 200 |
| GET | `/api/products` | Alle Produkte | 200 |
| GET | `/api/products?categoryId=1` | Produkte einer Kategorie | 200 |
| GET | `/api/products/{id}` | Ein Produkt | 200, sonst 404 |
| POST | `/api/products` | Produkt anlegen | 200 |
| PUT | `/api/products/{id}` | Produkt ändern | 200, sonst 404 |
| DELETE | `/api/products/{id}` | Produkt löschen | 204, sonst 404 |

Beispiel für einen POST auf `/api/products`:

```json
{
  "name": "Apfelsaft 1L",
  "price": 2.50,
  "stock": 40,
  "category": { "id": 1 }
}
```

Alle Requests liegen in `api.http` und lassen sich im IntelliJ HTTP Client direkt ausführen.

## Datenmodell

```
Category 1 ── * Product
```

| Category | Typ | |
|---|---|---|
| id | Long | Primärschlüssel |
| name | String | Pflicht |

| Product | Typ | |
|---|---|---|
| id | Long | Primärschlüssel |
| name | String | Pflicht |
| price | BigDecimal | Pflicht, Spalte `numeric(10,2)` |
| stock | Integer | Pflicht, Startwert 0 |
| category | Category | `@ManyToOne` |

Der Preis ist bewusst ein `BigDecimal` und kein `double`, sonst stimmen die Rappen nicht.

Die Beziehung ist nur in eine Richtung gesetzt. Ohne `@OneToMany` zurück auf `Product` kann das JSON nicht in eine Endlosrekursion laufen.

## Aufbau

```
src/main/java/ch/nico/minishop/
├── MiniShopApplication.java
├── category/
│   ├── Category.java
│   ├── CategoryRepository.java
│   ├── CategoryService.java
│   └── CategoryController.java
└── product/
    ├── Product.java
    ├── ProductRepository.java
    ├── ProductService.java
    └── ProductController.java
```

Pro Fachbereich ein Package, darin Entity, Repository, Service und Controller.

## Datenbank-Zugang

| | |
|---|---|
| Host | localhost:5432 |
| Datenbank | minishop |
| Benutzer | minishop |
| Passwort | minishop |

Direkt in der Datenbank nachschauen:

```bash
docker exec -it minishop-db psql -U minishop -d minishop
```

## Frontend

Das React-Frontend kommt in den Ordner `frontend/`. CORS ist im Backend für http://localhost:5173 freigegeben.
