package com.eccora.gestion_ofertas.domain.entity;

import com.eccora.gestion_ofertas.domain.exception.CuposInvalidosException;
import com.eccora.gestion_ofertas.domain.exception.EstadoOfertaInvalidoException;
import com.eccora.gestion_ofertas.domain.exception.OfertaNoEditableException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

public class Oferta {

    private final Long id;
    private String titulo;
    private String descripcion;
    private BigDecimal salario;
    private Integer vacantes;
    private EstadoOferta estado;
    private String motivoRechazo;
    private final LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
    private LocalDateTime fechaPublicacion;
    private LocalDateTime fechaCierre;

    private Oferta(
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
        validarDatosObligatorios(titulo, descripcion, salario);
        validarVacantes(vacantes);
        if (estado == null) {
            throw new EstadoOfertaInvalidoException("El estado de la oferta es obligatorio");
        }
        if (estado == EstadoOferta.RECHAZADA) {
            validarMotivoRechazo(motivoRechazo);
        }

        this.id = id;
        this.titulo = titulo.trim();
        this.descripcion = descripcion.trim();
        this.salario = salario;
        this.vacantes = vacantes;
        this.estado = estado;
        this.motivoRechazo = motivoRechazo != null ? motivoRechazo.trim() : null;
        this.fechaCreacion = fechaCreacion != null ? fechaCreacion : LocalDateTime.now();
        this.fechaActualizacion = fechaActualizacion != null ? fechaActualizacion : this.fechaCreacion;
        this.fechaPublicacion = fechaPublicacion;
        this.fechaCierre = fechaCierre;
    }

    public static Oferta crear(
            String titulo,
            String descripcion,
            BigDecimal salario,
            Integer vacantes
    ) {
        LocalDateTime ahora = LocalDateTime.now();
        return new Oferta(
                null,
                titulo,
                descripcion,
                salario,
                vacantes,
                EstadoOferta.BORRADOR,
                null,
                ahora,
                ahora,
                null,
                null
        );
    }

    public static Oferta reconstituir(
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
        return new Oferta(
                id,
                titulo,
                descripcion,
                salario,
                vacantes,
                estado,
                motivoRechazo,
                fechaCreacion,
                fechaActualizacion,
                fechaPublicacion,
                fechaCierre
        );
    }

    public void editar(
            String nuevoTitulo,
            String nuevaDescripcion,
            BigDecimal nuevoSalario,
            Integer nuevasVacantes
    ) {
        if (!esEditable()) {
            throw new OfertaNoEditableException(this.estado);
        }
        validarDatosObligatorios(nuevoTitulo, nuevaDescripcion, nuevoSalario);
        validarVacantes(nuevasVacantes);

        this.titulo = nuevoTitulo.trim();
        this.descripcion = nuevaDescripcion.trim();
        this.salario = nuevoSalario;
        this.vacantes = nuevasVacantes;
        this.fechaActualizacion = LocalDateTime.now();
    }

    public void enviarARevision() {
        if (this.estado != EstadoOferta.BORRADOR && this.estado != EstadoOferta.RECHAZADA) {
            throw new EstadoOfertaInvalidoException(this.estado, EstadoOferta.PENDIENTE_REVISION);
        }
        validarDatosObligatorios(this.titulo, this.descripcion, this.salario);
        validarVacantes(this.vacantes);

        this.estado = EstadoOferta.PENDIENTE_REVISION;
        this.motivoRechazo = null;
        this.fechaActualizacion = LocalDateTime.now();
    }

    public void aprobar() {
        if (this.estado != EstadoOferta.PENDIENTE_REVISION) {
            throw new EstadoOfertaInvalidoException(this.estado, EstadoOferta.APROBADA);
        }

        this.estado = EstadoOferta.APROBADA;
        this.motivoRechazo = null;
        this.fechaActualizacion = LocalDateTime.now();
    }

    public void rechazar(String motivoRechazo) {
        if (this.estado != EstadoOferta.PENDIENTE_REVISION) {
            throw new EstadoOfertaInvalidoException(this.estado, EstadoOferta.RECHAZADA);
        }
        validarMotivoRechazo(motivoRechazo);

        this.estado = EstadoOferta.RECHAZADA;
        this.motivoRechazo = motivoRechazo.trim();
        this.fechaActualizacion = LocalDateTime.now();
    }

    public void publicar() {
        if (this.estado != EstadoOferta.APROBADA) {
            throw new EstadoOfertaInvalidoException(this.estado, EstadoOferta.PUBLICADA);
        }
        validarVacantes(this.vacantes);

        LocalDateTime ahora = LocalDateTime.now();
        this.estado = EstadoOferta.PUBLICADA;
        this.fechaPublicacion = ahora;
        this.fechaActualizacion = ahora;
    }

    public void cerrar() {
        if (this.estado != EstadoOferta.PUBLICADA) {
            throw new EstadoOfertaInvalidoException(this.estado, EstadoOferta.CERRADA);
        }

        LocalDateTime ahora = LocalDateTime.now();
        this.estado = EstadoOferta.CERRADA;
        this.fechaCierre = ahora;
        this.fechaActualizacion = ahora;
    }

    public boolean esEditable() {
        return this.estado == EstadoOferta.BORRADOR || this.estado == EstadoOferta.RECHAZADA;
    }

    private static void validarDatosObligatorios(String titulo, String descripcion, BigDecimal salario) {
        if (titulo == null || titulo.isBlank()) {
            throw new IllegalArgumentException("El título de la oferta es obligatorio");
        }
        if (descripcion == null || descripcion.isBlank()) {
            throw new IllegalArgumentException("La descripción de la oferta es obligatoria");
        }
        if (salario == null || salario.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El salario es obligatorio y debe ser mayor a cero");
        }
    }

    private static void validarVacantes(Integer vacantes) {
        if (vacantes == null || vacantes <= 0) {
            throw new CuposInvalidosException(vacantes);
        }
    }

    private static void validarMotivoRechazo(String motivoRechazo) {
        if (motivoRechazo == null || motivoRechazo.isBlank()) {
            throw new IllegalArgumentException("El motivo de rechazo es obligatorio");
        }
    }

    public Long getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public BigDecimal getSalario() {
        return salario;
    }

    public Integer getVacantes() {
        return vacantes;
    }

    public EstadoOferta getEstado() {
        return estado;
    }

    public String getMotivoRechazo() {
        return motivoRechazo;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public LocalDateTime getFechaActualizacion() {
        return fechaActualizacion;
    }

    public LocalDateTime getFechaPublicacion() {
        return fechaPublicacion;
    }

    public LocalDateTime getFechaCierre() {
        return fechaCierre;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Oferta otra)) {
            return false;
        }
        return id != null && Objects.equals(id, otra.id);
    }

    @Override
    public int hashCode() {
        return id != null ? Objects.hashCode(id) : System.identityHashCode(this);
    }
}
