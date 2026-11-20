package com.consulta.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Vehiculo {

    private String placa;
    private String marca;
    private String modelo;
    private String anioModelo;
    private String cilindraje;
    private String tipo;
    private String claseVehiculo;
    private String subclaseVehiculo;
    private String paisOrigen;
    private String color;
    private String combustible;
    private String estado;
    private String campv;
    private String ramv;

    public Vehiculo() {}

    public static Vehiculo fromSriMap(Map<String, Object> map) {
        Vehiculo v = new Vehiculo();
        if (map == null) return v;

        v.setPlaca((String) map.get("numeroPlaca"));
        v.setMarca((String) map.get("descripcionMarca"));
        v.setModelo((String) map.get("descripcionModelo"));
        Object anio = map.get("anioAuto");
        v.setAnioModelo(anio != null ? anio.toString() : "—");
        Object cil = map.get("cilindraje");
        v.setCilindraje(cil != null ? cil.toString() : "—");
        v.setClaseVehiculo((String) map.get("nombreClase"));
        v.setPaisOrigen((String) map.get("descripcionPais"));
        v.setColor((String) map.get("colorVehiculo1"));
        v.setCampv((String) map.get("numeroCamvCpn"));
        v.setEstado((String) map.get("estadoExoneracion"));

        return v;
    }

    public String getPlaca() { return placa; }
    public void setPlaca(String placa) { this.placa = placa; }

    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }

    public String getModelo() { return modelo; }
    public void setModelo(String modelo) { this.modelo = modelo; }

    public String getAnioModelo() { return anioModelo; }
    public void setAnioModelo(String anioModelo) { this.anioModelo = anioModelo; }

    public String getCilindraje() { return cilindraje; }
    public void setCilindraje(String cilindraje) { this.cilindraje = cilindraje; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getClaseVehiculo() { return claseVehiculo; }
    public void setClaseVehiculo(String claseVehiculo) { this.claseVehiculo = claseVehiculo; }

    public String getSubclaseVehiculo() { return subclaseVehiculo; }
    public void setSubclaseVehiculo(String subclaseVehiculo) { this.subclaseVehiculo = subclaseVehiculo; }

    public String getPaisOrigen() { return paisOrigen; }
    public void setPaisOrigen(String paisOrigen) { this.paisOrigen = paisOrigen; }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }

    public String getCombustible() { return combustible; }
    public void setCombustible(String combustible) { this.combustible = combustible; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getCampv() { return campv; }
    public void setCampv(String campv) { this.campv = campv; }

    public String getRamv() { return ramv; }
    public void setRamv(String ramv) { this.ramv = ramv; }
}
