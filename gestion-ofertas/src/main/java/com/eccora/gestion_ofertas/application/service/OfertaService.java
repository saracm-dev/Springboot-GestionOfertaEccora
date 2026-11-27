package com.eccora.gestion_ofertas.application.service;

import com.eccora.gestion_ofertas.application.port.in.AprobarOfertaUseCase;
import com.eccora.gestion_ofertas.application.port.in.CerrarOfertaUseCase;
import com.eccora.gestion_ofertas.application.port.in.ConsultarOfertaUseCase;
import com.eccora.gestion_ofertas.application.port.in.EditarOfertaUseCase;
import com.eccora.gestion_ofertas.application.port.in.EnviarARevisionOfertaUseCase;
import com.eccora.gestion_ofertas.application.port.in.PublicarOfertaUseCase;
import com.eccora.gestion_ofertas.application.port.in.RechazarOfertaUseCase;
import com.eccora.gestion_ofertas.application.port.in.RegistrarOfertaUseCase;
import com.eccora.gestion_ofertas.application.port.out.OfertaRepositoryPort;
import com.eccora.gestion_ofertas.domain.entity.Oferta;
import com.eccora.gestion_ofertas.domain.exception.OfertaNoEncontradaException;

import java.util.List;
import java.util.Objects;

/**
 * Implementa los casos de uso de Gestión de Ofertas.
 * Las reglas de estado y validación las aplica la entidad Oferta;
 * este servicio solo busca, delega en el dominio y guarda.
 * Sin anotaciones de Spring: se registra como bean en BeanConfiguration.
 */
public class OfertaService implements
        RegistrarOfertaUseCase,
        ConsultarOfertaUseCase,
        EditarOfertaUseCase,
        EnviarARevisionOfertaUseCase,
        AprobarOfertaUseCase,
        RechazarOfertaUseCase,
        PublicarOfertaUseCase,
        CerrarOfertaUseCase {

    private final OfertaRepositoryPort ofertaRepositoryPort;

    public OfertaService(OfertaRepositoryPort ofertaRepositoryPort) {
        this.ofertaRepositoryPort = Objects.requireNonNull(ofertaRepositoryPort);
    }

    @Override
    public Oferta registrar(RegistrarOfertaCommand command) {
        Oferta oferta = Oferta.crear(
                command.titulo(),
                command.descripcion(),
                command.salario(),
                command.vacantes()
        );
        return ofertaRepositoryPort.guardar(oferta);
    }

    @Override
    public Oferta consultarPorId(Long id) {
        return obtenerOferta(id);
    }

    @Override
    public List<Oferta> consultarTodas() {
        return ofertaRepositoryPort.buscarTodas();
    }

    @Override
    public Oferta editar(Long id, EditarOfertaCommand command) {
        Oferta oferta = obtenerOferta(id);
        oferta.editar(
                command.titulo(),
                command.descripcion(),
                command.salario(),
                command.vacantes()
        );
        return ofertaRepositoryPort.guardar(oferta);
    }

    @Override
    public Oferta enviarARevision(Long id) {
        Oferta oferta = obtenerOferta(id);
        oferta.enviarARevision();
        return ofertaRepositoryPort.guardar(oferta);
    }

    @Override
    public Oferta aprobar(Long id) {
        Oferta oferta = obtenerOferta(id);
        oferta.aprobar();
        return ofertaRepositoryPort.guardar(oferta);
    }

    @Override
    public Oferta rechazar(Long id, String motivoRechazo) {
        Oferta oferta = obtenerOferta(id);
        oferta.rechazar(motivoRechazo);
        return ofertaRepositoryPort.guardar(oferta);
    }

    @Override
    public Oferta publicar(Long id) {
        Oferta oferta = obtenerOferta(id);
        oferta.publicar();
        return ofertaRepositoryPort.guardar(oferta);
    }

    @Override
    public Oferta cerrar(Long id) {
        Oferta oferta = obtenerOferta(id);
        oferta.cerrar();
        return ofertaRepositoryPort.guardar(oferta);
    }

    private Oferta obtenerOferta(Long id) {
        return ofertaRepositoryPort.buscarPorId(id)
                .orElseThrow(() -> new OfertaNoEncontradaException(id));
    }
}
