package com.eccora.gestion_ofertas.application.port.in;

import com.eccora.gestion_ofertas.domain.entity.Oferta;

/**
 * Puerto de entrada: cerrar una oferta PUBLICADA.
 */
public interface CerrarOfertaUseCase {

    Oferta cerrar(Long id);
}
