package com.eccora.gestion_ofertas.infrastructure.config;

import com.eccora.gestion_ofertas.application.port.out.OfertaRepositoryPort;
import com.eccora.gestion_ofertas.application.service.OfertaService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {

    @Bean
    public OfertaService ofertaService(
            OfertaRepositoryPort ofertaRepositoryPort
    ) {
        return new OfertaService(ofertaRepositoryPort);
    }
}

