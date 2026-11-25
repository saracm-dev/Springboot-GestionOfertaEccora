
package com.eccora.gestion_ofertas.infrastructure.adapter.in;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Digits;

import java.math.BigDecimal;

public record EditarOfertaRequest(

        @NotBlank(message = "El título es obligatorio")
        @Size(max = 255, message = "El título no puede superar los 255 caracteres")
        String titulo,

        @NotBlank(message = "La descripción es obligatoria")
        @Size(max = 5000, message = "La descripción no puede superar los 5000 caracteres")
        String descripcion,

        @NotNull(message = "El salario es obligatorio")
        @Positive(message = "El salario debe ser mayor que cero")
        @Digits(integer = 17, fraction = 2)
        BigDecimal salario,

        @NotNull(message = "Las vacantes son obligatorias")
        @Positive(message = "Las vacantes deben ser mayores que cero")
        Integer vacantes

) {
}

