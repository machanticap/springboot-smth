# springboot-smth
Email Notification Service
Scenario

Applications create events and Kafka handles notifications.

Flow
User registers.
API publishes UserRegisteredEvent.
Notification service consumes event.
Simulated email is sent.
Example Event
JSON
1
{
2
"userId": 123,
3
"email": "john@example.com",
4
"timestamp": "2026-08-25T12:30:00"
5
}
6
``
Very small scope while demonstrating Kafka's purpose.
