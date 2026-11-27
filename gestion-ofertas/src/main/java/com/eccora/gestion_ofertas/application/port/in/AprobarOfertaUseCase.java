package com.eccora.gestion_ofertas.application.port.in;

import com.eccora.gestion_ofertas.domain.entity.Oferta;

/**
 * Puerto de entrada: aprobar una oferta en PENDIENTE_REVISION.
 */
public interface AprobarOfertaUseCase {

    Oferta aprobar(Long id);
}
