# CLAUDE.md

This file provides guidance to Claude Code when working with code in this repository.

## Project Overview

**Support Ticket Analyzer** — an automated pipeline that:
1. Receives customer support tickets (submitted via API)
2. Stores raw tickets in MongoDB
3. Uses Claude to:
   - Summarize the issue in plain language
   - Classify the ticket into a category
   - Assign a severity level
4. Writes the enriched result back to MongoDB for downstream use (dashboards, routing, alerting)

> Edit this overview once the intake mechanism and downstream consumers are finalized.

## Tech Stack

- **Database:** MongoDB (via Spring Data MongoDB)
- **Backend language/framework:** Java 25, Spring Boot
- **Build tool:** Gradle
- **LLM integration:** Anthropic API (Claude), called via [ASSUMPTION: Java HTTP client / Anthropic Java SDK — confirm which]

## Data Model (MongoDB)

### `tickets` collection (raw input)

```json
{
  "_id": "ObjectId",
  "customer_id": "string",   // UUID (validated on Ticket construction)
  "subject": "string",
  "body": "string",
  "submitted_at": "ISODate",
  "channel": "string",       // e.g. "email", "web_form", "chat"
  "status": "string"          // "new" | "processed" | "error"
}
```

### `ticket_analysis` collection (Claude output)

```json
{
  "_id": "ObjectId",
  "ticket_id": "ObjectId",    // ref to tickets._id
  "summary": "string",
  "category": "string",
  "severity": "string",
  "confidence": "number",     // optional, if you want Claude to self-report confidence
  "analyzed_at": "ISODate",
  "model_version": "string"
}
```

> [ASSUMPTION] Adjust field names/collections to match your actual schema. If tickets contain PII (names, emails, account numbers), confirm with the Data Privacy Officer whether encryption at rest, field-level masking, or retention limits are required before storing raw ticket bodies.

## Classification Taxonomy

**Categories:**
- `Billing`
- `Technical Issue`
- `Account Access`
- `Feature Request`
- `Bug Report`
- `General Inquiry`

**Severity levels:**
- `Critical` — service outage, data loss, security issue, blocking all users
- `High` — major feature broken, no workaround, affects many customers
- `Medium` — feature partially broken, workaround exists
- `Low` — cosmetic issue, question, or minor inconvenience

> Claude should always choose from this fixed list — never invent new categories or severities on the fly. If a ticket doesn't cleanly fit, default to the closest category and note the ambiguity in the summary rather than fabricating a new label.

## Core Workflow / Common Commands

```bash
./gradlew bootRun                            # start the analyzer service locally
./gradlew test                               # run test suite
./gradlew build                              # build the jar
java -jar build/libs/ticket-analyzer-0.0.1-SNAPSHOT.jar # run the built jar
```

## Coding Conventions

- Follow standard Java naming conventions (PascalCase for classes, camelCase for methods/fields).
- Use Spring's constructor injection over field injection (`@Autowired` on fields) for testability.
- Keep MongoDB documents mapped via `@Document`-annotated model classes in a dedicated `model` package; keep repositories (`MongoRepository` interfaces) in a `repository` package.
- Keep the Claude prompt (system prompt / instructions for summarization+classification) in a single, version-controlled resource file (e.g. `src/main/resources/prompts/ticket_analysis_prompt.md`) rather than inline strings scattered across the codebase.
- Log the raw Claude response alongside the parsed result for auditability, but never log full ticket bodies containing PII to plaintext files without redaction.

## Things to Avoid

- **Do not** fabricate categories or severities outside the defined taxonomy.
- **Do not** hardcode API keys or MongoDB connection strings in this repo — use environment variables / secrets manager.
- **Do not** send raw customer PII to any external service beyond what's approved (confirm with Data Privacy Officer if scope expands, e.g. adding third-party enrichment tools).
- **Do not** silently overwrite `ticket_analysis` records — version or timestamp re-analyses if a ticket is reprocessed.

## Current Focus

[fill in: e.g. "Currently building the MongoDB change-stream listener that triggers analysis on new ticket inserts."]

---
*This file should evolve as the project takes shape — replace bracketed assumptions with real project details as they're confirmed.*
