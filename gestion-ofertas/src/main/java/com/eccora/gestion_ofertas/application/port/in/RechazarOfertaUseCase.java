package com.eccora.gestion_ofertas.application.port.in;

import com.eccora.gestion_ofertas.domain.entity.Oferta;

/**
 * Puerto de entrada: rechazar una oferta en PENDIENTE_REVISION registrando el motivo.
 */
public interface RechazarOfertaUseCase {

    Oferta rechazar(Long id, String motivoRechazo);
}
