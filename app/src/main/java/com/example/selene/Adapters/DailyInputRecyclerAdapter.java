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

/**
An Adapter class is necessary when using a RecyclerView
This binds data from the mDailyInputs array to the views that are displayed in the RecyclerView on the MainActivity
It uses the layout_daily_input_item layout when creating each view
 */

public class DailyInputRecyclerAdapter extends RecyclerView.Adapter<DailyInputRecyclerAdapter.ViewHolder> {

    private ArrayList<DailyInput> mDailyInputs;
    private OnDailyInputListener mOnDailyInputListener;
    private static final String IS_BLEEDING = "Bleeding";

    public DailyInputRecyclerAdapter(ArrayList<DailyInput> dailyInputs, OnDailyInputListener onDailyInputListener) {
        this.mDailyInputs = dailyInputs;
        this.mOnDailyInputListener = onDailyInputListener;
    }

    //this is called when the RecyclerView needs a view to represent a daily input: called for every item in mDailyInputs
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_daily_input_item, parent, false);
        return new ViewHolder(view, mOnDailyInputListener);
    }
    //this is called by RecyclerView to display data at a specified position
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
        Date date = mDailyInputs.get(i).getDate();
        String formattedDate = DailyInput.formatDateToString(date);

        holder.date.setText(formattedDate);
        if (mDailyInputs.get(i).getBleeding().equals(IS_BLEEDING)) {
            holder.bleedingIndicator.setVisibility(View.VISIBLE);
        } else {
            holder.bleedingIndicator.setVisibility(View.GONE);

        }
    }

    //returns the total number of items in the data set which is held by the adapter
    @Override
    public int getItemCount() {

        return mDailyInputs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView date;
        ImageView bleedingIndicator;
        OnDailyInputListener onDailyInputListener;

        public ViewHolder(@NonNull View itemView, OnDailyInputListener onDailyInputListener) {
            super(itemView);
            //sets view to textview in item layout using their id
            date = itemView.findViewById(R.id.item_date);
            bleedingIndicator = itemView.findViewById(R.id.bleeding_indicator);
            this.onDailyInputListener = onDailyInputListener;
            itemView.setOnClickListener(this);


        }

        //gets position of item which is clicked on using the Listener
        @Override
        public void onClick(View view) {

            onDailyInputListener.onDailyInputClick(getAdapterPosition());

        }
    }

    //Listener for RecyclerView
    public interface OnDailyInputListener {
        void onDailyInputClick(int position);
    }
}
