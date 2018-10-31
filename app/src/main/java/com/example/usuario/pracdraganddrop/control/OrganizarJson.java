package com.example.usuario.pracdraganddrop.control;

import android.content.Context;

import com.example.usuario.pracdraganddrop.componentes.entradas_digitales.EntradaDigitalView;
import com.example.usuario.pracdraganddrop.componentes.salidas_digitales.SalidaDigitalView;
import com.example.usuario.pracdraganddrop.modelos.ConfigDO;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class OrganizarJson {

    private Context context;

    public OrganizarJson(Context context){

    }
    public String getJson(ArrayList<SalidaDigitalView> salidaDigitalViews){
        JSONArray jsonArray=new JSONArray();
        for (int i=0; i<salidaDigitalViews.size();i++){
            JSONObject jsonObject=new JSONObject();
            SalidaDigitalView salida=salidaDigitalViews.get(i);
            try{

                jsonObject.put("salida",salida.getReferencia());
                jsonObject.put("estado",salida.getEstado());
                jsonObject.put("intervalo",salida.isIntervalo());
                jsonObject.put("timeIntervalo",salida.getTiempoIntervalo());



                if (salida.isUnido()){
                    JSONObject jsonConfi=new JSONObject();
                    if (salida.getConfigDO()==null)salida.setConfigDO(new ConfigDO(0,0));

                    jsonConfi.put("estado_entrada",salida.getConfigDO().getEstadoEntrada());
                    jsonConfi.put("estado_salida",salida.getConfigDO().getEstadoSalida());

                    jsonObject.put("entrada",((EntradaDigitalView)salida.getEntradaView()).getReferencia());
                    jsonObject.put("configuracion",jsonConfi);
                }


            }catch (JSONException e){}
            jsonArray.put(jsonObject);
        }
       return jsonArray.toString();
    }
}
