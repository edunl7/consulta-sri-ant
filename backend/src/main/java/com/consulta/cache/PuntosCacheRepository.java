package com.consulta.cache;

import com.consulta.model.PuntosLicencia;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public class PuntosCacheRepository {

    private static final Logger log = LoggerFactory.getLogger(PuntosCacheRepository.class);
    private static final String KEY_PREFIX = "puntos:";

    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    @Value("${cache.puntos.soft-ttl-hours:24}")
    private int softTtlHours;

    @Value("${cache.puntos.hard-ttl-hours:168}")
    private int hardTtlHours;

    public PuntosCacheRepository(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    private String buildKey(String cedula, String placa) {
        return KEY_PREFIX + cedula + ":" + (placa != null ? placa : "SINPLACA");
    }

    public void guardar(PuntosLicencia puntos) {
        try {
            String key = buildKey(puntos.getCedula(), puntos.getPlaca());
            redisTemplate.opsForValue().set(key, puntos, Duration.ofHours(hardTtlHours));
            log.info("Resultado de ANT guardado en caché. Key: {}, TTL: {}h", key, hardTtlHours);
        } catch (Exception e) {
            log.error("Error guardando en caché: {}", e.getMessage());
        }
    }

    public Optional<PuntosLicencia> buscar(String cedula, String placa) {
        try {
            String key = buildKey(cedula, placa);
            Object raw = redisTemplate.opsForValue().get(key);

            if (raw == null) {
                log.info("Caché MISS para key: {}", key);
                return Optional.empty();
            }

            PuntosLicencia puntos = objectMapper.convertValue(raw, PuntosLicencia.class);
            puntos.setDesdeCachE(true);
            log.info("Caché HIT para key: {}. Fecha consulta: {}", key, puntos.getFechaConsulta());
            return Optional.of(puntos);

        } catch (Exception e) {
            log.error("Error leyendo caché: {}", e.getMessage());
            return Optional.empty();
        }
    }

    public boolean esFresco(PuntosLicencia puntos) {
        if (puntos.getFechaConsulta() == null) return false;
        return puntos.getFechaConsulta()
                .plusHours(softTtlHours)
                .isAfter(LocalDateTime.now());
    }
}
