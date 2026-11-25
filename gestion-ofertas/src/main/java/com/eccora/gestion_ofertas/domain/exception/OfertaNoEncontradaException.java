package com.eccora.gestion_ofertas.domain.exception;

public class OfertaNoEncontradaException extends RuntimeException {

    public OfertaNoEncontradaException(String message) {
        super(message);
    }

    public OfertaNoEncontradaException(Long id) {
        super("No se encontró la oferta con id: " + id);
    }
}
