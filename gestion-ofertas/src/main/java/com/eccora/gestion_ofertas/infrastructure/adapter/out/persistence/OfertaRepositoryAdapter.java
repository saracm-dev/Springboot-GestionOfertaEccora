package com.eccora.gestion_ofertas.infrastructure.adapter.out.persistence;

import com.eccora.gestion_ofertas.application.port.out.OfertaRepositoryPort;
import com.eccora.gestion_ofertas.domain.entity.Oferta;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class OfertaRepositoryAdapter implements OfertaRepositoryPort {

    private final OfertaJpaRepository repository;
    private final OfertaMapper mapper;

    public OfertaRepositoryAdapter(
            OfertaJpaRepository repository,
            OfertaMapper mapper
    ) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Oferta guardar(Oferta oferta) {

        OfertaJpaEntity entity = mapper.toEntity(oferta);

        OfertaJpaEntity guardada = repository.save(entity);

        return mapper.toDomain(guardada);
    }

    @Override
    public Optional<Oferta> buscarPorId(Long id) {

        return repository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public List<Oferta> buscarTodas() {

        return repository.findAll()
                .stream()
                .map(mapper::toDomain)
                .toList();
    }
}

