package com.lacf.luisadrian.vendexpress.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.lacf.luisadrian.vendexpress.R;
import com.lacf.luisadrian.vendexpress.fragments.Carrito;
import com.lacf.luisadrian.vendexpress.fragments.Productos;
import com.lacf.luisadrian.vendexpress.models.Producto;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Creado por LuisAdrian el 27/11/2016.
 */

public class CarritoAdapter extends RecyclerView.Adapter<CarritoAdapter.ViewHolderCarrito> {
    private ArrayList<Producto> productos = new ArrayList<>();
    private LayoutInflater layoutInflater;
    private SharedPreferences data;
    private Context context;
    private Carrito carrito;

    public CarritoAdapter(Context context, Carrito carrito) {
        layoutInflater = LayoutInflater.from(context);
        data = context.getSharedPreferences("shoppingCart", Context.MODE_PRIVATE);
        this.context = context;
        this.carrito = carrito;
    }

    @Override
    public ViewHolderCarrito onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.layout_carrito, parent, false);
        ViewHolderCarrito viewHolderCarrito = new ViewHolderCarrito(view);
        return viewHolderCarrito;
    }

    @Override
    public void onBindViewHolder(final ViewHolderCarrito holder, int position) {
        final Integer pos = position;
        Producto currentProducto = productos.get(position);
        holder.lblId.setText(currentProducto.getId());
        holder.lblName.setText(currentProducto.getName());
        holder.lblPrice.setText(currentProducto.getPrice());
        holder.lblQuantity.setText(currentProducto.getQuantity());
        final Context context = holder.imgPhoto.getContext();
        Picasso.with(context).load(currentProducto.getPhoto()).into(holder.imgPhoto);
        holder.btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = data.edit();
                String id = productos.get(pos).getId();
                editor.remove(id);
                editor.apply();
                carrito.showShoppingCart();
            }
        });
    }

    @Override
    public int getItemCount() {
        return productos.size();
    }

    public void setProductos(ArrayList<Producto> productos) {
        this.productos = productos;
        notifyDataSetChanged();
    }

    static class ViewHolderCarrito extends RecyclerView.ViewHolder {

        TextView lblId;
        TextView lblName;
        TextView lblQuantity;
        TextView lblPrice;
        ImageView imgPhoto;
        ImageButton btnEliminar;
        Context context;
        CardView cardView;

        public ViewHolderCarrito(View itemView) {

            super(itemView);
            context = itemView.getContext();
            cardView = (CardView) itemView.findViewById(R.id.card_view_carrito);
            lblId = (TextView) itemView.findViewById(R.id.lbl_shopping_producto_id);
            lblName = (TextView) itemView.findViewById(R.id.lbl_shopping_producto_name);
            lblQuantity = (TextView) itemView.findViewById(R.id.lbl_shopping_producto_quantity);
            lblPrice = (TextView) itemView.findViewById(R.id.lbl_shopping_producto_price);
            imgPhoto = (ImageView) itemView.findViewById(R.id.img_shopping_producto_photo);
            btnEliminar = (ImageButton) itemView.findViewById(R.id.btn_shopping_eliminar);
        }
    }
}
