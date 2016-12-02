package com.lacf.luisadrian.vendexpress.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.lacf.luisadrian.vendexpress.PagarAbono;
import com.lacf.luisadrian.vendexpress.R;
import com.lacf.luisadrian.vendexpress.models.Abono;

import java.util.ArrayList;

/**
 * Creado por LuisAdrian el 30/11/2016.
 */

public class AbonosAdapter extends RecyclerView.Adapter<AbonosAdapter.ViewHolderAbonos> {

    private ArrayList<Abono> abonos = new ArrayList<>();
    private LayoutInflater layoutInflater;

    public AbonosAdapter(Context context) {
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolderAbonos onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.layout_abono, parent, false);
        ViewHolderAbonos viewHolderAbonos = new ViewHolderAbonos(view);
        return viewHolderAbonos;
    }

    @Override
    public void onBindViewHolder(final ViewHolderAbonos holder, int position) {
        final Integer pos = position;
        Abono currentAbono = abonos.get(position);
        holder.lblId.setText(currentAbono.getId());
        holder.lblPayment.setText(currentAbono.getPayment());
        String formatedDate = "";
        String[] dateTime = currentAbono.getDate().split("T");
        if (dateTime.length > 1) {
            String[] time = dateTime[1].split("Z");
            dateTime[1] = time[0];
            formatedDate = dateTime[0] + " " + dateTime[1];
        }
        holder.lblDate.setText(formatedDate);
        holder.lblCustomerName.setText(currentAbono.getCustomerName());
        holder.lblCustomerAddress.setText(currentAbono.getCustomerAddress());
        holder.lblCustomerPhone.setText(currentAbono.getCustomerPhone());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.context, PagarAbono.class);
                String id = abonos.get(pos).getId();
                if (id == null)
                    id = "-1";
                intent.putExtra("id", id);
                holder.context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return abonos.size();
    }

    public void setAbonos(ArrayList<Abono> abonos) {
        this.abonos = abonos;
        notifyDataSetChanged();
    }

    static class ViewHolderAbonos extends RecyclerView.ViewHolder {

        TextView lblId;
        TextView lblPayment;
        TextView lblDate;
        TextView lblCustomerName;
        TextView lblCustomerAddress;
        TextView lblCustomerPhone;
        Context context;
        CardView cardView;

        public ViewHolderAbonos(View itemView) {
            super(itemView);
            context = itemView.getContext();
            cardView = (CardView) itemView.findViewById(R.id.card_view_abonos);
            lblId = (TextView) itemView.findViewById(R.id.lbl_abonos_id);
            lblPayment = (TextView) itemView.findViewById(R.id.lbl_abonos_payment);
            lblDate = (TextView) itemView.findViewById(R.id.lbl_abonos_date);
            lblCustomerName = (TextView) itemView.findViewById(R.id.lbl_abonos_customerName);
            lblCustomerAddress = (TextView) itemView.findViewById(R.id.lbl_abonos_customerAddress);
            lblCustomerPhone = (TextView) itemView.findViewById(R.id.lbl_abonos_customerPhone);
        }
    }
}
