package com.lacf.luisadrian.vendexpress.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lacf.luisadrian.vendexpress.ClienteEdit;
import com.lacf.luisadrian.vendexpress.ProductoEdit;
import com.lacf.luisadrian.vendexpress.R;
import com.lacf.luisadrian.vendexpress.models.Producto;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Creado por LuisAdrian el 26/11/2016.
 */

public class ProductosAdapter extends RecyclerView.Adapter<ProductosAdapter.ViewHolderProductos> {
    private ArrayList<Producto> productos = new ArrayList<>();
    private LayoutInflater layoutInflater;

    public ProductosAdapter(Context context) {
        layoutInflater = LayoutInflater.from(context);
    }

    public void setProductos(ArrayList<Producto> productos) {
        this.productos = productos;
        notifyDataSetChanged();
    }

    @Override
    public ProductosAdapter.ViewHolderProductos onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.layout_productos, parent, false);
        ViewHolderProductos viewHolderProductos = new ViewHolderProductos(view);
        return viewHolderProductos;
    }

    @Override
    public void onBindViewHolder(final ProductosAdapter.ViewHolderProductos holder, int position) {
        final Integer pos = position;
        Producto currentProducto = productos.get(position);
        holder.lblId.setText(currentProducto.getId());
        holder.lblName.setText(currentProducto.getName());
        holder.lblDescription.setText(currentProducto.getDescription());
        holder.lblPrice.setText(currentProducto.getPrice());
        Context context = holder.imgPhoto.getContext();
        Picasso.with(context).load(currentProducto.getPhoto()).into(holder.imgPhoto);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.context, ProductoEdit.class);
                String id = productos.get(pos).getId();
                if (id == null)
                    id = "-1";
                intent.putExtra("id", id);
                holder.context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return productos.size();
    }

    static class ViewHolderProductos extends RecyclerView.ViewHolder {

        TextView lblId;
        TextView lblName;
        TextView lblDescription;
        TextView lblPrice;
        ImageView imgPhoto;
        Context context;
        CardView cardView;

        public ViewHolderProductos(View itemView) {

            super(itemView);
            context = itemView.getContext();
            cardView = (CardView) itemView.findViewById(R.id.card_view_productos);
            lblId = (TextView) itemView.findViewById(R.id.lbl_show_producto_id);
            lblName = (TextView) itemView.findViewById(R.id.lbl_show_producto_name);
            lblDescription = (TextView) itemView.findViewById(R.id.lbl_show_producto_description);
            lblPrice = (TextView) itemView.findViewById(R.id.lbl_show_producto_price);
            imgPhoto = (ImageView) itemView.findViewById(R.id.img_show_producto_photo);
        }
    }
}
