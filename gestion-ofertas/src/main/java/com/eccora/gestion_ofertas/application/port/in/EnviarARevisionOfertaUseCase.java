package com.eccora.gestion_ofertas.application.port.in;

import com.eccora.gestion_ofertas.domain.entity.Oferta;

/**
 * Puerto de entrada: enviar una oferta en BORRADOR o RECHAZADA a PENDIENTE_REVISION.
 * Sin este paso una oferta nueva no podría llegar a aprobarse.
 */
public interface EnviarARevisionOfertaUseCase {

    Oferta enviarARevision(Long id);
}
