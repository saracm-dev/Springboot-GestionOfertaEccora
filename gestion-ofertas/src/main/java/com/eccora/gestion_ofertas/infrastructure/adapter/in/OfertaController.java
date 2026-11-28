
package com.eccora.gestion_ofertas.infrastructure.adapter.in;

import com.eccora.gestion_ofertas.application.port.in.*;
import com.eccora.gestion_ofertas.application.port.in.RegistrarOfertaUseCase.RegistrarOfertaCommand;
import com.eccora.gestion_ofertas.application.port.in.EditarOfertaUseCase.EditarOfertaCommand;
import com.eccora.gestion_ofertas.domain.entity.Oferta;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/ofertas")
public class OfertaController {

    private final RegistrarOfertaUseCase registrarOfertaUseCase;
    private final ConsultarOfertaUseCase consultarOfertaUseCase;
    private final EditarOfertaUseCase editarOfertaUseCase;
    private final EnviarARevisionOfertaUseCase enviarARevisionOfertaUseCase;
    private final AprobarOfertaUseCase aprobarOfertaUseCase;
    private final RechazarOfertaUseCase rechazarOfertaUseCase;
    private final PublicarOfertaUseCase publicarOfertaUseCase;
    private final CerrarOfertaUseCase cerrarOfertaUseCase;

    public OfertaController(
            RegistrarOfertaUseCase registrarOfertaUseCase,
            ConsultarOfertaUseCase consultarOfertaUseCase,
            EditarOfertaUseCase editarOfertaUseCase,
            EnviarARevisionOfertaUseCase enviarARevisionOfertaUseCase,
            AprobarOfertaUseCase aprobarOfertaUseCase,
            RechazarOfertaUseCase rechazarOfertaUseCase,
            PublicarOfertaUseCase publicarOfertaUseCase,
            CerrarOfertaUseCase cerrarOfertaUseCase
    ) {
        this.registrarOfertaUseCase = registrarOfertaUseCase;
        this.consultarOfertaUseCase = consultarOfertaUseCase;
        this.editarOfertaUseCase = editarOfertaUseCase;
        this.enviarARevisionOfertaUseCase = enviarARevisionOfertaUseCase;
        this.aprobarOfertaUseCase = aprobarOfertaUseCase;
        this.rechazarOfertaUseCase = rechazarOfertaUseCase;
        this.publicarOfertaUseCase = publicarOfertaUseCase;
        this.cerrarOfertaUseCase = cerrarOfertaUseCase;
    }

    @PostMapping
    public ResponseEntity<OfertaDetalleResponse> registrar(
            @Valid @RequestBody CrearOfertaRequest request
    ) {

        RegistrarOfertaCommand command = new RegistrarOfertaCommand(
                request.titulo(),
                request.descripcion(),
                request.salario(),
                request.vacantes()
        );

        Oferta oferta = registrarOfertaUseCase.registrar(command);

        return ResponseEntity
                .created(URI.create("/api/ofertas/" + oferta.getId()))
                .body(OfertaDetalleResponse.fromDomain(oferta));
    }

    @GetMapping
    public ResponseEntity<List<OfertaResponse>> consultarTodas() {

        List<OfertaResponse> ofertas = consultarOfertaUseCase
                .consultarTodas()
                .stream()
                .map(OfertaResponse::fromDomain)
                .toList();

        return ResponseEntity.ok(ofertas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OfertaDetalleResponse> consultarPorId(
            @PathVariable Long id
    ) {

        Oferta oferta = consultarOfertaUseCase.consultarPorId(id);

        return ResponseEntity.ok(
                OfertaDetalleResponse.fromDomain(oferta)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<OfertaDetalleResponse> editar(
            @PathVariable Long id,
            @Valid @RequestBody EditarOfertaRequest request
    ) {

        EditarOfertaCommand command = new EditarOfertaCommand(
                request.titulo(),
                request.descripcion(),
                request.salario(),
                request.vacantes()
        );

        Oferta oferta = editarOfertaUseCase.editar(id, command);

        return ResponseEntity.ok(
                OfertaDetalleResponse.fromDomain(oferta)
        );
    }

    @PatchMapping("/{id}/enviar-revision")
    public ResponseEntity<OfertaDetalleResponse> enviarARevision(
            @PathVariable Long id
    ) {

        Oferta oferta = enviarARevisionOfertaUseCase.enviarARevision(id);

        return ResponseEntity.ok(
                OfertaDetalleResponse.fromDomain(oferta)
        );
    }

    @PatchMapping("/{id}/aprobar")
    public ResponseEntity<OfertaDetalleResponse> aprobar(
            @PathVariable Long id
    ) {

        Oferta oferta = aprobarOfertaUseCase.aprobar(id);

        return ResponseEntity.ok(
                OfertaDetalleResponse.fromDomain(oferta)
        );
    }

    @PatchMapping("/{id}/rechazar")
    public ResponseEntity<OfertaDetalleResponse> rechazar(
            @PathVariable Long id,
            @Valid @RequestBody RechazarOfertaRequest request
    ) {

        Oferta oferta = rechazarOfertaUseCase.rechazar(
                id,
                request.motivoRechazo()
        );

        return ResponseEntity.ok(
                OfertaDetalleResponse.fromDomain(oferta)
        );
    }

    @PatchMapping("/{id}/publicar")
    public ResponseEntity<OfertaDetalleResponse> publicar(
            @PathVariable Long id
    ) {

        Oferta oferta = publicarOfertaUseCase.publicar(id);

        return ResponseEntity.ok(
                OfertaDetalleResponse.fromDomain(oferta)
        );
    }

    @PatchMapping("/{id}/cerrar")
    public ResponseEntity<OfertaDetalleResponse> cerrar(
            @PathVariable Long id
    ) {

        Oferta oferta = cerrarOfertaUseCase.cerrar(id);

        return ResponseEntity.ok(
                OfertaDetalleResponse.fromDomain(oferta)
        );
    }
}

