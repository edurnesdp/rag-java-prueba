package com.example.rag;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ActiveProfiles("mock")
@SpringBootTest
class RagApplicationTest {

    private static final Logger logger = LoggerFactory.getLogger(RagApplicationTest.class);

    @Test
    void contextLoads() {
        logger.info("Cargando el contexto de la aplicación para pruebas con el perfil 'mock'...");
        // Verifica que el contexto de la aplicación se cargue correctamente
    }
}