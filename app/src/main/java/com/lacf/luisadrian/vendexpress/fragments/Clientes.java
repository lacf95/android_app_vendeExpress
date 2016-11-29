package com.lacf.luisadrian.vendexpress.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.lacf.luisadrian.vendexpress.ClienteAdd;
import com.lacf.luisadrian.vendexpress.R;
import com.lacf.luisadrian.vendexpress.adapters.ClientesAdapter;
import com.lacf.luisadrian.vendexpress.models.Cliente;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Clientes extends Fragment {

    private static final String URL = "http://salesapi2016.azurewebsites.net/api/clientes";
    private SharedPreferences data;
    private static final String KEY_SELLER = "vendedor";
    private ArrayList<Cliente> clientes = new ArrayList<>();
    private RequestQueue requestQueue;
    private RecyclerView recyclerView;
    private ClientesAdapter clientesAdapter;

    public Clientes() {
        // Required empty public constructor
    }

    public static Clientes newInstance() {
        Clientes fragment = new Clientes();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_clientes, container, false);

        requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        Button btnAddCliente;
        data = getActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        btnAddCliente = (Button) view.findViewById(R.id.btn_add_cliente);
        btnAddCliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ClienteAdd.class);
                startActivity(intent);
            }
        });
        recyclerView = (RecyclerView) view.findViewById(R.id.rcv_clientes);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        clientesAdapter = new ClientesAdapter(getActivity());
        recyclerView.setAdapter(clientesAdapter);
        sendRequest();

        final EditText txtSearchCliente = (EditText) view.findViewById(R.id.txt_clientes_search);
        ImageButton btnSearchCliente;
        btnSearchCliente = (ImageButton) view.findViewById(R.id.btn_clientes_search);
        btnSearchCliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRequest(txtSearchCliente.getText().toString());
            }
        });

        return view;
    }

    private void sendRequest() {
        String queryString = URL + "?vendedor=" + data.getString("id", "");
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, queryString, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        clientes = parseJSON(response);
                        clientesAdapter.setClientes(clientes);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }
        );
        requestQueue.add(jsonArrayRequest);
    }

    private void sendRequest(String clientName) {
        String queryString = URL + "?vendedor=" + data.getString("id", "") + "&nombre=" + clientName;
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, queryString, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        clientes = parseJSON(response);
                        clientesAdapter.setClientes(clientes);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }
        );
        requestQueue.add(jsonArrayRequest);
    }

    @Override
    public void onResume() {
        super.onResume();
        sendRequest();
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

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

    }
}
