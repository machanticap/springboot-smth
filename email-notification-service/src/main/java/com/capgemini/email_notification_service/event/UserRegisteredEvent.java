package com.capgemini.email_notification_service.event;

import java.time.LocalDateTime;

public record UserRegisteredEvent(
        Long userId,
        String email,
        LocalDateTime timestamp
) {
}
