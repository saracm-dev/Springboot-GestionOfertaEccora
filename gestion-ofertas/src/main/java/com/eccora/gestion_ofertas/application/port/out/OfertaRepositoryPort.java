package com.eccora.gestion_ofertas.application.port.out;

import com.eccora.gestion_ofertas.domain.entity.Oferta;

import java.util.List;
import java.util.Optional;

/**
 * Puerto de salida: operaciones de persistencia que necesita la aplicación.
 * Lo implementa OfertaRepositoryAdapter en infraestructura.
 */
public interface OfertaRepositoryPort {

    Oferta guardar(Oferta oferta);

    Optional<Oferta> buscarPorId(Long id);

    List<Oferta> buscarTodas();
}
