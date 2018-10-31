package com.example.usuario.pracdraganddrop.componentes.salidas_digitales;

import android.content.Context;
import android.util.AttributeSet;

import com.example.usuario.pracdraganddrop.componentes.SalidaView;
import com.example.usuario.pracdraganddrop.modelos.ConfigDO;

public class SalidaDigitalView extends SalidaView {

    private String referencia="";
    private int estado=0;
    private ConfigDO configDO;
    private boolean intervalo;
    private long tiempoIntervalo=0;

    public SalidaDigitalView(Context context) {
        super(context);
    }

    public SalidaDigitalView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SalidaDigitalView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ConfigDO getConfigDO() {
        return configDO;
    }

    public void setConfigDO(ConfigDO configDO) {
        this.configDO = configDO;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public boolean isIntervalo() {
        return intervalo;
    }

    public void setIntervalo(boolean intervalo) {
        this.intervalo = intervalo;
    }

    public long getTiempoIntervalo() {
        return tiempoIntervalo;
    }

    public void setTiempoIntervalo(long tiempoIntervalo) {
        this.tiempoIntervalo = tiempoIntervalo;
    }
}
