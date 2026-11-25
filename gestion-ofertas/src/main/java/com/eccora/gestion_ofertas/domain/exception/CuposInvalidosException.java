package com.eccora.gestion_ofertas.domain.exception;

public class CuposInvalidosException extends RuntimeException {

    public CuposInvalidosException(String message) {
        super(message);
    }

    public CuposInvalidosException(Integer vacantes) {
        super("El número de vacantes debe ser mayor a cero. Valor recibido: " + vacantes);
    }
}
