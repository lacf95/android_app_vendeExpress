package com.lacf.luisadrian.vendexpress;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.lacf.luisadrian.vendexpress.models.Cliente;
import com.lacf.luisadrian.vendexpress.models.Producto;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ProductoEdit extends AppCompatActivity {
    private String id;

    private TextView lblProductoId, lblProductoName, lblProductoPrice, lblProductoDescription;
    private ImageView imgProductoPhoto;
    private EditText txtProductoQuantity;
    private Producto producto;
    private SharedPreferences data;

    private static final String URL = "http://salesapi2016.azurewebsites.net/api/productos";
    private static final String KEY_ID = "id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_producto_edit);
        data = getSharedPreferences("shoppingCart", Context.MODE_PRIVATE);
        Bundle bundle = getIntent().getExtras();
        id = bundle.getString("id");
        lblProductoId = (TextView) findViewById(R.id.lbl_edit_producto_id);
        lblProductoName = (TextView) findViewById(R.id.lbl_edit_producto_name);
        lblProductoPrice = (TextView) findViewById(R.id.lbl_edit_producto_price);
        lblProductoDescription = (TextView) findViewById(R.id.lbl_edit_producto_description);
        imgProductoPhoto = (ImageView) findViewById(R.id.img_edit_producto_photo);
        txtProductoQuantity = (EditText) findViewById(R.id.txt_productos_shopp_quantity);
        lblProductoId.setText(id);
        try {
            getInfo();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Button btnAddShopping = (Button) findViewById(R.id.btn_productos_shopp_add);
        btnAddShopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToCart();
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

    private void getInfo() throws JSONException {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, URL+"?id="+id, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        ArrayList<Producto> infoProducto = new ArrayList<>();
                        infoProducto = parseJSON(response);
                        if (infoProducto.size() > 0) {
                            producto = new Producto(infoProducto.get(0).getId(), infoProducto.get(0).getName(), infoProducto.get(0).getDescription(), infoProducto.get(0).getPhoto(), infoProducto.get(0).getPrice(), infoProducto.get(0).getSeller());
                            lblProductoId.setText(infoProducto.get(0).getId());
                            lblProductoName.setText(infoProducto.get(0).getName());
                            lblProductoDescription.setText(infoProducto.get(0).getDescription());
                            Picasso.with(getApplicationContext()).load(infoProducto.get(0).getPhoto()).into(imgProductoPhoto);
                            lblProductoPrice.setText(infoProducto.get(0).getPrice());
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

    private void addToCart() {
        SharedPreferences.Editor editor = data.edit();
        String infoProducto = producto.getId()+";;";
        infoProducto += producto.getName()+";;";
        infoProducto += producto.getDescription()+";;";
        infoProducto += producto.getPhoto()+";;";
        infoProducto += producto.getPrice()+";;";
        infoProducto += txtProductoQuantity.getText().toString();
        editor.putString(id, infoProducto);
        Toast.makeText(this, "Producto agregado al carrito", Toast.LENGTH_SHORT).show();
        editor.apply();
    }
}
