
package com.eccora.gestion_ofertas.infrastructure.adapter.in;

import com.eccora.gestion_ofertas.domain.entity.Oferta;
import com.eccora.gestion_ofertas.domain.entity.EstadoOferta;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record OfertaResponse(

        Long id,
        String titulo,
        BigDecimal salario,
        Integer vacantes,
        EstadoOferta estado,
        LocalDateTime fechaPublicacion

) {

    public static OfertaResponse fromDomain(Oferta oferta) {

        return new OfertaResponse(
                oferta.getId(),
                oferta.getTitulo(),
                oferta.getSalario(),
                oferta.getVacantes(),
                oferta.getEstado(),
                oferta.getFechaPublicacion()
        );
    }
}

