package com.example.usuario.pracdraganddrop.control;

import android.content.Context;
import android.view.View;

import com.example.usuario.pracdraganddrop.componentes.EntradaView;
import com.example.usuario.pracdraganddrop.componentes.SalidaView;
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
    public String getJson2(ArrayList<View> componentes){
        JSONArray jsonArray=new JSONArray();



        for (int i=0; i<componentes.size();i++){
            JSONObject jsonComponente=new JSONObject();


            try {

                if (componentes.get(i) instanceof EntradaDigitalView) {
                    EntradaDigitalView entrada = (EntradaDigitalView) componentes.get(i);

                    jsonComponente.put("chanel", entrada.getReferencia());
                    jsonComponente.put("type","input");
                    jsonComponente.put("interval",false);
                    jsonComponente.put("time_interval",0);
                    jsonComponente.put("initial_state:",0);
                    jsonComponente.put("value_entry",false);
                    jsonComponente.put("value_output",false);

                    ArrayList<SalidaView> salidaViews=entrada.getSalidaViews();
                    JSONArray arraySalidas=new JSONArray();

                    for (int j=0; j<salidaViews.size();j++){
                        SalidaDigitalView salida=(SalidaDigitalView)salidaViews.get(j);
                        JSONObject jsonSalida=new JSONObject();

                        jsonSalida.put("chanel", salida.getReferencia());
                        jsonSalida.put("type","output");
                        jsonSalida.put("interval",salida.isIntervalo());
                        jsonSalida.put("time_interval",salida.getTiempoIntervalo());
                        jsonSalida.put("initial_state:",salida.getEstado());

                        if (salida.getConfigDO()!=null){
                            if (salida.getConfigDO().getEstadoEntrada()==0){
                                jsonSalida.put("value_entry",true);

                            }else{
                                jsonSalida.put("value_entry",false);
                            }

                            if (salida.getConfigDO().getEstadoSalida()==0){
                                jsonSalida.put("value_output",true);

                            }else{
                                jsonSalida.put("value_output",false);
                            }

                        }else{
                            jsonSalida.put("value_entry",true);
                            jsonSalida.put("value_output",true);
                        }


                        jsonSalida.put("outputs",null);

                        arraySalidas.put(jsonSalida);
                    }

                    jsonComponente.put("outputs",arraySalidas);


                } else if (componentes.get(i) instanceof SalidaDigitalView) {


                    SalidaDigitalView salida = (SalidaDigitalView) componentes.get(i);
                    if (!salida.isUnido()){
                        jsonComponente.put("chanel", salida.getReferencia());
                        jsonComponente.put("type","output");
                        jsonComponente.put("interval",salida.isIntervalo());
                        jsonComponente.put("time_interval",salida.getTiempoIntervalo());
                        jsonComponente.put("initial_state:",salida.getEstado());

                        if (salida.getConfigDO()!=null){
                            if (salida.getConfigDO().getEstadoEntrada()==0){
                                jsonComponente.put("value_entry",true);

                            }else{
                                jsonComponente.put("value_entry",false);
                            }

                            if (salida.getConfigDO().getEstadoSalida()==0){
                                jsonComponente.put("value_output",true);

                            }else{
                                jsonComponente.put("value_output",false);
                            }

                        }else{
                            jsonComponente.put("value_entry",true);
                            jsonComponente.put("value_output",true);
                        }

                        jsonComponente.put("outputs",null);
                    }else{

                        jsonComponente=null;
                    }

                }

            }catch (JSONException e){ }
            if (jsonComponente!=null) jsonArray.put(jsonComponente);
        }

        return jsonArray.toString();
    }
}
