package com.lacf.luisadrian.vendexpress;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ClienteAdd extends AppCompatActivity {

    private EditText txtName;
    private EditText txtEmail;
    private EditText txtAddress;
    private EditText txtPhone;

    private Button btnAdd;

    private static final String URL = "http://salesapi2016.azurewebsites.net/api/clientes";
    private static final String KEY_EMAIL = "correo";
    private static final String KEY_NAME = "nombre";
    private static final String KEY_PHONE = "telefono";
    private static final String KEY_ADDRESS = "direccion";
    private static final String KEY_SELLER = "vendedor";
    private SharedPreferences data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cliente_add);
        txtEmail = (EditText) findViewById(R.id.txt_cliente_add_email);
        txtName = (EditText) findViewById(R.id.txt_cliente_add_name);
        txtAddress = (EditText) findViewById(R.id.txt_cliente_add_address);
        txtPhone = (EditText) findViewById(R.id.txt_cliente_add_phone);
        data = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        btnAdd = (Button) findViewById(R.id.btn_cliente_add_add);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    addCliente();
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

    private void addCliente() throws JSONException {
        final String email = txtEmail.getText().toString().trim();
        final String name = txtName.getText().toString().trim();
        final String address = txtAddress.getText().toString().trim();
        final String phone = txtPhone.getText().toString().trim();
        final String seller = data.getString("id", "");

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        HashMap<String, String> map = jsonToMap(response);
                        if (map.get("id") != null) {
                            finish();
                            /*Intent intent = new Intent(ClienteAdd.this, Home.class);
                            startActivity(intent);*/
                        } else {
                            Toast.makeText(ClienteAdd.this, "Parámetros inválidos",Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ClienteAdd.this, "Error del servidor, intenta más tarde",Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put(KEY_EMAIL, email);
                params.put(KEY_NAME, name);
                params.put(KEY_ADDRESS, address);
                params.put(KEY_PHONE, phone);
                params.put(KEY_SELLER, seller);
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