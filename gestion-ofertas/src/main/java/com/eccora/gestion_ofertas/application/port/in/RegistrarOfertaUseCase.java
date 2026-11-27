package com.eccora.gestion_ofertas.application.port.in;

import com.eccora.gestion_ofertas.domain.entity.Oferta;

import java.math.BigDecimal;

/**
 * Puerto de entrada: registrar una nueva oferta laboral.
 * La oferta se crea en estado BORRADOR.
 */
public interface RegistrarOfertaUseCase {

    Oferta registrar(RegistrarOfertaCommand command);

    record RegistrarOfertaCommand(
            String titulo,
            String descripcion,
            BigDecimal salario,
            Integer vacantes
    ) {
    }
}
