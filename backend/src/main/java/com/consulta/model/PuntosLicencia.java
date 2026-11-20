package com.consulta.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PuntosLicencia implements Serializable {

    private String cedula;
    private String placa;
    private int puntosActuales;
    private int totalCitaciones;
    private List<Citacion> citaciones;
    private LocalDateTime fechaConsulta;
    private boolean desdeCachE;

    public PuntosLicencia() {}

    public PuntosLicencia(String cedula, String placa, int puntosActuales, int totalCitaciones, List<Citacion> citaciones, LocalDateTime fechaConsulta, boolean desdeCachE) {
        this.cedula = cedula;
        this.placa = placa;
        this.puntosActuales = puntosActuales;
        this.totalCitaciones = totalCitaciones;
        this.citaciones = citaciones;
        this.fechaConsulta = fechaConsulta;
        this.desdeCachE = desdeCachE;
    }

    public String getCedula() { return cedula; }
    public void setCedula(String cedula) { this.cedula = cedula; }

    public String getPlaca() { return placa; }
    public void setPlaca(String placa) { this.placa = placa; }

    public int getPuntosActuales() { return puntosActuales; }
    public void setPuntosActuales(int puntosActuales) { this.puntosActuales = puntosActuales; }

    public int getTotalCitaciones() { return totalCitaciones; }
    public void setTotalCitaciones(int totalCitaciones) { this.totalCitaciones = totalCitaciones; }

    public List<Citacion> getCitaciones() { return citaciones; }
    public void setCitaciones(List<Citacion> citaciones) { this.citaciones = citaciones; }

    public LocalDateTime getFechaConsulta() { return fechaConsulta; }
    public void setFechaConsulta(LocalDateTime fechaConsulta) { this.fechaConsulta = fechaConsulta; }

    public boolean isDesdeCachE() { return desdeCachE; }
    public void setDesdeCachE(boolean desdeCachE) { this.desdeCachE = desdeCachE; }

    public static class Citacion implements Serializable {
        private String numeroCitacion;
        private String fechaCitacion;
        private String infraccion;
        private String puntos;
        private String estado;

        public Citacion() {}

        public Citacion(String numeroCitacion, String fechaCitacion, String infraccion, String puntos, String estado) {
            this.numeroCitacion = numeroCitacion;
            this.fechaCitacion = fechaCitacion;
            this.infraccion = infraccion;
            this.puntos = puntos;
            this.estado = estado;
        }

        public String getNumeroCitacion() { return numeroCitacion; }
        public void setNumeroCitacion(String numeroCitacion) { this.numeroCitacion = numeroCitacion; }

        public String getFechaCitacion() { return fechaCitacion; }
        public void setFechaCitacion(String fechaCitacion) { this.fechaCitacion = fechaCitacion; }

        public String getInfraccion() { return infraccion; }
        public void setInfraccion(String infraccion) { this.infraccion = infraccion; }

        public String getPuntos() { return puntos; }
        public void setPuntos(String puntos) { this.puntos = puntos; }

        public String getEstado() { return estado; }
        public void setEstado(String estado) { this.estado = estado; }
    }
}
