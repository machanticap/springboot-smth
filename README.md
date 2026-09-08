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

- **Database:** MongoDB
- **Backend:** [Node.js / Python — fill in]
- **AI:** Anthropic Claude API

## Getting Started

### Prerequisites

- MongoDB instance (local or hosted, e.g. MongoDB Atlas)
- Anthropic API key
- [Node.js 18+ / Python 3.10+ — fill in]

### Installation

```bash
git clone <repo-url>
cd support-ticket-analyzer
[npm install / pip install -r requirements.txt]
```

### Environment Variables

Create a `.env` file in the project root:

```
MONGODB_URI=your_mongodb_connection_string
ANTHROPIC_API_KEY=your_api_key
```

> Never commit your `.env` file. Add it to `.gitignore`.

### Running the Project

```bash
[npm run dev / python main.py]
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
├── CLAUDE.md              # Guidance for Claude Code when working in this repo
├── README.md              # This file
├── src/                   # Application source code
├── prompts/               # Claude prompt templates
└── .env.example           # Environment variable template
```

## Status

🚧 Practice / learning project — not production-ready.

## License

[MIT / choose a license]
