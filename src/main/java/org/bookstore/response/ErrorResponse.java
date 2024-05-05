package org.bookstore.response;

import org.springframework.http.HttpStatus;

public record ErrorResponse(HttpStatus status, String message) {
}

