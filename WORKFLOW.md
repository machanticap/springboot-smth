# Email Notification Service - Development Plan

This project is currently in the planning stage. The repository contains only a README and no Gradle build files or source code. The goal is to build an event-driven Email Notification Service using **Spring Boot**, **Apache Kafka**, **Java 21**, and **Gradle**.

---

# 1. Project Scaffolding

## Objectives

Create the foundational Spring Boot project structure and dependencies.

### Tasks

1. Generate the project using **Spring Initializr** (or `gradle init`):
   - Java 21
   - Spring Boot 3.x
   - Gradle (Groovy or Kotlin DSL)

2. Add the following dependencies:

```gradle
spring-boot-starter-web
spring-kafka
spring-boot-starter-validation
spring-boot-starter-actuator
lombok (optional)
spring-boot-starter-test
spring-kafka-test
```

3. Create the standard package structure:

```text
src/main/java/com/capgemini/notification
├── api
├── event
├── kafka
└── service
```

---

# 2. Local Kafka Environment

## Objectives

Set up Kafka locally for development and testing.

### Tasks

1. Add a `docker-compose.yml` containing:
   - Kafka broker
   - Zookeeper (or KRaft single-node configuration)

2. Create the Kafka topic:

```text
user-registered
```

3. Verify Kafka is operational by producing and consuming test messages from the command line before integrating with Spring Boot.

---

# 3. Define the Event Contract

## Objectives

Establish the event format shared between producers and consumers.

### Tasks

1. Create the `UserRegisteredEvent` DTO/record.

Example payload:

```json
{
  "userId": 123,
  "email": "john@example.com",
  "timestamp": "2026-08-25T12:30:00"
}
```

2. Use JSON serialization with:

```text
JsonSerializer
JsonDeserializer
```

> Future enhancement: Consider Avro and Schema Registry for stronger event contracts.

---

# 4. Registration API (Producer)

## Objectives

Create an API that publishes registration events to Kafka.

### Tasks

### Create Registration Endpoint

```http
POST /api/users/register
```

Responsibilities:

- Accept registration data
- Validate request payload
- Return success response

### Create Producer Service

Implement a Kafka producer using:

```java
KafkaTemplate<String, UserRegisteredEvent>
```

Publish events to:

```text
user-registered
```

### Configure Application Properties

Add producer configuration in `application.yml`:

```yaml
spring:
  kafka:
    bootstrap-servers: localhost:9092
```

Externalize settings such as:

- Bootstrap servers
- Topic names
- Producer serialization settings

---

# 5. Notification Service (Consumer)

## Objectives

Consume registration events and simulate sending email notifications.

### Tasks

### Create Kafka Listener

Consumer group:

```text
notification-service
```

Topic:

```text
user-registered
```

Use:

```java
@KafkaListener
```

### Implement Email Service

Create an `EmailService` responsible for simulated email delivery.

Example log output:

```text
Received UserRegisteredEvent:
{
  "userId": 123,
  "email": "john@example.com"
}

Sending welcome email to john@example.com...

✅ Email sent successfully.
```

### Configure Consumer Settings

Configure:

- JSON deserialization
- Consumer groups
- Error handling

Optional enhancements:

- `DefaultErrorHandler`
- Dead Letter Topic (DLT)

---

# 6. Configuration & Profiles

## Objectives

Support multiple runtime environments.

### Tasks

Create environment profiles:

```text
application.yml
application-dev.yml
application-docker.yml
```

### Development Profile

```text
localhost:9092
```

### Docker Profile

Use the Kafka container hostname from Docker Compose.

---

# 7. Testing

## Objectives

Ensure producer and consumer functionality are working correctly.

### Unit Tests

Test:

- REST controller logic
- Validation
- Event mapping

### Integration Tests

Use:

```java
@EmbeddedKafka
```

or

```text
Testcontainers Kafka
```

Verify:

```text
Producer
   ↓
Kafka Topic
   ↓
Consumer
```

End-to-end event flow should be validated automatically.

---

# 8. Manual Validation

## Objectives

Verify application behavior from end to end.

### Tasks

1. Start Kafka:

```bash
docker compose up -d
```

2. Start the application:

```bash
./gradlew bootRun
```

3. Send a registration request:

```bash
curl -X POST http://localhost:8080/api/users/register \
-H "Content-Type: application/json" \
-d '{
  "userId": 123,
  "email": "john@example.com"
}'
```

4. Confirm consumer logs match the expected notification output.

---

# 9. Observability & Enhancements (Optional)

## Objectives

Improve monitoring, diagnostics, and operational visibility.

### Features

- Spring Boot Actuator
- Kafka health checks
- Structured logging
- Message processing metrics
- Consumer lag monitoring

Potential metrics:

```text
Messages Processed
Consumer Lag
Processing Failures
Average Processing Time
```

---

# 10. Documentation

## Objectives

Provide accurate setup and usage instructions.

### Update README

Replace planning-only content with actual implementation details.

Include:

### Running Kafka

```bash
docker compose up -d
```

### Running the Application

```bash
./gradlew bootRun
```

### API Example

```bash
curl -X POST http://localhost:8080/api/users/register \
-H "Content-Type: application/json" \
-d '{
  "userId": 123,
  "email": "john@example.com"
}'
```

### Expected Output

```text
Received UserRegisteredEvent

Sending welcome email to john@example.com...

✅ Email sent successfully.
```

---

# Project Milestones

## Phase 1: Foundation

- [ ] Spring Boot project created
- [ ] Java 21 configured
- [ ] Gradle build configured
- [ ] Kafka running locally

## Phase 2: Event Flow

- [ ] UserRegisteredEvent created
- [ ] REST registration endpoint implemented
- [ ] Kafka producer implemented
- [ ] Kafka consumer implemented

## Phase 3: Quality

- [ ] Unit tests added
- [ ] Integration tests added
- [ ] Error handling configured

## Phase 4: Production Readiness

- [ ] Actuator enabled
- [ ] Monitoring configured
- [ ] Documentation updated
- [ ] Docker support completed