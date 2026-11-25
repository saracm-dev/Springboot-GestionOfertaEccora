
package com.eccora.gestion_ofertas.infrastructure.adapter.out.persistence;

import com.eccora.gestion_ofertas.domain.entity.Oferta;
import org.springframework.stereotype.Component;

@Component
public class OfertaMapper {

    public OfertaJpaEntity toEntity(Oferta oferta) {

        OfertaJpaEntity entity = new OfertaJpaEntity();

        entity.setId(oferta.getId());
        entity.setTitulo(oferta.getTitulo());
        entity.setDescripcion(oferta.getDescripcion());
        entity.setSalario(oferta.getSalario());
        entity.setVacantes(oferta.getVacantes());
        entity.setEstado(oferta.getEstado());
        entity.setMotivoRechazo(oferta.getMotivoRechazo());
        entity.setFechaCreacion(oferta.getFechaCreacion());
        entity.setFechaActualizacion(oferta.getFechaActualizacion());
        entity.setFechaPublicacion(oferta.getFechaPublicacion());
        entity.setFechaCierre(oferta.getFechaCierre());

        return entity;
    }

    public Oferta toDomain(OfertaJpaEntity entity) {

        return Oferta.reconstituir(
                entity.getId(),
                entity.getTitulo(),
                entity.getDescripcion(),
                entity.getSalario(),
                entity.getVacantes(),
                entity.getEstado(),
                entity.getMotivoRechazo(),
                entity.getFechaCreacion(),
                entity.getFechaActualizacion(),
                entity.getFechaPublicacion(),
                entity.getFechaCierre()
        );
    }
}

