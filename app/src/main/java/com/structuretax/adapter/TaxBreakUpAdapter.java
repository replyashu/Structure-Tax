package com.structuretax.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.structuretax.R;
import com.structuretax.model.Components;
import com.structuretax.model.TaxComponents;

import java.util.List;

/**
 * Created by apple on 23/04/17.
 */

public class TaxBreakUpAdapter extends RecyclerView.Adapter<TaxBreakUpAdapter.ViewHolder>{

    private List<TaxComponents> components;
    private Context context;
    private int lastPosition = -1;


    public TaxBreakUpAdapter(List<TaxComponents> components){
        this.components = components;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        TaxBreakUpAdapter.ViewHolder viewHolder;
        context = parent.getContext();
        view = LayoutInflater.from(context).inflate(
                R.layout.fragment_tax_calculation_item, parent, false);
        viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.txtComponentName.setText(components.get(position).getComponentName());

        holder.txtMonthly.setText("₹ " + String.format("%.2f", components.get(position).getMonthly()));
        holder.txtYearly.setText("₹ " + String.format("%.2f",components.get(position).getYearly()));
        holder.txtTax.setText("₹ " + String.format("%.2f",components.get(position).getTax()));
        boolean isProvable = components.get(position).isProof();
        if(isProvable) {
            holder.imgProof.setVisibility(View.VISIBLE);
            holder.imgProof.setImageResource(R.drawable.proof);
        }
        else
            holder.imgProof.setVisibility(View.GONE);

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
        return components.size() - 2;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView txtComponentName;
        TextView txtMonthly;
        TextView txtYearly;
        TextView txtTax;
        ImageView imgProof;

        public ViewHolder(View itemView) {
            super(itemView);

            txtComponentName = (TextView) itemView.findViewById(R.id.txtComponent);
            txtMonthly = (TextView) itemView.findViewById(R.id.txtMonthly);
            txtYearly = (TextView) itemView.findViewById(R.id.txtYearly);
            txtTax = (TextView) itemView.findViewById(R.id.txtTaxComputed);
            imgProof = (ImageView) itemView.findViewById(R.id.imgProof);
        }

    }
}
