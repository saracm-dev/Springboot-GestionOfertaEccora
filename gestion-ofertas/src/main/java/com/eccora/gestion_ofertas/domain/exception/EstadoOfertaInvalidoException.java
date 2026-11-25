package com.eccora.gestion_ofertas.domain.exception;

import com.eccora.gestion_ofertas.domain.entity.EstadoOferta;

public class EstadoOfertaInvalidoException extends RuntimeException {

    public EstadoOfertaInvalidoException(String message) {
        super(message);
    }

    public EstadoOfertaInvalidoException(EstadoOferta estadoActual, EstadoOferta estadoDestino) {
        super("Transición de estado inválida: no se puede pasar de " + estadoActual + " a " + estadoDestino);
    }

    public EstadoOfertaInvalidoException(String accion, EstadoOferta estadoActual) {
        super("No se puede " + accion + " una oferta en estado " + estadoActual);
    }
}
