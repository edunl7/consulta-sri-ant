package com.consulta.service;

import com.consulta.client.SriCatastroClient;
import com.consulta.model.Contribuyente;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ContribuyenteService {

    private static final Logger log = LoggerFactory.getLogger(ContribuyenteService.class);
    private final SriCatastroClient sriClient;

    public ContribuyenteService(SriCatastroClient sriClient) {
        this.sriClient = sriClient;
    }

    public String convertirARuc(String identificacion) {
        if (identificacion == null) throw new IllegalArgumentException("La identificación es requerida");
        String limpio = identificacion.trim().replaceAll("[^0-9]", "");

        if (limpio.length() == 10) {
            return limpio + "001";
        } else if (limpio.length() == 13) {
            return limpio;
        } else {
            throw new IllegalArgumentException(
                    "La identificación debe tener 10 dígitos (cédula) o 13 dígitos (RUC). Recibido: " + limpio.length());
        }
    }

    public String extraerCedula(String ruc) {
        if (ruc != null && ruc.length() >= 10) {
            return ruc.substring(0, 10);
        }
        return ruc;
    }

    public boolean verificarContribuyente(String ruc) {
        return sriClient.existeContribuyente(ruc);
    }

    public Contribuyente obtenerPersonaNatural(String ruc) {
        Map<String, Object> datos = sriClient.obtenerContribuyente(ruc);

        if (datos == null || datos.isEmpty()) {
            throw new RuntimeException("No se encontraron datos en el catastro para el RUC: " + ruc);
        }

        Contribuyente contribuyente = Contribuyente.fromSriMap(datos);
        if (contribuyente.getNumeroRuc() == null) {
            contribuyente.setNumeroRuc(ruc);
        }

        if (!contribuyente.esPersonaNatural()) {
            throw new RuntimeException(
                    "El RUC " + ruc + " no corresponde a una persona natural. "
                    + "Tipo: " + contribuyente.getTipoContribuyente());
        }

        log.info("Contribuyente encontrado: {} - {}", ruc, contribuyente.getRazonSocial());
        return contribuyente;
    }
}
