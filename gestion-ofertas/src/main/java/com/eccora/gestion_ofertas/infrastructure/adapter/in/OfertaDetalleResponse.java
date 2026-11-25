
package com.eccora.gestion_ofertas.infrastructure.adapter.in;

import com.eccora.gestion_ofertas.domain.entity.Oferta;
import com.eccora.gestion_ofertas.domain.entity.EstadoOferta;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record OfertaDetalleResponse(

        Long id,
        String titulo,
        String descripcion,
        BigDecimal salario,
        Integer vacantes,
        EstadoOferta estado,
        String motivoRechazo,
        LocalDateTime fechaCreacion,
        LocalDateTime fechaActualizacion,
        LocalDateTime fechaPublicacion,
        LocalDateTime fechaCierre

) {

    public static OfertaDetalleResponse fromDomain(Oferta oferta) {

        return new OfertaDetalleResponse(
                oferta.getId(),
                oferta.getTitulo(),
                oferta.getDescripcion(),
                oferta.getSalario(),
                oferta.getVacantes(),
                oferta.getEstado(),
                oferta.getMotivoRechazo(),
                oferta.getFechaCreacion(),
                oferta.getFechaActualizacion(),
                oferta.getFechaPublicacion(),
                oferta.getFechaCierre()
        );
    }
}

