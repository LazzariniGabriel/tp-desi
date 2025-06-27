package com.tpdesi.controllers;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SuccessResponse {
    private String mensaje;
    private Object datos;
}
