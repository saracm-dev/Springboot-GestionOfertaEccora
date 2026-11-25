
package com.eccora.gestion_ofertas.infrastructure.adapter.in;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RechazarOfertaRequest(

        @NotBlank(message = "El motivo de rechazo es obligatorio")
        @Size(max = 2000, message = "El motivo de rechazo no puede superar los 2000 caracteres")
        String motivoRechazo

) {
}

