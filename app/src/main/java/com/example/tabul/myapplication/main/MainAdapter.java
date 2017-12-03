package com.example.tabul.myapplication.main;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tabul.myapplication.R;
import com.example.tabul.myapplication.api.dao.MainDao;
import com.squareup.picasso.Picasso;

import java.util.List;


public class MainAdapter extends RecyclerView.Adapter<MainAdapter.MainHolder> {

    private List<MainDao> mData;

    public MainAdapter(List<MainDao> mData) {
        this.mData = mData;
    }

    @Override
    public MainAdapter.MainHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_main, parent, false);
        MainHolder holder = new MainHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MainHolder holder, int position) {
        holder.titleRow.setText(mData.get(position).getTitle());
        Picasso.with(holder.imageRow.getContext())
                .load(mData.get(position).getImageUrl())
                .into(holder.imageRow);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class MainHolder extends RecyclerView.ViewHolder {
        private ImageView imageRow;
        private TextView titleRow;

        public MainHolder(View itemView) {
            super(itemView);

            imageRow = itemView.findViewById(R.id.imageRow);
            titleRow = itemView.findViewById(R.id.titleRow);
        }
    }
}
