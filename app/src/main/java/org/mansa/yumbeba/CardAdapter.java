package org.mansa.yumbeba;

/**
 * Created by mansa on 12/7/15.
 */
import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {

    List<PopulateOrdersCard> mOrderItems;
    Activity activity;

    public CardAdapter(Activity activity, List<PopulateOrdersCard> mOrderItems) {
        this.activity = activity;
        this.mOrderItems = mOrderItems;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.order_card, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        PopulateOrdersCard orderItem = mOrderItems.get(i);
        viewHolder.tvOriginRestaurant.setText(orderItem.getRestaurant());
        viewHolder.tvDestinationConsumer.setText(orderItem.getDestination());
    }

    @Override
    public int getItemCount() {
        return mOrderItems.size();
    }


    public Object getItem(int location) {
        return mOrderItems.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public TextView tvOriginRestaurant;
        public TextView tvDestinationConsumer;

        public ViewHolder(View itemView) {
            super(itemView);
            tvOriginRestaurant = (TextView)itemView.findViewById(R.id.origin_order_txt);
            tvDestinationConsumer = (TextView)itemView.findViewById(R.id.destination_order_txt);
        }
    }
}
