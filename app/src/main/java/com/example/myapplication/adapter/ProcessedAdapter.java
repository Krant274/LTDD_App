package com.example.myapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.model.HoanTat;

import java.util.List;

public class ProcessedAdapter extends RecyclerView.Adapter<ProcessedAdapter.MyViewHolder> {
    private final List<HoanTat> hoanTatList;
    private final Context context;
    private OnClickListener onClickListener;

    public ProcessedAdapter(Context context, List<HoanTat> hoanTatList) {
        this.context = context;
        this.hoanTatList = hoanTatList;
    }

    @NonNull
    @Override
    public ProcessedAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_process, parent, false);
        return new ProcessedAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProcessedAdapter.MyViewHolder holder, int position) {
        HoanTat hoanTat = hoanTatList.get(position);
        holder.bind(hoanTat);
    }

    @Override
    public int getItemCount() {
        return hoanTatList.size();
    }

    public void setOnClickListener(ProcessedAdapter.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public interface OnClickListener {
        void onClick(int position, HoanTat model);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView itemId;

        public MyViewHolder(View itemView) {
            super(itemView);
            itemId = itemView.findViewById(R.id.id_processing);

            itemView.setOnClickListener(this);
        }

        public void bind(HoanTat hoanTat) {
            itemId.setText(String.valueOf(hoanTat.getId()));
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION && onClickListener != null) {
                onClickListener.onClick(position, hoanTatList.get(position));
            }
        }

    }
}
