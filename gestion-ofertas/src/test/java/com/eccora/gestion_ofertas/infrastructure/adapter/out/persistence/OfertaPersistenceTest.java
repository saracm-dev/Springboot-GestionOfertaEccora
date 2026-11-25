
package com.eccora.gestion_ofertas.infrastructure.adapter.out.persistence;

import com.eccora.gestion_ofertas.domain.entity.Oferta;
import com.eccora.gestion_ofertas.domain.entity.EstadoOferta;

import jakarta.persistence.EntityManager;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class OfertaPersistenceTest {

    @Autowired
    private OfertaJpaRepository repository;

    @Autowired
    private OfertaMapper mapper;

    @Autowired
    private EntityManager entityManager;

    private Oferta crearOfertaPrueba() {
        return Oferta.crear(
                "Desarrollador Java",
                "Desarrollo de microservicios con Spring Boot",
                new BigDecimal("3500000.00"),
                3
        );
    }

    private Oferta guardar(Oferta oferta) {

        OfertaJpaEntity entity = mapper.toEntity(oferta);

        OfertaJpaEntity guardada = repository.saveAndFlush(entity);

        Long id = guardada.getId();

        entityManager.clear();

        return mapper.toDomain(
                repository.findById(id).orElseThrow()
        );
    }

    @Test
    void debeGuardarOferta() {

        Oferta oferta = crearOfertaPrueba();

        assertNull(oferta.getId());

        Oferta guardada = guardar(oferta);

        assertNotNull(guardada.getId());
        assertEquals(oferta.getTitulo(), guardada.getTitulo());
        assertEquals(oferta.getDescripcion(), guardada.getDescripcion());
        assertEquals(0, oferta.getSalario().compareTo(guardada.getSalario()));
        assertEquals(oferta.getVacantes(), guardada.getVacantes());
        assertEquals(EstadoOferta.BORRADOR, guardada.getEstado());
        assertNotNull(guardada.getFechaCreacion());
        assertNotNull(guardada.getFechaActualizacion());
        assertNull(guardada.getFechaPublicacion());
        assertNull(guardada.getFechaCierre());
    }

    @Test
    void debeBuscarOfertaPorId() {

        Oferta guardada = guardar(crearOfertaPrueba());

        OfertaJpaEntity encontrada = repository
                .findById(guardada.getId())
                .orElseThrow();

        Oferta oferta = mapper.toDomain(encontrada);

        assertEquals(guardada.getId(), oferta.getId());
        assertEquals(guardada.getTitulo(), oferta.getTitulo());
        assertEquals(guardada.getEstado(), oferta.getEstado());
    }

    @Test
    void debeActualizarOferta() {

        Oferta oferta = guardar(crearOfertaPrueba());

        Long idOriginal = oferta.getId();

        oferta.editar(
                "Desarrollador Java Senior",
                "Desarrollo de servicios empresariales",
                new BigDecimal("6500000.00"),
                5
        );

        Oferta actualizada = guardar(oferta);

        assertEquals(idOriginal, actualizada.getId());
        assertEquals("Desarrollador Java Senior", actualizada.getTitulo());
        assertEquals("Desarrollo de servicios empresariales",
                actualizada.getDescripcion());
        assertEquals(0, new BigDecimal("6500000.00")
                .compareTo(actualizada.getSalario()));
        assertEquals(5, actualizada.getVacantes());
        assertEquals(EstadoOferta.BORRADOR, actualizada.getEstado());
        assertEquals(oferta.getFechaCreacion(),
                actualizada.getFechaCreacion());
    }

    @Test
    void debePersistirEstadoPendienteRevision() {

        Oferta oferta = guardar(crearOfertaPrueba());

        oferta.enviarARevision();

        Oferta actualizada = guardar(oferta);

        assertEquals(
                EstadoOferta.PENDIENTE_REVISION,
                actualizada.getEstado()
        );
    }

    @Test
    void debePersistirOfertaAprobada() {

        Oferta oferta = guardar(crearOfertaPrueba());

        oferta.enviarARevision();
        oferta.aprobar();

        Oferta actualizada = guardar(oferta);

        assertEquals(
                EstadoOferta.APROBADA,
                actualizada.getEstado()
        );
    }

    @Test
    void debePersistirOfertaRechazada() {

        Oferta oferta = guardar(crearOfertaPrueba());

        oferta.enviarARevision();
        oferta.rechazar("La descripción es insuficiente");

        Oferta actualizada = guardar(oferta);

        assertEquals(
                EstadoOferta.RECHAZADA,
                actualizada.getEstado()
        );

        assertEquals(
                "La descripción es insuficiente",
                actualizada.getMotivoRechazo()
        );
    }

    @Test
    void debePersistirOfertaPublicada() {

        Oferta oferta = guardar(crearOfertaPrueba());

        oferta.enviarARevision();
        oferta.aprobar();
        oferta.publicar();

        Oferta actualizada = guardar(oferta);

        assertEquals(
                EstadoOferta.PUBLICADA,
                actualizada.getEstado()
        );

        assertNotNull(actualizada.getFechaPublicacion());
    }

    @Test
    void debePersistirOfertaCerrada() {

        Oferta oferta = guardar(crearOfertaPrueba());

        oferta.enviarARevision();
        oferta.aprobar();
        oferta.publicar();
        oferta.cerrar();

        Oferta actualizada = guardar(oferta);

        assertEquals(
                EstadoOferta.CERRADA,
                actualizada.getEstado()
        );

        assertNotNull(actualizada.getFechaPublicacion());
        assertNotNull(actualizada.getFechaCierre());
    }
}

