package com.lacf.luisadrian.vendexpress.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.lacf.luisadrian.vendexpress.ComprarProductos;
import com.lacf.luisadrian.vendexpress.R;
import com.lacf.luisadrian.vendexpress.adapters.CarritoAdapter;
import com.lacf.luisadrian.vendexpress.models.Producto;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Map;

public class Carrito extends Fragment {
    private SharedPreferences data;
    private TextView lblTotal;
    private ArrayList<Producto> productos = new ArrayList<>();
    private double total;
    private RecyclerView recyclerView;
    private CarritoAdapter carritoAdapter;

    public Carrito() {
        // Required empty public constructor
    }

    public static Carrito newInstance() {
        return new Carrito();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_carrito, container, false);
        data = this.getActivity().getSharedPreferences("shoppingCart", Context.MODE_PRIVATE);
        recyclerView = (RecyclerView) view.findViewById(R.id.rcv_carrito);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        carritoAdapter = new CarritoAdapter(getActivity(), Carrito.this);
        recyclerView.setAdapter(carritoAdapter);
        return view;
    }

    public void showShoppingCart() {
        total = 0;
        productos = new ArrayList<>();
        Map<String, ?> allEntries = data.getAll();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            String[] values = entry.getValue().toString().split(";;");
            Producto item = new Producto();
            item.setId(values[0]);
            item.setName(values[1]);
            item.setDescription(values[2]);
            item.setPhoto(values[3]);
            item.setPrice(values[4]);
            item.setQuantity(values[5]);
            total += Double.parseDouble(item.getPrice()) * Integer.parseInt(item.getQuantity());
            productos.add(item);
        }
        carritoAdapter.setProductos(productos);
        lblTotal.setText(new DecimalFormat("#.##").format(total));
    }

    @Override
    public void onResume() {
        super.onResume();
        showShoppingCart();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        lblTotal = (TextView) view.findViewById(R.id.lbl_shopping_total);
        Button btnPagar = (Button) view.findViewById(R.id.btn_shopping_pagar);
        btnPagar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ComprarProductos.class);
                intent.putExtra("total", new DecimalFormat("#.##").format(total));
                startActivity(intent);
            }
        });
        showShoppingCart();
    }
}
