package com.example.usuario.pracdraganddrop.control;

import android.content.Context;
import android.view.View;

import com.example.usuario.pracdraganddrop.componentes.InputCView;
import com.example.usuario.pracdraganddrop.componentes.OutputCView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class OrganizarJson {

    private Context context;

    public OrganizarJson(Context context){

    }
    public String getJson(ArrayList<View> componentes){
        JSONArray jsonArray=new JSONArray();



        for (int i=0; i<componentes.size();i++){
            JSONObject jsonComponente=new JSONObject();


            try {

                if (componentes.get(i) instanceof InputCView) {
                    InputCView entrada = (InputCView) componentes.get(i);

                    jsonComponente.put("channel", entrada.getReference());
                    jsonComponente.put("type","input");
                    jsonComponente.put("interval",false);
                    jsonComponente.put("time_interval",0);
                    jsonComponente.put("initial_state",false);
                    jsonComponente.put("value_entry",false);
                    jsonComponente.put("value_output",false);

                    ArrayList<OutputCView> salidaViews=entrada.getOutputsView();
                    JSONArray arraySalidas=new JSONArray();

                    for (int j=0; j<salidaViews.size();j++){
                        OutputCView salida=(OutputCView) salidaViews.get(j);
                        JSONObject jsonSalida=new JSONObject();

                        jsonSalida.put("channel", salida.getReference());
                        jsonSalida.put("type","output");
                        jsonSalida.put("initial_state",salida.getValue()== 0? true : false);
                        jsonSalida.put("interval",salida.isIntervalo());
                        jsonSalida.put("time_interval",salida.getTiempoIntervalo());

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


                } else if (componentes.get(i) instanceof OutputCView) {


                    OutputCView salida = (OutputCView) componentes.get(i);
                    if (!salida.isUnido()){
                        jsonComponente.put("channel", salida.getReference());
                        jsonComponente.put("type","output");
                        jsonComponente.put("initial_state",salida.getValue()==0 ? true : false);
                        jsonComponente.put("interval",salida.isIntervalo());
                        jsonComponente.put("time_interval",salida.getTiempoIntervalo());

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
