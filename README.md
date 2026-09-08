# Support Ticket Analyzer

A practice project that automatically summarizes, classifies, and prioritizes customer support tickets using Claude.

## Overview

Customers submit support tickets, which are stored in MongoDB. Claude then analyzes each ticket to:

- **Summarize** the issue in plain language
- **Classify** it into a category (e.g. Billing, Technical Issue, Account Access, Bug Report, Feature Request, General Inquiry)
- **Assign a severity** level (Critical, High, Medium, Low)

The enriched analysis is written back to MongoDB, ready for use in dashboards, routing, or alerting.

## How It Works

1. A customer submits a ticket (subject + body) via [web form / API / manual entry]
2. The raw ticket is stored in the `tickets` collection in MongoDB
3. A processing script/service picks up new tickets and sends them to Claude for analysis
4. Claude returns a structured summary, category, and severity
5. The result is saved to the `ticket_analysis` collection, linked back to the original ticket

```
Customer → Ticket (MongoDB) → Claude Analysis → Enriched Result (MongoDB)
```

## Tech Stack

- **Database:** MongoDB (via Spring Data MongoDB)
- **Backend:** Java 25, Spring Boot
- **Build tool:** Gradle
- **AI:** Anthropic Claude API

## Getting Started

### Prerequisites

- Java 25 (JDK)
- No separate Gradle install needed — use the included `./gradlew` wrapper (Gradle 8.14.5)
- MongoDB instance (local or hosted, e.g. MongoDB Atlas)
- Anthropic API key

### Installation

```bash
git clone <repo-url>
cd support-ticket-analyzer
./gradlew build
```

### Environment Variables

Spring Boot reads configuration from `src/main/resources/application.properties` (or `application.yml`). Use environment variable placeholders so secrets aren't committed:

```properties
spring.data.mongodb.uri=${MONGODB_URI}
anthropic.api.key=${ANTHROPIC_API_KEY}
```

The project uses [spring-dotenv](https://github.com/paulschwarz/spring-dotenv) to auto-load a local `.env` file into Spring's environment, so you don't need to `export` these manually every session — it works the same whether you run via `./gradlew bootRun`, your IDE's Run button, or a terminal.

```bash
cp .env.example .env
```

Then edit `.env` and fill in your real values:

```
MONGODB_URI=your_mongodb_connection_string
ANTHROPIC_API_KEY=your_api_key
```

> `.env` is git-ignored — never commit real credentials. `.env.example` is the tracked template; keep it in sync with whatever variables the app actually reads.

### Running the Project

```bash
./gradlew bootRun
```

## Data Model

**`tickets` collection**

| Field | Type | Description |
|---|---|---|
| `_id` | ObjectId | Ticket ID |
| `customer_id` | string | Submitting customer |
| `subject` | string | Ticket subject line |
| `body` | string | Full ticket text |
| `submitted_at` | date | Submission timestamp |
| `status` | string | `new`, `processed`, or `error` |

**`ticket_analysis` collection**

| Field | Type | Description |
|---|---|---|
| `_id` | ObjectId | Analysis ID |
| `ticket_id` | ObjectId | Reference to the original ticket |
| `summary` | string | Claude-generated summary |
| `category` | string | Assigned category |
| `severity` | string | Assigned severity level |
| `analyzed_at` | date | When the analysis was run |

## Categories & Severity

**Categories:** Billing, Technical Issue, Account Access, Feature Request, Bug Report, General Inquiry

**Severity levels:**
- `Critical` — outage, data loss, or security issue
- `High` — major feature broken, no workaround
- `Medium` — partial issue, workaround exists
- `Low` — minor or cosmetic issue

## Project Structure

```
support-ticket-analyzer/
├── CLAUDE.md                          # Guidance for Claude Code when working in this repo
├── README.md                          # This file
├── build.gradle                       # Gradle project config
├── settings.gradle                    # Gradle project settings
├── gradlew / gradlew.bat              # Gradle wrapper scripts
├── gradle/wrapper/                    # Gradle wrapper jar + properties
└── src/
    ├── main/
    │   ├── java/com/example/ticketanalyzer/
    │   │   ├── model/                 # MongoDB document classes
    │   │   ├── repository/            # Spring Data MongoDB repositories
    │   │   ├── service/               # Business logic + Claude integration
    │   │   └── controller/            # REST endpoints (if applicable)
    │   └── resources/
    │       ├── application.properties
    │       └── prompts/                # Claude prompt templates
    └── test/
        └── java/com/example/ticketanalyzer/
```

## Status

🚧 Practice / learning project — not production-ready.

## License

[MIT / choose a license]

---
*This is a practice/learning project, not intended for production use.*
