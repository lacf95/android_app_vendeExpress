package com.lacf.luisadrian.vendexpress;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
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
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.lacf.luisadrian.vendexpress.models.Cliente;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ClienteEdit extends AppCompatActivity {

    private String id;
    EditText lblDetallesClienteId, lblDetallesClienteNombre, lblDetallesClienteCorreo, lblDetallesClienteNumero, lblDetallesClienteDomicilio;
    private static final String URL = "http://salesapi2016.azurewebsites.net/api/clientes";
    private static final String KEY_ID = "id";
    private static final String KEY_EMAIL = "correo";
    private static final String KEY_NAME = "nombre";
    private static final String KEY_PHONE = "telefono";
    private static final String KEY_ADDRESS = "direccion";
    Button btnEditar, btnEliminar;
    Boolean flgEditar = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cliente_edit);
        Bundle bundle = getIntent().getExtras();
        id = bundle.getString("id");
        lblDetallesClienteId = (EditText) findViewById(R.id.lbl_detalles_cliente_id);
        lblDetallesClienteNombre = (EditText) findViewById(R.id.lbl_detalles_cliente_nombre);
        lblDetallesClienteCorreo = (EditText) findViewById(R.id.lbl_detalles_cliente_correo);
        lblDetallesClienteNumero = (EditText) findViewById(R.id.lbl_detalles_cliente_telefono);
        lblDetallesClienteDomicilio = (EditText) findViewById(R.id.lbl_detalles_cliente_domicilio);
        lblDetallesClienteId.setText(id);
        try {
            getInfo();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        btnEditar = (Button) findViewById(R.id.btn_editar_cliente);
        btnEliminar = (Button) findViewById(R.id.btn_eliminar_cliente);
        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setEditar();
            }
        });
        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setEliminar();
            }
        });
    }

    private void setEliminar() {
        AlertDialog.Builder dialogo = new AlertDialog.Builder(this);
        dialogo.setTitle("¡Importante!");
        dialogo.setMessage("¿Deseas eliminar el cliente?");
        dialogo.setCancelable(false);
        dialogo.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                aceptar();
            }
        });
        dialogo.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        dialogo.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();    //Call the back button's method
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void aceptar() {
        final JSONObject object = new JSONObject();

        try {
            object.put(KEY_ID, id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.DELETE, URL+"/"+id, object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(getApplication(), "Eliminado", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplication(), "Error del servidor, intenta más tarde",Toast.LENGTH_LONG).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
    }

    private void setEditar() {
        if (!flgEditar) {
            btnEditar.setText("Guardar");
            flgEditar = true;
            lblDetallesClienteNombre.setEnabled(true);
            lblDetallesClienteCorreo.setEnabled(true);
            lblDetallesClienteNumero.setEnabled(true);
            lblDetallesClienteDomicilio.setEnabled(true);
            lblDetallesClienteNombre.requestFocus();
        } else {
            final String nombre = lblDetallesClienteNombre.getText().toString().trim();
            final String correo = lblDetallesClienteCorreo.getText().toString().trim();
            final String numero = lblDetallesClienteNumero.getText().toString().trim();
            final String domicilio = lblDetallesClienteDomicilio.getText().toString().trim();
            StringRequest stringRequest = new StringRequest(Request.Method.PUT, URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Toast.makeText(getApplication(), "Modificado", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplication(), "Error del servidor, intenta más tarde",Toast.LENGTH_LONG).show();
                }
            }) {
                @Override
                protected Map<String,String> getParams(){
                    Map<String,String> params = new HashMap<String, String>();
                    params.put(KEY_ID, id);
                    params.put(KEY_EMAIL, correo);
                    params.put(KEY_NAME, nombre);
                    params.put(KEY_ADDRESS, domicilio);
                    params.put(KEY_PHONE, numero);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
        }
    }

    private void getInfo() throws JSONException {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, URL+"?id="+id, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        ArrayList<Cliente> infoCliente = new ArrayList<>();
                        infoCliente = parseJSON(response);
                        if (infoCliente.size() > 0) {
                            lblDetallesClienteId.setText(infoCliente.get(0).getId());
                            lblDetallesClienteNombre.setText(infoCliente.get(0).getName());
                            lblDetallesClienteCorreo.setText(infoCliente.get(0).getEmail());
                            lblDetallesClienteNumero.setText(infoCliente.get(0).getPhone());
                            lblDetallesClienteDomicilio.setText(infoCliente.get(0).getAddress());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplication(), "", Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }

    private ArrayList<Cliente> parseJSON(JSONArray response) {
        ArrayList<Cliente> clientes = new ArrayList<>();
        if (response != null && response.length() > 0) {
            for (int i = 0; i < response.length(); i++) {
                try {
                    JSONObject currentCliente = response.getJSONObject(i);
                    String id = "";
                    String name = "";
                    String email = "";
                    String phone = "";
                    String address = "";
                    String seller = "";
                    if (currentCliente.has("id"))
                        id = currentCliente.getString("id");
                    if (currentCliente.has("nombre"))
                        name = currentCliente.getString("nombre");
                    if (currentCliente.has("correo"))
                        email = currentCliente.getString("correo");
                    if (currentCliente.has("telefono"))
                        phone = currentCliente.getString("telefono");
                    if (currentCliente.has("direccion"))
                        address = currentCliente.getString("direccion");
                    if (currentCliente.has("vendedor"))
                        seller = currentCliente.getString("vendedor");
                    clientes.add(new Cliente(id, name, email, phone, address, seller));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return clientes;
    }
}
