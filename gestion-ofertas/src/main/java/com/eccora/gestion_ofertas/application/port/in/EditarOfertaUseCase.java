package com.eccora.gestion_ofertas.application.port.in;

import com.eccora.gestion_ofertas.domain.entity.Oferta;

import java.math.BigDecimal;

/**
 * Puerto de entrada: modificar los datos de una oferta.
 * Solo se permite en estado BORRADOR o RECHAZADA (lo valida el dominio).
 */
public interface EditarOfertaUseCase {

    Oferta editar(Long id, EditarOfertaCommand command);

    record EditarOfertaCommand(
            String titulo,
            String descripcion,
            BigDecimal salario,
            Integer vacantes
    ) {
    }
}
