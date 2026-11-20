package com.consulta.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Component
public class SriVehiculoClient {

    private static final Logger log = LoggerFactory.getLogger(SriVehiculoClient.class);
    private final WebClient webClient;

    @Value("${external.sri.vehiculo.base-url}")
    private String baseUrl;

    public SriVehiculoClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public Map<String, Object> obtenerVehiculo(String placa) {
        try {
            String url = baseUrl
                    + "/obtenerPorNumeroPlacaOPorNumeroCampvOPorNumeroCpn?numeroPlacaCampvCpn="
                    + placa.toUpperCase().replace("-", "");
            log.info("Consultando vehículo con placa: {}", placa);

            @SuppressWarnings("unchecked")
            Map<String, Object> response = webClient.get()
                    .uri(url)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            return response;

        } catch (Exception e) {
            log.error("Error obteniendo vehículo placa {}: {}", placa, e.getMessage());
            throw new RuntimeException("Error al consultar vehículo en el SRI: " + e.getMessage(), e);
        }
    }
}
