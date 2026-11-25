package com.eccora.gestion_ofertas.domain.entity;

import com.eccora.gestion_ofertas.domain.exception.CuposInvalidosException;
import com.eccora.gestion_ofertas.domain.exception.EstadoOfertaInvalidoException;
import com.eccora.gestion_ofertas.domain.exception.OfertaNoEditableException;
import com.eccora.gestion_ofertas.domain.exception.OfertaNoEncontradaException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OfertaTest {

    private static final String TITULO_VALIDO = "Desarrollador Java Backend";
    private static final String DESCRIPCION_VALIDA = "Desarrollo de microservicios con arquitectura hexagonal";
    private static final BigDecimal SALARIO_VALIDO = new BigDecimal("5500000.00");
    private static final Integer VACANTES_VALIDAS = 3;

    private Oferta crearOfertaBorrador() {
        return Oferta.crear(TITULO_VALIDO, DESCRIPCION_VALIDA, SALARIO_VALIDO, VACANTES_VALIDAS);
    }

    private Oferta crearOfertaPendienteRevision() {
        Oferta oferta = crearOfertaBorrador();
        oferta.enviarARevision();
        return oferta;
    }

    private Oferta crearOfertaAprobada() {
        Oferta oferta = crearOfertaPendienteRevision();
        oferta.aprobar();
        return oferta;
    }

    private Oferta crearOfertaRechazada() {
        Oferta oferta = crearOfertaPendienteRevision();
        oferta.rechazar("Falta detallar los requisitos técnicos");
        return oferta;
    }

    private Oferta crearOfertaPublicada() {
        Oferta oferta = crearOfertaAprobada();
        oferta.publicar();
        return oferta;
    }

    private Oferta crearOfertaCerrada() {
        Oferta oferta = crearOfertaPublicada();
        oferta.cerrar();
        return oferta;
    }

    @Nested
    @DisplayName("Creación y validación de invariantes")
    class CreacionTests {

        @Test
        @DisplayName("Debe crear oferta válida en estado BORRADOR")
        void debeCrearOfertaEnBorrador() {
            Oferta oferta = Oferta.crear(
                    "  " + TITULO_VALIDO + "  ",
                    "  " + DESCRIPCION_VALIDA + "  ",
                    SALARIO_VALIDO,
                    VACANTES_VALIDAS
            );

            assertNull(oferta.getId());
            assertEquals(TITULO_VALIDO, oferta.getTitulo());
            assertEquals(DESCRIPCION_VALIDA, oferta.getDescripcion());
            assertEquals(0, SALARIO_VALIDO.compareTo(oferta.getSalario()));
            assertEquals(VACANTES_VALIDAS, oferta.getVacantes());
            assertEquals(EstadoOferta.BORRADOR, oferta.getEstado());
            assertNull(oferta.getMotivoRechazo());
            assertNotNull(oferta.getFechaCreacion());
            assertNotNull(oferta.getFechaActualizacion());
            assertNull(oferta.getFechaPublicacion());
            assertNull(oferta.getFechaCierre());
            assertTrue(oferta.esEditable());
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {"   ", "\t\n"})
        @DisplayName("Debe fallar si el título es nulo o vacío")
        void debeFallarConTituloInvalido(String tituloInvalido) {
            assertThrows(IllegalArgumentException.class, () ->
                    Oferta.crear(tituloInvalido, DESCRIPCION_VALIDA, SALARIO_VALIDO, VACANTES_VALIDAS)
            );
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {"   ", "\t\n"})
        @DisplayName("Debe fallar si la descripción es nula o vacía")
        void debeFallarConDescripcionInvalida(String descripcionInvalida) {
            assertThrows(IllegalArgumentException.class, () ->
                    Oferta.crear(TITULO_VALIDO, descripcionInvalida, SALARIO_VALIDO, VACANTES_VALIDAS)
            );
        }

        @Test
        @DisplayName("Debe fallar si el salario es nulo, cero o negativo")
        void debeFallarConSalarioInvalido() {
            assertThrows(IllegalArgumentException.class, () ->
                    Oferta.crear(TITULO_VALIDO, DESCRIPCION_VALIDA, null, VACANTES_VALIDAS)
            );
            assertThrows(IllegalArgumentException.class, () ->
                    Oferta.crear(TITULO_VALIDO, DESCRIPCION_VALIDA, BigDecimal.ZERO, VACANTES_VALIDAS)
            );
            assertThrows(IllegalArgumentException.class, () ->
                    Oferta.crear(TITULO_VALIDO, DESCRIPCION_VALIDA, new BigDecimal("-100.50"), VACANTES_VALIDAS)
            );
        }

        @Test
        @DisplayName("Debe lanzar CuposInvalidosException si vacantes es nulo, cero o negativo")
        void debeFallarConVacantesInvalidas() {
            assertThrows(CuposInvalidosException.class, () ->
                    Oferta.crear(TITULO_VALIDO, DESCRIPCION_VALIDA, SALARIO_VALIDO, null)
            );
            assertThrows(CuposInvalidosException.class, () ->
                    Oferta.crear(TITULO_VALIDO, DESCRIPCION_VALIDA, SALARIO_VALIDO, 0)
            );
            assertThrows(CuposInvalidosException.class, () ->
                    Oferta.crear(TITULO_VALIDO, DESCRIPCION_VALIDA, SALARIO_VALIDO, -2)
            );
        }

        @Test
        @DisplayName("Debe validar estado y motivoRechazo al reconstituir")
        void debeValidarAlReconstituir() {
            LocalDateTime ahora = LocalDateTime.now();

            assertThrows(EstadoOfertaInvalidoException.class, () ->
                    Oferta.reconstituir(1L, TITULO_VALIDO, DESCRIPCION_VALIDA, SALARIO_VALIDO,
                            VACANTES_VALIDAS, null, null, ahora, ahora, null, null)
            );

            assertThrows(IllegalArgumentException.class, () ->
                    Oferta.reconstituir(1L, TITULO_VALIDO, DESCRIPCION_VALIDA, SALARIO_VALIDO,
                            VACANTES_VALIDAS, EstadoOferta.RECHAZADA, "  ", ahora, ahora, null, null)
            );

            Oferta reconstituida = Oferta.reconstituir(
                    10L, TITULO_VALIDO, DESCRIPCION_VALIDA, SALARIO_VALIDO,
                    VACANTES_VALIDAS, EstadoOferta.APROBADA, null, ahora, ahora, null, null
            );
            assertEquals(10L, reconstituida.getId());
            assertEquals(EstadoOferta.APROBADA, reconstituida.getEstado());
        }
    }

    @Nested
    @DisplayName("Reglas de edición")
    class EdicionTests {

        @Test
        @DisplayName("Debe permitir editar en estado BORRADOR")
        void debePermitirEditarEnBorrador() {
            Oferta oferta = crearOfertaBorrador();
            BigDecimal nuevoSalario = new BigDecimal("6200000.50");

            oferta.editar("Arquitecto Java", "Diseño de sistemas distribuidos", nuevoSalario, 2);

            assertEquals("Arquitecto Java", oferta.getTitulo());
            assertEquals("Diseño de sistemas distribuidos", oferta.getDescripcion());
            assertEquals(0, nuevoSalario.compareTo(oferta.getSalario()));
            assertEquals(2, oferta.getVacantes());
            assertEquals(EstadoOferta.BORRADOR, oferta.getEstado());
        }

        @Test
        @DisplayName("Debe permitir editar en estado RECHAZADA")
        void debePermitirEditarEnRechazada() {
            Oferta oferta = crearOfertaRechazada();

            oferta.editar("Backend Java Senior", "Incluye Spring y arquitectura hexagonal",
                    new BigDecimal("7000000"), 4);

            assertEquals("Backend Java Senior", oferta.getTitulo());
            assertEquals(4, oferta.getVacantes());
            assertEquals(EstadoOferta.RECHAZADA, oferta.getEstado());
            assertTrue(oferta.esEditable());
        }

        @Test
        @DisplayName("No debe modificar la oferta si fallan las validaciones al editar")
        void noDebeMutarSiFallaValidacionAlEditar() {
            Oferta oferta = crearOfertaBorrador();

            assertThrows(CuposInvalidosException.class, () ->
                    oferta.editar("Nuevo título", "Nueva descripción", new BigDecimal("6000000"), 0)
            );

            assertEquals(TITULO_VALIDO, oferta.getTitulo());
            assertEquals(VACANTES_VALIDAS, oferta.getVacantes());
        }

        @Test
        @DisplayName("Debe lanzar OfertaNoEditableException en estados no editables")
        void debeFallarEdicionEnEstadosNoEditables() {
            Oferta pendiente = crearOfertaPendienteRevision();
            Oferta aprobada = crearOfertaAprobada();
            Oferta publicada = crearOfertaPublicada();
            Oferta cerrada = crearOfertaCerrada();

            assertFalse(pendiente.esEditable());
            assertFalse(aprobada.esEditable());
            assertFalse(publicada.esEditable());
            assertFalse(cerrada.esEditable());

            assertThrows(OfertaNoEditableException.class, () ->
                    pendiente.editar("Nuevo", "Desc", SALARIO_VALIDO, 1));
            assertThrows(OfertaNoEditableException.class, () ->
                    aprobada.editar("Nuevo", "Desc", SALARIO_VALIDO, 1));
            assertThrows(OfertaNoEditableException.class, () ->
                    publicada.editar("Nuevo", "Desc", SALARIO_VALIDO, 1));
            assertThrows(OfertaNoEditableException.class, () ->
                    cerrada.editar("Nuevo", "Desc", SALARIO_VALIDO, 1));
        }
    }

    @Nested
    @DisplayName("Transiciones de estado válidas")
    class TransicionesValidasTests {

        @Test
        @DisplayName("Flujo completo feliz: BORRADOR -> PENDIENTE_REVISION -> APROBADA -> PUBLICADA -> CERRADA")
        void flujoCompletoExitoso() {
            Oferta oferta = crearOfertaBorrador();
            assertEquals(EstadoOferta.BORRADOR, oferta.getEstado());

            oferta.enviarARevision();
            assertEquals(EstadoOferta.PENDIENTE_REVISION, oferta.getEstado());

            oferta.aprobar();
            assertEquals(EstadoOferta.APROBADA, oferta.getEstado());
            assertNull(oferta.getMotivoRechazo());

            oferta.publicar();
            assertEquals(EstadoOferta.PUBLICADA, oferta.getEstado());
            assertNotNull(oferta.getFechaPublicacion());

            oferta.cerrar();
            assertEquals(EstadoOferta.CERRADA, oferta.getEstado());
            assertNotNull(oferta.getFechaCierre());
        }

        @Test
        @DisplayName("Flujo de rechazo, edición y reenvío a revisión limpia motivoRechazo")
        void flujoRechazoYReenvio() {
            Oferta oferta = crearOfertaPendienteRevision();

            oferta.rechazar("  El rango salarial no coincide con la banda  ");
            assertEquals(EstadoOferta.RECHAZADA, oferta.getEstado());
            assertEquals("El rango salarial no coincide con la banda", oferta.getMotivoRechazo());

            oferta.editar(TITULO_VALIDO, DESCRIPCION_VALIDA, new BigDecimal("6000000"), 2);
            oferta.enviarARevision();

            assertEquals(EstadoOferta.PENDIENTE_REVISION, oferta.getEstado());
            assertNull(oferta.getMotivoRechazo());
        }
    }

    @Nested
    @DisplayName("Transiciones de estado inválidas")
    class TransicionesInvalidasTests {

        @Test
        @DisplayName("No debe permitir enviar a revisión desde estados distintos a BORRADOR o RECHAZADA")
        void noDebeEnviarARevisionDesdeEstadosInvalidos() {
            assertThrows(EstadoOfertaInvalidoException.class, () -> crearOfertaPendienteRevision().enviarARevision());
            assertThrows(EstadoOfertaInvalidoException.class, () -> crearOfertaAprobada().enviarARevision());
            assertThrows(EstadoOfertaInvalidoException.class, () -> crearOfertaPublicada().enviarARevision());
            assertThrows(EstadoOfertaInvalidoException.class, () -> crearOfertaCerrada().enviarARevision());
        }

        @Test
        @DisplayName("No debe permitir aprobar desde estados distintos a PENDIENTE_REVISION")
        void noDebeAprobarDesdeEstadosInvalidos() {
            assertThrows(EstadoOfertaInvalidoException.class, () -> crearOfertaBorrador().aprobar());
            assertThrows(EstadoOfertaInvalidoException.class, () -> crearOfertaAprobada().aprobar());
            assertThrows(EstadoOfertaInvalidoException.class, () -> crearOfertaRechazada().aprobar());
            assertThrows(EstadoOfertaInvalidoException.class, () -> crearOfertaPublicada().aprobar());
            assertThrows(EstadoOfertaInvalidoException.class, () -> crearOfertaCerrada().aprobar());
        }

        @Test
        @DisplayName("No debe permitir rechazar sin motivo o desde estados distintos a PENDIENTE_REVISION")
        void noDebeRechazarIncorrectamente() {
            Oferta pendiente = crearOfertaPendienteRevision();
            assertThrows(IllegalArgumentException.class, () -> pendiente.rechazar(null));
            assertThrows(IllegalArgumentException.class, () -> pendiente.rechazar("   "));
            assertEquals(EstadoOferta.PENDIENTE_REVISION, pendiente.getEstado());

            assertThrows(EstadoOfertaInvalidoException.class, () -> crearOfertaBorrador().rechazar("Motivo"));
            assertThrows(EstadoOfertaInvalidoException.class, () -> crearOfertaAprobada().rechazar("Motivo"));
            assertThrows(EstadoOfertaInvalidoException.class, () -> crearOfertaRechazada().rechazar("Motivo"));
            assertThrows(EstadoOfertaInvalidoException.class, () -> crearOfertaPublicada().rechazar("Motivo"));
            assertThrows(EstadoOfertaInvalidoException.class, () -> crearOfertaCerrada().rechazar("Motivo"));
        }

        @Test
        @DisplayName("No debe permitir publicar desde estados distintos a APROBADA")
        void noDebePublicarDesdeEstadosInvalidos() {
            assertThrows(EstadoOfertaInvalidoException.class, () -> crearOfertaBorrador().publicar());
            assertThrows(EstadoOfertaInvalidoException.class, () -> crearOfertaPendienteRevision().publicar());
            assertThrows(EstadoOfertaInvalidoException.class, () -> crearOfertaRechazada().publicar());
            assertThrows(EstadoOfertaInvalidoException.class, () -> crearOfertaPublicada().publicar());
            assertThrows(EstadoOfertaInvalidoException.class, () -> crearOfertaCerrada().publicar());
        }

        @Test
        @DisplayName("No debe permitir cerrar desde estados distintos a PUBLICADA")
        void noDebeCerrarDesdeEstadosInvalidos() {
            assertThrows(EstadoOfertaInvalidoException.class, () -> crearOfertaBorrador().cerrar());
            assertThrows(EstadoOfertaInvalidoException.class, () -> crearOfertaPendienteRevision().cerrar());
            assertThrows(EstadoOfertaInvalidoException.class, () -> crearOfertaAprobada().cerrar());
            assertThrows(EstadoOfertaInvalidoException.class, () -> crearOfertaRechazada().cerrar());
            assertThrows(EstadoOfertaInvalidoException.class, () -> crearOfertaCerrada().cerrar());
        }
    }

    @Nested
    @DisplayName("Excepciones de dominio e identidad")
    class ExcepcionesEIdentidadTests {

        @Test
        @DisplayName("Debe construir mensajes descriptivos en las excepciones de dominio")
        void debeConstruirExcepcionesDeDominio() {
            OfertaNoEncontradaException noEncontrada = new OfertaNoEncontradaException(42L);
            assertTrue(noEncontrada.getMessage().contains("42"));

            EstadoOfertaInvalidoException estadoInvalido = new EstadoOfertaInvalidoException("aprobar", EstadoOferta.BORRADOR);
            assertTrue(estadoInvalido.getMessage().contains("BORRADOR"));

            OfertaNoEditableException noEditable = new OfertaNoEditableException(EstadoOferta.PUBLICADA);
            assertTrue(noEditable.getMessage().contains("PUBLICADA"));

            CuposInvalidosException cuposInvalidos = new CuposInvalidosException(0);
            assertTrue(cuposInvalidos.getMessage().contains("0"));
        }

        @Test
        @DisplayName("Debe comparar identidad por id cuando existe")
        void debeCompararIdentidadPorId() {
            LocalDateTime ahora = LocalDateTime.now();
            Oferta oferta1 = Oferta.reconstituir(1L, TITULO_VALIDO, DESCRIPCION_VALIDA, SALARIO_VALIDO,
                    1, EstadoOferta.BORRADOR, null, ahora, ahora, null, null);
            Oferta oferta1Copia = Oferta.reconstituir(1L, "Otro título", DESCRIPCION_VALIDA, SALARIO_VALIDO,
                    2, EstadoOferta.BORRADOR, null, ahora, ahora, null, null);
            Oferta oferta2 = Oferta.reconstituir(2L, TITULO_VALIDO, DESCRIPCION_VALIDA, SALARIO_VALIDO,
                    1, EstadoOferta.BORRADOR, null, ahora, ahora, null, null);
            Oferta sinId1 = crearOfertaBorrador();
            Oferta sinId2 = crearOfertaBorrador();

            assertEquals(oferta1, oferta1Copia);
            assertEquals(oferta1.hashCode(), oferta1Copia.hashCode());
            assertNotEquals(oferta1, oferta2);
            assertNotEquals(sinId1, sinId2);
        }
    }
}
