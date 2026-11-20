# Consulta SRI - ANT · Backend

API REST en Java 17 + Spring Boot 3 que integra servicios del SRI y la ANT.

## Arquitectura

- **ContribuyenteService** → SRI Catastro (`existePorNumeroRuc`, `obtenerPorNumerosRuc`)
- **VehiculoService** → SRI Matriculación (`obtenerPorNumeroPlacaOPorNumeroCampvOPorNumeroCpn`)
- **PuntosLicenciaService** → ANT Portal Web + Redis Cache + Circuit Breaker

## Patrón de Caché (ANT)

```
Cache-Aside + Fallback con doble TTL:
  - Soft TTL (24h): dato fresco → no llama a la ANT
  - Hard TTL (7d): dato expirado → se borra de Redis

Si la ANT falla y hay dato viejo → lo devuelve con advertencia.
Protegido por Resilience4j Circuit Breaker.
```

## Endpoints

| Método | Endpoint | Descripción |
|--------|---------|------------|
| GET | `/api/contribuyente/verificar?identificacion=` | Verifica si es contribuyente |
| GET | `/api/contribuyente/info?identificacion=` | Datos de persona natural |
| GET | `/api/vehiculo?placa=` | Datos del vehículo |
| GET | `/api/puntos?cedula=&placa=` | Puntos de licencia (con caché) |

## Requisitos

- Java 17+
- Maven 3.8+
- Redis (local o cloud)

## Ejecutar

```bash
# Configurar Redis en application.yml o con variables de entorno
export REDIS_HOST=tu-host-redis
export REDIS_PORT=6379
export REDIS_PASSWORD=tu-password

# Compilar y ejecutar
mvn clean install
mvn spring-boot:run
```

## Tecnologías

- Spring Boot 3.2
- Spring WebFlux (WebClient)
- Spring Data Redis
- Resilience4j (Circuit Breaker)
- Jsoup (HTML Parser para ANT)
- Lombok
