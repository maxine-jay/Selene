package com.example.selene.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.selene.Models.DailyInput;
import com.example.selene.R;

import java.util.ArrayList;

public class DailyInputRecyclerAdapter extends RecyclerView.Adapter<DailyInputRecyclerAdapter.ViewHolder> {

        private ArrayList<DailyInput> mDailyInputs = new ArrayList<>();

    public DailyInputRecyclerAdapter(ArrayList<DailyInput> dailyInputs) {
            this.mDailyInputs = dailyInputs;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_daily_data_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int i) {

            holder.date.setText(mDailyInputs.get(i).getDate());
            holder.bleeding.setText(mDailyInputs.get(i).getBleeding());
            holder.emotion.setText(mDailyInputs.get(i).getEmotion());
            holder.physical.setText(mDailyInputs.get(i).getPhysicalFeeling());

        }

        @Override
        public int getItemCount() {

            return mDailyInputs.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView date, bleeding, emotion, physical;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                //sets views to textviews in item layout using their ids
                date = itemView.findViewById(R.id.item_date);
                bleeding = itemView.findViewById(R.id.item_bleeding);
                emotion = itemView.findViewById(R.id.item_emotion);
                physical = itemView.findViewById(R.id.item_physical);

            }
        }
}
