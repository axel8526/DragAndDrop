package com.example.usuario.pracdraganddrop.modelos;

public class ConfigDO {

    private int estadoEntrada, estadoSalida;
    private long intervalo;

    public ConfigDO(int estadoEntrada, int estadoSalida) {
        this.estadoEntrada = estadoEntrada;
        this.estadoSalida = estadoSalida;
    }

    public long getIntervalo() {
        return intervalo;
    }

    public void setIntervalo(long intervalo) {
        this.intervalo = intervalo;
    }

    public int getEstadoEntrada() {
        return estadoEntrada;
    }

    public void setEstadoEntrada(int estadoEntrada) {
        this.estadoEntrada = estadoEntrada;
    }

    public int getEstadoSalida() {
        return estadoSalida;
    }

    public void setEstadoSalida(int estadoSalida) {
        this.estadoSalida = estadoSalida;
    }
}
