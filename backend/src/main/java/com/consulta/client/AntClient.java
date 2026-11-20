package com.consulta.client;

import com.consulta.model.PuntosLicencia;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class AntClient {

    private static final Logger log = LoggerFactory.getLogger(AntClient.class);

    @Value("${external.ant.base-url}")
    private String baseUrl;

    public PuntosLicencia consultarPuntos(String cedula, String placa) {
        try {
            String url = baseUrl
                    + "/clp_grid_citaciones.jsp"
                    + "?ps_tipo_identificacion=CED"
                    + "&ps_identificacion=" + cedula
                    + "&ps_placa=" + (placa != null ? placa : "");

            log.info("Consultando ANT para cédula: {} placa: {}", cedula, placa);

            Document doc = Jsoup.connect(url)
                    .timeout(10000)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36")
                    .get();

            return parsearRespuestaAnt(doc, cedula, placa);

        } catch (Exception e) {
            log.error("Error consultando ANT para cédula {}: {}", cedula, e.getMessage());
            throw new RuntimeException("Error al consultar la ANT: " + e.getMessage(), e);
        }
    }

    private PuntosLicencia parsearRespuestaAnt(Document doc, String cedula, String placa) {
        List<PuntosLicencia.Citacion> citaciones = new ArrayList<>();
        int totalPuntos = 30;

        Elements tablas = doc.select("table");
        for (Element tabla : tablas) {
            Elements filas = tabla.select("tr");
            for (int i = 1; i < filas.size(); i++) {
                Elements columnas = filas.get(i).select("td");
                if (columnas.size() >= 4) {
                    PuntosLicencia.Citacion citacion = new PuntosLicencia.Citacion(
                            columnas.get(0).text().trim(),
                            columnas.get(1).text().trim(),
                            columnas.size() > 2 ? columnas.get(2).text().trim() : "",
                            columnas.size() > 3 ? columnas.get(3).text().trim() : "0",
                            columnas.size() > 4 ? columnas.get(4).text().trim() : ""
                    );
                    citaciones.add(citacion);

                    try {
                        totalPuntos -= Integer.parseInt(citacion.getPuntos());
                    } catch (NumberFormatException ignored) {}
                }
            }
        }

        if (tablas.isEmpty()) {
            String bodyText = doc.body().text().toLowerCase();
            if (bodyText.contains("error") || bodyText.contains("mantenimiento")
                    || bodyText.contains("no disponible")) {
                throw new RuntimeException("Portal ANT en mantenimiento o con error");
            }
            log.info("No se encontraron citaciones para cédula: {}", cedula);
        }

        return new PuntosLicencia(
                cedula,
                placa,
                Math.max(totalPuntos, 0),
                citaciones.size(),
                citaciones,
                LocalDateTime.now(),
                false
        );
    }
}
