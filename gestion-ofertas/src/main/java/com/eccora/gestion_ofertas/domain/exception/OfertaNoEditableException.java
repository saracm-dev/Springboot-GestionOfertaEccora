package com.eccora.gestion_ofertas.domain.exception;

import com.eccora.gestion_ofertas.domain.entity.EstadoOferta;

public class OfertaNoEditableException extends RuntimeException {

    public OfertaNoEditableException(String message) {
        super(message);
    }

    public OfertaNoEditableException(EstadoOferta estadoActual) {
        super("La oferta no es editable en estado " + estadoActual);
    }
}
