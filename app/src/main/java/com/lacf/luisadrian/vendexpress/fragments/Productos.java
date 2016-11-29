package com.lacf.luisadrian.vendexpress.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.lacf.luisadrian.vendexpress.R;
import com.lacf.luisadrian.vendexpress.adapters.ClientesAdapter;
import com.lacf.luisadrian.vendexpress.adapters.ProductosAdapter;
import com.lacf.luisadrian.vendexpress.models.Cliente;
import com.lacf.luisadrian.vendexpress.models.Producto;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Productos extends Fragment {

    private static final String URL = "http://salesapi2016.azurewebsites.net/api/productos";
    private SharedPreferences data;
    private static final String KEY_SELLER = "vendedor";
    private ArrayList<Producto> productos = new ArrayList<>();
    private RequestQueue requestQueue;
    private RecyclerView recyclerView;
    private ProductosAdapter productosAdapter;

    public Productos() {

    }

    public static Productos newInstance() {
        Productos fragment = new Productos();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_productos, container, false);

        requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        Button btnAddProducto;
        data = getActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        btnAddProducto = (Button) view.findViewById(R.id.btn_add_producto);
        btnAddProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(getActivity(), ClienteAdd.class);
                //startActivity(intent);
            }
        });

        recyclerView = (RecyclerView) view.findViewById(R.id.rcv_productos);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        productosAdapter = new ProductosAdapter(getActivity());
        recyclerView.setAdapter(productosAdapter);
        sendRequest();

        final EditText txtSearchProducto = (EditText) view.findViewById(R.id.txt_productos_search);
        ImageButton btnSearchProducto;
        btnSearchProducto = (ImageButton) view.findViewById(R.id.btn_productos_search);
        btnSearchProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRequest(txtSearchProducto.getText().toString());
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
                        productos = parseJSON(response);
                        productosAdapter.setProductos(productos);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }
        );
        requestQueue.add(jsonArrayRequest);
    }

    private void sendRequest(String productName) {
        String queryString = URL + "?vendedor=" + data.getString("id", "") + "&nombre=" + productName;
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, queryString, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        productos = parseJSON(response);
                        productosAdapter.setProductos(productos);
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

    private ArrayList<Producto> parseJSON(JSONArray response) {
        ArrayList<Producto> productos = new ArrayList<>();
        if (response != null && response.length() > 0) {
            for (int i = 0; i < response.length(); i++) {
                try {
                    JSONObject currentProducto = response.getJSONObject(i);
                    String id = "";
                    String name = "";
                    String description = "";
                    String price = "";
                    String photo = "";
                    String seller = "";
                    if (currentProducto.has("id"))
                        id = currentProducto.getString("id");
                    if (currentProducto.has("nombre"))
                        name = currentProducto.getString("nombre");
                    if (currentProducto.has("descripcion"))
                        description = currentProducto.getString("descripcion");
                    if (currentProducto.has("precio"))
                        price = currentProducto.getString("precio");
                    if (currentProducto.has("fotografia"))
                        photo = currentProducto.getString("fotografia");
                    if (currentProducto.has("vendedor"))
                        seller = currentProducto.getString("vendedor");
                    productos.add(new Producto(id, name, description, photo, price, seller));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return productos;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

    }
}
