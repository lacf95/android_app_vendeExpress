package com.lacf.luisadrian.vendexpress;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class Singup extends AppCompatActivity {

    private EditText txtEmail;
    private EditText txtPassword;
    private EditText txtFirstName;
    private EditText txtLastName;
    private EditText txtPhone;
    private Button btnSignUp;
    private static final String URL = "http://salesapi2016.azurewebsites.net/api/vendedores";
    private static final String KEY_EMAIL = "correo";
    private static final String KEY_PASSWORD = "clave";
    private static final String KEY_FIRSTNAME = "nombre";
    private static final String KEY_LASTNAME = "apellido";
    private static final String KEY_PHONE = "telefono";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singup);
        txtEmail = (EditText) findViewById(R.id.txt_email);
        txtPassword = (EditText) findViewById(R.id.txt_password);
        txtFirstName = (EditText) findViewById(R.id.txt_firstname);
        txtLastName = (EditText) findViewById(R.id.txt_lastname);
        txtPhone = (EditText) findViewById(R.id.txt_phone);
        btnSignUp = (Button) findViewById(R.id.btn_signup);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    signup();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void signup() throws JSONException {
        final String email = txtEmail.getText().toString().trim();
        final String password = txtPassword.getText().toString().trim();
        final String firstName = txtFirstName.getText().toString().trim();
        final String lastName = txtLastName.getText().toString().trim();
        final String phone = txtPhone.getText().toString().trim();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        HashMap<String, String> map = jsonToMap(response);
                        if (map.get("id") != null) {
                            Intent intent = new Intent(Singup.this, Login.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(Singup.this, "Parámetros inválidos",Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Singup.this, "Error del servidor, intenta más tarde",Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put(KEY_EMAIL, email);
                params.put(KEY_PASSWORD, password);
                params.put(KEY_FIRSTNAME, firstName);
                params.put(KEY_LASTNAME, lastName);
                params.put(KEY_PHONE, phone);
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
