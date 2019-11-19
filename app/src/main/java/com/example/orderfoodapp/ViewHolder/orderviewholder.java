package com.example.orderfoodapp.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.orderfoodapp.Interface.ItemClickListener;
import com.example.orderfoodapp.R;

public class orderviewholder extends RecyclerView.ViewHolder implements View.OnClickListener {


    public TextView txtOrderId,txtOrderStatus,txtOrderPhone,txtOrderAddress;

    private ItemClickListener itemClickListener;






    public orderviewholder(@NonNull View itemView) {
        super(itemView);
        txtOrderId = (TextView)itemView.findViewById(R.id.order_id);

        txtOrderStatus = (TextView)itemView.findViewById(R.id.status);

        txtOrderPhone = (TextView)itemView.findViewById(R.id.order_phone);

        txtOrderAddress = (TextView)itemView.findViewById(R.id.order_address);


        itemView.setOnClickListener(this);

    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View v)

    {

        itemClickListener.onClick(v,getAdapterPosition(),false);
    }
}
