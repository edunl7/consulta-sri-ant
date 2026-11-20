package com.consulta.client;

import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Collections;
import java.util.Map;

@Component
public class SriCatastroClient {

    private static final Logger log = LoggerFactory.getLogger(SriCatastroClient.class);
    private final WebClient webClient;

    @Value("${external.sri.catastro.base-url}")
    private String baseUrl;

    public SriCatastroClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public boolean existeContribuyente(String ruc) {
        try {
            String url = baseUrl + "/existePorNumeroRuc?numeroRuc=" + ruc;
            log.info("Consultando existencia de RUC: {}", ruc);

            JsonNode response = webClient.get()
                    .uri(url)
                    .retrieve()
                    .bodyToMono(JsonNode.class)
                    .block();

            if (response == null) return false;

            if (response.isBoolean()) {
                return response.asBoolean();
            }
            if (response.has("existeContribuyente")) {
                return response.get("existeContribuyente").asBoolean();
            }
            return response.asBoolean(false);

        } catch (Exception e) {
            log.error("Error verificando RUC {}: {}", ruc, e.getMessage());
            throw new RuntimeException("Error al consultar el SRI: " + e.getMessage(), e);
        }
    }

    public Map<String, Object> obtenerContribuyente(String ruc) {
        try {
            String url = baseUrl + "/obtenerPorNumerosRuc?&ruc=" + ruc;
            log.info("Obteniendo datos del contribuyente RUC: {}", ruc);

            // El SRI devuelve un array de contribuyentes: [{ "numeroRuc": ... }]
            @SuppressWarnings("unchecked")
            Map<String, Object>[] responseArray = webClient.get()
                    .uri(url)
                    .retrieve()
                    .bodyToMono(Map[].class)
                    .block();

            if (responseArray != null && responseArray.length > 0) {
                return responseArray[0];
            }

            return Collections.emptyMap();

        } catch (Exception e) {
            log.error("Error obteniendo contribuyente {}: {}", ruc, e.getMessage());
            throw new RuntimeException("Error al obtener datos del SRI: " + e.getMessage(), e);
        }
    }
}
