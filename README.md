# springboot-smth
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

1. A
