package com.lacf.luisadrian.vendexpress.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lacf.luisadrian.vendexpress.ClienteEdit;
import com.lacf.luisadrian.vendexpress.R;
import com.lacf.luisadrian.vendexpress.models.Cliente;

import java.util.ArrayList;

/**
 * Creado por LuisAdrian el 24/11/2016.
 */

public class ClientesAdapter extends RecyclerView.Adapter<ClientesAdapter.ViewHolderClientes> {
    private ArrayList<Cliente> clientes = new ArrayList<>();
    private LayoutInflater layoutInflater;

    public ClientesAdapter(Context context) {

        layoutInflater = LayoutInflater.from(context);

    }

    public void setClientes(ArrayList<Cliente> clientes) {

        this.clientes = clientes;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolderClientes onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.layout_clientes, parent, false);
        ViewHolderClientes viewHolderClientes = new ViewHolderClientes(view);
        return viewHolderClientes;
    }

    @Override
    public void onBindViewHolder(final ViewHolderClientes holder, int position) {
        final Integer pos = position;
        Cliente currentCliente = clientes.get(position);
        holder.lblId.setText(currentCliente.getId());
        holder.lblName.setText(currentCliente.getName());
        holder.lblEmail.setText(currentCliente.getEmail());
        holder.lblPhone.setText(currentCliente.getPhone());
        holder.lblAddress.setText(currentCliente.getAddress());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.context, ClienteEdit.class);
                String id = clientes.get(pos).getId();
                if (id == null)
                    id = "-1";
                intent.putExtra("id", id);
                holder.context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {

        return clientes.size();
    }

    static class ViewHolderClientes extends RecyclerView.ViewHolder {

        TextView lblId;
        TextView lblName;
        TextView lblEmail;
        TextView lblPhone;
        TextView lblAddress;
        Context context;
        CardView cardView;

        public ViewHolderClientes(View itemView) {

            super(itemView);
            context = itemView.getContext();
            cardView = (CardView) itemView.findViewById(R.id.card_view_clientes);
            lblId = (TextView) itemView.findViewById(R.id.lbl_show_cliente_id);
            lblName = (TextView) itemView.findViewById(R.id.lbl_show_cliente_name);
            lblEmail = (TextView) itemView.findViewById(R.id.lbl_show_cliente_email);
            lblPhone = (TextView) itemView.findViewById(R.id.lbl_show_cliente_phone);
            lblAddress = (TextView) itemView.findViewById(R.id.lbl_show_cliente_address);
        }
    }
}
