# TODO

Tracks remaining work for the Support Ticket Analyzer. Check items off as they land; add new ones as scope firms up.

## Done

- [x] Gradle scaffold (Spring Boot 3.4.13, Java 21 toolchain, Spring Data MongoDB/Web/Validation/Actuator)
- [x] `Ticket` model + `TicketStatus` enum (`customer_id` validated as UUID on construction)
- [x] `TicketRepository`

## Ticket intake

- [ ] `TicketController` — `POST` endpoint to create a ticket, persist via `TicketRepository`
- [ ] Decide response shape (full ticket, id only, or a bare ack)
- [ ] Request validation (non-blank subject/body, allowed `channel` values)

## Ticket analysis

- [ ] `TicketAnalysis` model + repository (`category`/`severity` as enums matching the CLAUDE.md taxonomy, never free-form)
- [ ] Confirm Claude client: raw HTTP vs Anthropic Java SDK — open `[ASSUMPTION]` in CLAUDE.md
- [ ] `src/main/resources/prompts/ticket_analysis_prompt.md` per coding conventions
- [ ] Service that calls Claude, parses the response into summary/category/severity, and enforces the fixed taxonomy
- [ ] Versioning/timestamping strategy for re-analysis (never silently overwrite `ticket_analysis`)
- [ ] Log the raw Claude response for auditability without logging unredacted PII ticket bodies

## Processing trigger (unconfirmed)

- [ ] Decide how new tickets get picked up for analysis — Mongo change stream, polling, or triggered synchronously from the intake endpoint

## Compliance / data privacy

- [ ] Confirm with the Data Privacy Officer whether ticket bodies need encryption at rest, field-level masking, or retention limits
- [ ] Confirm no raw customer PII is sent to Claude beyond what's approved

## Testing

- [ ] Repository/integration tests against MongoDB (e.g. Testcontainers)
- [ ] Controller tests

## Config / ops

- [ ] `MONGODB_URI` / `ANTHROPIC_API_KEY` set up per environment (see README)
- [ ] Confirm downstream consumers (dashboards/routing/alerting) — CLAUDE.md overview is still marked as pending this
