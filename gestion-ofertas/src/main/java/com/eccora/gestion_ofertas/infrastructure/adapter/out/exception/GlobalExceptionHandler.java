
package com.eccora.gestion_ofertas.infrastructure.adapter.out.exception;

import com.eccora.gestion_ofertas.domain.exception.CuposInvalidosException;
import com.eccora.gestion_ofertas.domain.exception.EstadoOfertaInvalidoException;
import com.eccora.gestion_ofertas.domain.exception.OfertaNoEditableException;
import com.eccora.gestion_ofertas.domain.exception.OfertaNoEncontradaException;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(OfertaNoEncontradaException.class)
    public ResponseEntity<Map<String, Object>> manejarOfertaNoEncontrada(
            OfertaNoEncontradaException ex,
            HttpServletRequest request
    ) {
        return construirRespuesta(
                HttpStatus.NOT_FOUND,
                "OFERTA_NO_ENCONTRADA",
                ex.getMessage(),
                request
        );
    }

    @ExceptionHandler(OfertaNoEditableException.class)
    public ResponseEntity<Map<String, Object>> manejarOfertaNoEditable(
            OfertaNoEditableException ex,
            HttpServletRequest request
    ) {
        return construirRespuesta(
                HttpStatus.CONFLICT,
                "OFERTA_NO_EDITABLE",
                ex.getMessage(),
                request
        );
    }

    @ExceptionHandler(EstadoOfertaInvalidoException.class)
    public ResponseEntity<Map<String, Object>> manejarEstadoInvalido(
            EstadoOfertaInvalidoException ex,
            HttpServletRequest request
    ) {
        return construirRespuesta(
                HttpStatus.CONFLICT,
                "ESTADO_OFERTA_INVALIDO",
                ex.getMessage(),
                request
        );
    }

    @ExceptionHandler(CuposInvalidosException.class)
    public ResponseEntity<Map<String, Object>> manejarCuposInvalidos(
            CuposInvalidosException ex,
            HttpServletRequest request
    ) {
        return construirRespuesta(
                HttpStatus.BAD_REQUEST,
                "CUPOS_INVALIDOS",
                ex.getMessage(),
                request
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> manejarValidacion(
            MethodArgumentNotValidException ex,
            HttpServletRequest request
    ) {
        Map<String, String> errores = new LinkedHashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error ->
                errores.putIfAbsent(
                        error.getField(),
                        error.getDefaultMessage()
                )
        );

        Map<String, Object> respuesta = crearCuerpo(
                HttpStatus.BAD_REQUEST,
                "ERROR_VALIDACION",
                "Los datos enviados no son válidos",
                request
        );

        respuesta.put("errores", errores);

        return ResponseEntity.badRequest().body(respuesta);
    }

    private ResponseEntity<Map<String, Object>> construirRespuesta(
            HttpStatus status,
            String codigo,
            String mensaje,
            HttpServletRequest request
    ) {
        return ResponseEntity.status(status).body(
                crearCuerpo(status, codigo, mensaje, request)
        );
    }

    private Map<String, Object> crearCuerpo(
            HttpStatus status,
            String codigo,
            String mensaje,
            HttpServletRequest request
    ) {
        Map<String, Object> respuesta = new LinkedHashMap<>();

        respuesta.put("timestamp", Instant.now());
        respuesta.put("status", status.value());
        respuesta.put("code", codigo);
        respuesta.put("message", mensaje);
        respuesta.put("path", request.getRequestURI());

        return respuesta;
    }
}

