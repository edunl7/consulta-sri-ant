package com.consulta.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Contribuyente {

    private String numeroRuc;
    private String razonSocial;
    private String nombreComercial;
    private String estadoContribuyente;
    private String claseContribuyente;    // ej: PERSONA NATURAL o SOCIEDAD
    private String tipoContribuyente;
    private String fechaInicioActividades;
    private String fechaActualizacion;
    private String obligado;
    private String actividadEconomicaPrincipal;

    public Contribuyente() {}

    public boolean esPersonaNatural() {
        if (tipoContribuyente != null && tipoContribuyente.toUpperCase().contains("NATURAL")) {
            return true;
        }
        if (claseContribuyente != null && claseContribuyente.toUpperCase().contains("NATURAL")) {
            return true;
        }
        return false;
    }

    public static Contribuyente fromSriMap(Map<String, Object> map) {
        Contribuyente c = new Contribuyente();
        if (map == null) return c;

        c.setNumeroRuc((String) map.get("numeroRuc"));
        c.setRazonSocial((String) map.get("razonSocial"));
        c.setNombreComercial((String) map.get("nombreComercial"));
        c.setEstadoContribuyente((String) map.get("estadoContribuyenteRuc"));
        c.setTipoContribuyente((String) map.get("tipoContribuyente"));
        c.setClaseContribuyente((String) map.get("tipoContribuyente"));
        c.setObligado((String) map.get("obligadoLlevarContabilidad"));
        c.setActividadEconomicaPrincipal((String) map.get("actividadEconomicaPrincipal"));

        Object fechasObj = map.get("informacionFechasContribuyente");
        if (fechasObj instanceof Map<?, ?> fechas) {
            c.setFechaInicioActividades((String) fechas.get("fechaInicioActividades"));
            c.setFechaActualizacion((String) fechas.get("fechaActualizacion"));
        }

        return c;
    }

    public String getNumeroRuc() { return numeroRuc; }
    public void setNumeroRuc(String numeroRuc) { this.numeroRuc = numeroRuc; }

    public String getRazonSocial() { return razonSocial; }
    public void setRazonSocial(String razonSocial) { this.razonSocial = razonSocial; }

    public String getNombreComercial() { return nombreComercial; }
    public void setNombreComercial(String nombreComercial) { this.nombreComercial = nombreComercial; }

    public String getEstadoContribuyente() { return estadoContribuyente; }
    public void setEstadoContribuyente(String estadoContribuyente) { this.estadoContribuyente = estadoContribuyente; }

    public String getClaseContribuyente() { return claseContribuyente; }
    public void setClaseContribuyente(String claseContribuyente) { this.claseContribuyente = claseContribuyente; }

    public String getTipoContribuyente() { return tipoContribuyente; }
    public void setTipoContribuyente(String tipoContribuyente) { this.tipoContribuyente = tipoContribuyente; }

    public String getFechaInicioActividades() { return fechaInicioActividades; }
    public void setFechaInicioActividades(String fechaInicioActividades) { this.fechaInicioActividades = fechaInicioActividades; }

    public String getFechaActualizacion() { return fechaActualizacion; }
    public void setFechaActualizacion(String fechaActualizacion) { this.fechaActualizacion = fechaActualizacion; }

    public String getObligado() { return obligado; }
    public void setObligado(String obligado) { this.obligado = obligado; }

    public String getActividadEconomicaPrincipal() { return actividadEconomicaPrincipal; }
    public void setActividadEconomicaPrincipal(String actividadEconomicaPrincipal) { this.actividadEconomicaPrincipal = actividadEconomicaPrincipal; }
}
