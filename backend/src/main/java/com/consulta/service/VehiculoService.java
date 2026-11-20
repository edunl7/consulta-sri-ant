package com.consulta.service;

import com.consulta.client.SriVehiculoClient;
import com.consulta.model.Vehiculo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class VehiculoService {

    private static final Logger log = LoggerFactory.getLogger(VehiculoService.class);
    private final SriVehiculoClient sriVehiculoClient;

    public VehiculoService(SriVehiculoClient sriVehiculoClient) {
        this.sriVehiculoClient = sriVehiculoClient;
    }

    public Vehiculo obtenerVehiculo(String placa) {
        if (placa == null || placa.trim().isEmpty()) {
            throw new IllegalArgumentException("La placa es requerida");
        }

        String placaLimpia = placa.trim().toUpperCase();
        Map<String, Object> datos = sriVehiculoClient.obtenerVehiculo(placaLimpia);

        if (datos == null || datos.isEmpty()) {
            throw new RuntimeException("No se encontró vehículo con placa: " + placaLimpia);
        }

        Vehiculo vehiculo = Vehiculo.fromSriMap(datos);
        if (vehiculo.getPlaca() == null) {
            vehiculo.setPlaca(placaLimpia);
        }

        log.info("Vehículo encontrado: {} - {} {}", placaLimpia, vehiculo.getMarca(), vehiculo.getModelo());
        return vehiculo;
    }
}
