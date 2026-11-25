package com.eccora.gestion_ofertas.infrastructure.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OfertaJpaRepository
        extends JpaRepository<OfertaJpaEntity, Long> {

}
