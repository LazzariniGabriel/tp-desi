package com.tpdesi.exceptionHandler;

import java.time.LocalDateTime;

public class ErrorResponse {
    private String mensaje;
    private int status;
    private LocalDateTime timestamp;

    public ErrorResponse(String mensaje, int status) {
        this.mensaje = mensaje;
        this.status = status;
        this.timestamp = LocalDateTime.now();
    }

    public String getMensaje() {
        return mensaje;
    }

    public int getStatus() {
        return status;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
