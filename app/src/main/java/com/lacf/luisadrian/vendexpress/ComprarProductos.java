package com.lacf.luisadrian.vendexpress;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ComprarProductos extends AppCompatActivity {
    private String total;
    private TextView lblTotal;
    private EditText txtAbono, txtPagos, txtDay, txtTime;
    private Button btnFinalizar;
    private Spinner cmbClientes, cmbTipoPagos;
    private ArrayAdapter<String> adapter;
    private List<String> idClientes = new ArrayList<String>();
    private List<String> nombreClientes = new ArrayList<String>();

    private static final String URL = "http://salesapi2016.azurewebsites.net/api/";
    private SharedPreferences data;
    private static final String KEY_SELLER = "vendedor";
    private static final String KEY_TOTAL = "total";
    private static final String KEY_ADVANCE = "abonado";
    private static final String KEY_NEXT_PAYMENT = "proximo_pago";
    private static final String KEY_NO_PAGOS = "no_pagos";
    private static final String KEY_TIPO_PAGOS = "tipo_pago";
    private static final String KEY_CUSTOMER = "cliente";
    private RequestQueue requestQueue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comprar_productos);
        Bundle bundle = getIntent().getExtras();

        requestQueue = Volley.newRequestQueue(getApplicationContext());
        data = getSharedPreferences("shoppingCart", Context.MODE_PRIVATE);
        total = bundle.getString("total");
        lblTotal = (TextView) findViewById(R.id.lbl_venta_total);
        lblTotal.setText(total);
        txtAbono = (EditText) findViewById(R.id.txt_venta_anticipo);
        txtDay = (EditText) findViewById(R.id.txt_venta_day);
        txtTime = (EditText) findViewById(R.id.txt_venta_time);
        txtPagos = (EditText) findViewById(R.id.txt_venta_no_pagos);
        cmbTipoPagos = (Spinner) findViewById(R.id.cmb_venta_tipo_pago);
        sendRequest();
        cmbClientes = (Spinner) findViewById(R.id.cmb_venta_clientes);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, nombreClientes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cmbClientes.setAdapter(adapter);
        btnFinalizar = (Button) findViewById(R.id.btn_venta_finalizar);
        btnFinalizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    addVenta();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();    //Call the back button's method
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void sendRequest() {
        String queryString = URL + "clientes?vendedor=" + data.getString("id", "");
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, queryString, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        parseJSON(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }
        );
        requestQueue.add(jsonArrayRequest);
    }

    private void parseJSON(JSONArray response) {
        if (response != null && response.length() > 0) {
            for (int i = 0; i < response.length(); i++) {
                try {
                    JSONObject currentCliente = response.getJSONObject(i);
                    String id = "";
                    String name = "";
                    if (currentCliente.has("id"))
                        id = currentCliente.getString("id");
                    if (currentCliente.has("nombre"))
                        name = currentCliente.getString("nombre");
                    idClientes.add(id);
                    nombreClientes.add(name);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            adapter.notifyDataSetChanged();
            cmbClientes.setSelection(0);
        }
    }

    private void addVenta() throws JSONException {
        final String total = this.total;
        final String abonado = txtAbono.getText().toString().trim();
        String no_pagos = txtPagos.getText().toString().trim();
        final String date = txtDay.getText().toString().trim();
        String[] formatDate = date.split("/");
        final String proximo_pago = formatDate[2] + "-" + formatDate[1] + "-" + formatDate[0] + " " + txtTime.getText().toString().trim();
        String tipo_pago = "";
        switch (cmbTipoPagos.getSelectedItem().toString()) {
            case "Contado":
                tipo_pago = "1";
                no_pagos = "1";
                break;
            case "Mensual":
                tipo_pago = "2";
                break;
            case "Quincenal":
                tipo_pago = "3";
                break;
            case "Semanal":
                tipo_pago = "4";
                break;
            default:
                tipo_pago = "1";
                break;
        }
        final String cliente = idClientes.get(cmbClientes.getSelectedItemPosition());
        final String finalNo_pagos = no_pagos;
        final String finalTipo_pago = tipo_pago;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL+"ventas",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        HashMap<String, String> map = jsonToMap(response);
                        if (map.get("id") != null) {
                            SharedPreferences.Editor editor = data.edit();
                            editor.clear();
                            editor.apply();
                            finish();
                        } else {
                            Toast.makeText(ComprarProductos.this, "Parámetros inválidos",Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ComprarProductos.this, "Error del servidor, intenta más tarde",Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put(KEY_TOTAL, total);
                params.put(KEY_ADVANCE, abonado);
                params.put(KEY_NEXT_PAYMENT, proximo_pago);
                params.put(KEY_NO_PAGOS, finalNo_pagos);
                params.put(KEY_TIPO_PAGOS, finalTipo_pago);
                params.put(KEY_CUSTOMER, cliente);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public HashMap<String, String> jsonToMap(String jsonString) {

        HashMap<String, String> map = new HashMap<String, String>();
        JSONObject jObject = null;
        try {
            jObject = new JSONObject(jsonString);
            Iterator<?> keys = jObject.keys();
            while(keys.hasNext()) {
                String key = (String)keys.next();
                String value = jObject.getString(key);
                map.put(key, value);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return map;
    }
}
