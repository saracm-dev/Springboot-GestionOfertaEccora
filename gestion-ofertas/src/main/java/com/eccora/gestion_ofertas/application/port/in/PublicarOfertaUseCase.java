package com.eccora.gestion_ofertas.application.port.in;

import com.eccora.gestion_ofertas.domain.entity.Oferta;

/**
 * Puerto de entrada: publicar una oferta APROBADA.
 */
public interface PublicarOfertaUseCase {

    Oferta publicar(Long id);
}
