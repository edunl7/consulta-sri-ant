package com.consulta.controller;

import com.consulta.model.ApiResponse;
import com.consulta.model.Contribuyente;
import com.consulta.model.PuntosLicencia;
import com.consulta.model.Vehiculo;
import com.consulta.service.ContribuyenteService;
import com.consulta.service.PuntosLicenciaService;
import com.consulta.service.VehiculoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ConsultaController {

    private static final Logger log = LoggerFactory.getLogger(ConsultaController.class);
    private final ContribuyenteService contribuyenteService;
    private final VehiculoService vehiculoService;
    private final PuntosLicenciaService puntosService;

    public ConsultaController(ContribuyenteService contribuyenteService,
                              VehiculoService vehiculoService,
                              PuntosLicenciaService puntosService) {
        this.contribuyenteService = contribuyenteService;
        this.vehiculoService = vehiculoService;
        this.puntosService = puntosService;
    }

    @GetMapping("/contribuyente/verificar")
    public ResponseEntity<ApiResponse<Boolean>> verificarContribuyente(
            @RequestParam String identificacion) {
        try {
            String ruc = contribuyenteService.convertirARuc(identificacion);
            boolean existe = contribuyenteService.verificarContribuyente(ruc);

            if (existe) {
                return ResponseEntity.ok(ApiResponse.ok(true, "Contribuyente encontrado"));
            } else {
                return ResponseEntity.ok(ApiResponse.ok(false, "No es contribuyente del SRI"));
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            log.error("Error verificando contribuyente: {}", e.getMessage());
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.error("Error al consultar el SRI: " + e.getMessage()));
        }
    }

    @GetMapping("/contribuyente/info")
    public ResponseEntity<ApiResponse<Contribuyente>> obtenerContribuyente(
            @RequestParam String identificacion) {
        try {
            String ruc = contribuyenteService.convertirARuc(identificacion);
            Contribuyente persona = contribuyenteService.obtenerPersonaNatural(ruc);
            return ResponseEntity.ok(ApiResponse.ok(persona));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.ok(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            log.error("Error obteniendo contribuyente: {}", e.getMessage());
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.error("Error interno: " + e.getMessage()));
        }
    }

    @GetMapping("/vehiculo")
    public ResponseEntity<ApiResponse<Vehiculo>> obtenerVehiculo(
            @RequestParam String placa) {
        try {
            Vehiculo vehiculo = vehiculoService.obtenerVehiculo(placa);
            return ResponseEntity.ok(ApiResponse.ok(vehiculo));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            log.error("Error obteniendo vehículo: {}", e.getMessage());
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.error("Error al consultar vehículo: " + e.getMessage()));
        }
    }

    @GetMapping("/puntos")
    public ResponseEntity<ApiResponse<PuntosLicencia>> consultarPuntos(
            @RequestParam String cedula,
            @RequestParam(required = false, defaultValue = "") String placa) {
        try {
            PuntosLicencia puntos = puntosService.consultarPuntos(cedula, placa);

            String msg = puntos.isDesdeCachE()
                    ? "Datos obtenidos desde caché (última consulta: " + puntos.getFechaConsulta() + ")"
                    : "Datos obtenidos en tiempo real desde la ANT";

            return ResponseEntity.ok(ApiResponse.ok(puntos, msg));
        } catch (Exception e) {
            log.error("Error consultando puntos: {}", e.getMessage());
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.error("Error al consultar puntos: " + e.getMessage()));
        }
    }

    @GetMapping("/health")
    public ResponseEntity<ApiResponse<String>> health() {
        return ResponseEntity.ok(ApiResponse.ok("Sistema operativo"));
    }
}
