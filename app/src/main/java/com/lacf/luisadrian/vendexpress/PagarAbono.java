package com.lacf.luisadrian.vendexpress;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.lacf.luisadrian.vendexpress.models.Abono;
import com.lacf.luisadrian.vendexpress.models.Cliente;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PagarAbono extends AppCompatActivity {
    private String id;
    TextView lblCustomerName, lblDate, lblCustomerAddress, lblCustomerPhone, lblTotal;
    EditText txtAbono;
    Button btnPagar;
    private static final String URL = "http://salesapi2016.azurewebsites.net/api/abonos";
    private static final String KEY_ID = "id";
    private static final String KEY_PAYMENT = "abono";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagar_abono);
        Bundle bundle = getIntent().getExtras();
        id = bundle.getString("id");
        lblDate = (TextView) findViewById(R.id.lbl_abono_date);
        lblTotal = (TextView) findViewById(R.id.lbl_abono_total);
        lblCustomerName = (TextView) findViewById(R.id.lbl_abono_customerName);
        lblCustomerAddress = (TextView) findViewById(R.id.lbl_abono_customerAddress);
        lblCustomerPhone = (TextView) findViewById(R.id.lbl_abono_customerPhone);
        txtAbono = (EditText) findViewById(R.id.txt_abono_payment);
        try {
            getInfo();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        btnPagar = (Button) findViewById(R.id.btn_abono_pagar);
        btnPagar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pagar();
            }
        });
    }

    private void pagar() {
        final String abono = txtAbono.getText().toString().trim();
        StringRequest stringRequest = new StringRequest(Request.Method.PUT, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getApplication(), "Pagado", Toast.LENGTH_SHORT).show();
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
                params.put(KEY_PAYMENT, abono);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();    //Call the back button's method
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void getInfo() throws JSONException {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, URL+"?id="+id, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        ArrayList<Abono> infoCliente = new ArrayList<>();
                        infoCliente = parseJSON(response);
                        if (infoCliente.size() > 0) {
                            lblTotal.setText(infoCliente.get(0).getTotal());
                            lblCustomerName.setText(infoCliente.get(0).getCustomerName());
                            lblCustomerAddress.setText(infoCliente.get(0).getCustomerAddress());
                            lblCustomerPhone.setText(infoCliente.get(0).getCustomerPhone());
                            lblDate.setText(infoCliente.get(0).getDate());
                            txtAbono.setText(infoCliente.get(0).getPayment());
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

    private ArrayList<Abono> parseJSON(JSONArray response) {
        ArrayList<Abono> abonos = new ArrayList<>();
        if (response != null && response.length() > 0) {
            for (int i = 0; i < response.length(); i++) {
                try {
                    JSONObject currentAbono = response.getJSONObject(i);
                    String id = "";
                    String customerName = "";
                    String payment = "";
                    String date = "";
                    String customerPhone = "";
                    String customerAddress = "";
                    String state = "";
                    String sale = "";
                    String total = "";
                    if (currentAbono.has("id"))
                        id = currentAbono.getString("id");
                    if (currentAbono.has("venta"))
                        sale = currentAbono.getString("venta");
                    if (currentAbono.has("nombre"))
                        customerName = currentAbono.getString("nombre");
                    if (currentAbono.has("abono"))
                        payment = currentAbono.getString("abono");
                    if (currentAbono.has("telefono"))
                        customerPhone = currentAbono.getString("telefono");
                    if (currentAbono.has("direccion"))
                        customerAddress = currentAbono.getString("direccion");
                    if (currentAbono.has("fecha"))
                        date = currentAbono.getString("fecha");
                    if (currentAbono.has("estado"))
                        state = currentAbono.getString("estado");
                    if (currentAbono.has("total"))
                        total = currentAbono.getString("total");
                    Abono abono = new Abono(id, payment, date, state, sale, customerName, customerAddress, customerPhone);
                    abono.setTotal(total);
                    abonos.add(abono);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return abonos;
    }
}
