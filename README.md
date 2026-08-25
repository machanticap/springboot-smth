# Email Notification Service

## Overview

This project demonstrates a simple event-driven architecture using **Spring Boot**, **Apache Kafka**, **Java 21**, and **Gradle**.

When a user registers, the application publishes a Kafka event. A notification service then consumes the event and simulates sending an email to the user.

---

## Scenario

Applications create events, and Kafka handles notification delivery asynchronously.

---

## Workflow

```text
┌─────────────────┐
│  User Registers │
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│ Registration API│
└────────┬────────┘
         │ Publishes
         ▼
┌─────────────────┐
│   Kafka Topic   │
│ user-registered │
└────────┬────────┘
         │ Consumes
         ▼
┌─────────────────────┐
│ Notification Service│
└────────┬────────────┘
         │
         ▼
┌─────────────────┐
│ Simulated Email │
│     Sent        │
└─────────────────┘
```

---

## Event Flow

1. A user registers through a REST API.
2. The API publishes a `UserRegisteredEvent` to Kafka.
3. The Notification Service consumes the event.
4. The service simulates sending an email and logs the result.

---

## Example Event

```json
{
  "userId": 123,
  "email": "john@example.com",
  "timestamp": "2026-08-25T12:30:00"
}
```

---

## Kafka Components

### Producer

The Registration API publishes a `UserRegisteredEvent` to Kafka.

### Topic

```text
user-registered
```

### Consumer

The Notification Service listens for events from the `user-registered` topic and processes them.

---

## Sample Output

```text
Received UserRegisteredEvent:
{
  "userId": 123,
  "email": "john@example.com"
}

Sending welcome email to john@example.com...

✅ Email sent successfully.
```

---

## Technologies

- Java 21
- Spring Boot 3.x
- Apache Kafka
- Gradle
- Docker Compose (optional)

---

## Learning Objectives

By building this project, you will learn:

- Kafka Producers
- Kafka Consumers
- Event-driven architecture
- Asynchronous communication
- JSON event serialization/deserialization
- Spring Boot Kafka integration
- Consumer groups and topic subscriptions

---

## Why This Project?

This is a small but practical project that clearly demonstrates Kafka's purpose in modern distributed systems. It's easy to build, easy to explain in interviews, and provides a solid foundation for more advanced event-driven applications.
