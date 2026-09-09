# TODO

Tracks remaining work for the Support Ticket Analyzer. Check items off as they land; add new ones as scope firms up.

## Done

- [x] Gradle scaffold (Spring Boot 3.4.13, Java 25 toolchain, Spring Data MongoDB/Web/Validation/Actuator)
- [x] `Ticket` model + `TicketStatus` enum (`customer_id` validated as UUID on construction)
- [x] `TicketRepository`
- [x] `TicketController` — `POST /api/tickets` endpoint, returns full saved `Ticket` (caller gets generated `_id`)
- [x] Response shape: full ticket object
- [x] Request validation (non-blank subject/body, `customerId` UUID format, `channel` restricted to email/web_form/chat) — returns 400 with field-level messages

## Ticket intake

Intake is functionally complete pending real MongoDB wiring; move to Ticket analysis next.

## Ticket analysis

- [x] `TicketAnalysis` model + repository (`category`/`severity` as enums matching the CLAUDE.md taxonomy, never free-form) — `findByTicketIdOrderByAnalyzedAtDesc` added to support querying re-analysis history
- [x] Confirm Claude client: Anthropic Java SDK (`com.anthropic:anthropic-java:2.61.0`) — chosen for built-in structured outputs, typed retries/errors, and automatic `.env` API key pickup
- [x] `src/main/resources/prompts/ticket_analysis_prompt.md` per coding conventions
- [x] `TicketAnalysisService` — calls Claude via the SDK's structured outputs (`TicketAnalysisResult` record), enforcing `Category`/`Severity` as real enums so Claude cannot return an invented label
- [x] `POST /api/tickets/{id}/analyze` — manual trigger endpoint; marks the ticket `PROCESSED` on success, `ERROR` on failure
- [x] Versioning/timestamping: each analysis is a new inserted document (never an update-in-place), so re-analysis never silently overwrites; `findByTicketIdOrderByAnalyzedAtDesc` retrieves history
- [x] Logs the raw Claude response (`TicketAnalysisService`) for auditability — only the response is logged, never the outgoing ticket body
- [ ] **Blocked:** end-to-end verification against a live Claude call — the Capgemini-provisioned Anthropic API key returns `401 invalid` (key format/wiring confirmed correct via diagnostic logging in `AnthropicConfig`; likely a billing/provisioning issue on the account, needs internal follow-up). Remove the temporary diagnostic logging in `AnthropicConfig` once resolved.

## Processing trigger (unconfirmed)

- [ ] Decide how new tickets get picked up for analysis — Mongo change stream, polling, or triggered synchronously from the intake endpoint

## Compliance / data privacy

- [ ] Confirm with the Data Privacy Officer whether ticket bodies need encryption at rest, field-level masking, or retention limits
- [ ] Confirm no raw customer PII is sent to Claude beyond what's approved

## Testing

- [ ] Repository/integration tests against MongoDB (e.g. Testcontainers)
- [ ] Controller tests

## Config / ops

- [x] `.env` / `.env.example` workflow via spring-dotenv (see README) — each dev still needs their own MongoDB Atlas cluster + connection string
- [ ] Confirm downstream consumers (dashboards/routing/alerting) — CLAUDE.md overview is still marked as pending this
