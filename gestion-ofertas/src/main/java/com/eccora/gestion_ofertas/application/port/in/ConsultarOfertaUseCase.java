package com.eccora.gestion_ofertas.application.port.in;

import com.eccora.gestion_ofertas.domain.entity.Oferta;

import java.util.List;

/**
 * Puerto de entrada: consultar una oferta por ID y obtener el listado.
 */
public interface ConsultarOfertaUseCase {

    Oferta consultarPorId(Long id);

    List<Oferta> consultarTodas();
}
