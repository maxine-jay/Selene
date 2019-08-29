package com.example.selene.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.selene.R;
import com.example.selene.models.DailyInput;

import java.util.ArrayList;
import java.util.Date;

public class DailyInputRecyclerAdapter extends RecyclerView.Adapter<DailyInputRecyclerAdapter.ViewHolder> {

    private ArrayList<DailyInput> mDailyInputs;
    private OnDailyInputListener mOnDailyInputListener;

    public DailyInputRecyclerAdapter(ArrayList<DailyInput> dailyInputs, OnDailyInputListener onDailyInputListener) {
        this.mDailyInputs = dailyInputs;
        this.mOnDailyInputListener = onDailyInputListener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_daily_input_item, parent, false);
        return new ViewHolder(view, mOnDailyInputListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
        Date date = mDailyInputs.get(i).getDate();
        String formattedDate = DailyInput.formatDateToString(date);

        holder.date.setText(formattedDate);
        if (mDailyInputs.get(i).getBleeding().equals("Bleeding")) {
            holder.bleedingIndicator.setVisibility(View.VISIBLE);
        } else {
            holder.bleedingIndicator.setVisibility(View.GONE);

        }


    }

    @Override
    public int getItemCount() {

        return mDailyInputs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView date, bleeding, emotion, physical;
        ImageView bleedingIndicator;
        OnDailyInputListener onDailyInputListener;

        public ViewHolder(@NonNull View itemView, OnDailyInputListener onDailyInputListener) {
            super(itemView);
            //sets views to textviews in item layout using their ids
            date = itemView.findViewById(R.id.item_date);
            bleedingIndicator = itemView.findViewById(R.id.bleeding_indicator);
            this.onDailyInputListener = onDailyInputListener;
            itemView.setOnClickListener(this);


        }

        @Override
        public void onClick(View view) {

            onDailyInputListener.onDailyInputClick(getAdapterPosition());

        }
    }

    public interface OnDailyInputListener {
        void onDailyInputClick(int position);
    }
}
