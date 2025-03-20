package com.criticalblunder.exception;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;

class ErrorResponseTest {

    @Test
    void shouldCreateErrorResponseSuccessfully() {
        LocalDateTime timestamp = LocalDateTime.now();
        ErrorResponse errorResponse = new ErrorResponse("Error de prueba", 400, timestamp);

        assertEquals("Error de prueba", errorResponse.getMessage());
        assertEquals(400, errorResponse.getStatus());
        assertEquals(timestamp, errorResponse.getTimestamp());
    }
}