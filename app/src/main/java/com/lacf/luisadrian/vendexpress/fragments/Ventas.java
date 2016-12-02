package com.lacf.luisadrian.vendexpress.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.lacf.luisadrian.vendexpress.R;
import com.lacf.luisadrian.vendexpress.adapters.AbonosAdapter;
import com.lacf.luisadrian.vendexpress.models.Abono;
import com.lacf.luisadrian.vendexpress.models.Cliente;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Ventas extends Fragment {

    private static final String URL = "http://salesapi2016.azurewebsites.net/api/abonos";
    private SharedPreferences data;
    private static final String KEY_SELLER = "vendedor";
    private ArrayList<Abono> abonos = new ArrayList<>();
    private RequestQueue requestQueue;
    private RecyclerView recyclerView;
    private AbonosAdapter abonosAdapter;

    public Ventas() {
        // Required empty public constructor
    }

    public static Ventas newInstance() {
        Ventas fragment = new Ventas();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ventas, container, false);
        requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        data = getActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        recyclerView = (RecyclerView) view.findViewById(R.id.rcv_abonos);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        abonosAdapter = new AbonosAdapter(getActivity());
        recyclerView.setAdapter(abonosAdapter);
        recyclerView.setNestedScrollingEnabled(false);
        sendRequest();
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

    }

    @Override
    public void onResume() {
        super.onResume();
        sendRequest();
    }

    private void sendRequest() {
        String queryString = URL + "?vendedor=" + data.getString("id", "") + "&estado=1";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, queryString, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        abonos = parseJSON(response);
                        abonosAdapter.setAbonos(abonos);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }
        );
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
                    abonos.add(new Abono(id, payment, date, state, sale, customerName, customerAddress, customerPhone));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return abonos;
    }
}
