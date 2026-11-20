package com.consulta.service;

import com.consulta.cache.PuntosCacheRepository;
import com.consulta.client.AntClient;
import com.consulta.model.PuntosLicencia;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PuntosLicenciaService {

    private static final Logger log = LoggerFactory.getLogger(PuntosLicenciaService.class);
    private final AntClient antClient;
    private final PuntosCacheRepository cacheRepository;

    public PuntosLicenciaService(AntClient antClient, PuntosCacheRepository cacheRepository) {
        this.antClient = antClient;
        this.cacheRepository = cacheRepository;
    }

    public PuntosLicencia consultarPuntos(String cedula, String placa) {
        Optional<PuntosLicencia> cached = cacheRepository.buscar(cedula, placa);

        if (cached.isPresent() && cacheRepository.esFresco(cached.get())) {
            log.info("Dato FRESCO en caché para cédula: {}. No se llama a la ANT.", cedula);
            return cached.get();
        }

        try {
            PuntosLicencia resultado = llamarAntConCircuitBreaker(cedula, placa);
            cacheRepository.guardar(resultado);
            log.info("Dato obtenido de la ANT y guardado en caché para cédula: {}", cedula);
            return resultado;

        } catch (Exception e) {
            log.warn("ANT falló para cédula {}: {}. Buscando fallback en caché...", cedula, e.getMessage());

            if (cached.isPresent()) {
                PuntosLicencia fallback = cached.get();
                fallback.setDesdeCachE(true);
                log.info("Devolviendo dato VIEJO de caché. Fecha consulta: {}", fallback.getFechaConsulta());
                return fallback;
            }

            log.error("Sin caché disponible para cédula: {}. ANT caída.", cedula);
            throw new RuntimeException(
                    "El servicio de la ANT no está disponible y no hay datos en caché. "
                    + "Intente más tarde.");
        }
    }

    @CircuitBreaker(name = "antService", fallbackMethod = "antFallback")
    public PuntosLicencia llamarAntConCircuitBreaker(String cedula, String placa) {
        return antClient.consultarPuntos(cedula, placa);
    }

    public PuntosLicencia antFallback(String cedula, String placa, Throwable t) {
        log.warn("Circuit Breaker activado para ANT. Razón: {}", t.getMessage());
        throw new RuntimeException("Servicio ANT no disponible (circuit breaker abierto): " + t.getMessage(), t);
    }
}
