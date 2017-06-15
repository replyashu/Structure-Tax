package com.structuremytax.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.structuremytax.R;
import com.structuremytax.model.Components;

import java.util.List;

/**
 * Created by apple on 09/04/17.
 */

public class SalarySplitAdapter extends RecyclerView.Adapter<SalarySplitAdapter.ViewHolder>{

    private List<Components> components;
    private Context context;
    private int lastPosition = -1;


    public SalarySplitAdapter(List<Components> components){
        this.components = components;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        ViewHolder viewHolder;
        context = parent.getContext();
        view = LayoutInflater.from(context).inflate(
                R.layout.optimized_item, parent, false);
        viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.txtComponentName.setText(components.get(position).getComponentName());

        holder.txtMonthly.setText("₹ " + String.format("%.2f", components.get(position).getMonthly()));
        holder.txtYearly.setText("₹ " + String.format("%.2f",components.get(position).getYearly()));
        boolean isProvable = components.get(position).isProof();

        setAnimation(holder.itemView, position);

    }

    private void setAnimation(View viewToAnimate, int position)
    {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition)
        {
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.anticipateovershoot_interpolator);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    public int getItemCount() {
        return components.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView txtComponentName;
        TextView txtMonthly;
        TextView txtYearly;

        public ViewHolder(View itemView) {
            super(itemView);

            txtComponentName = (TextView) itemView.findViewById(R.id.txtComponent);
            txtMonthly = (TextView) itemView.findViewById(R.id.txtMonthly);
            txtYearly = (TextView) itemView.findViewById(R.id.txtYearly);
        }

    }
}
