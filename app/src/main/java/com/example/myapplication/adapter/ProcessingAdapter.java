package com.example.myapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.model.DangXuLi;

import java.util.List;

public class ProcessingAdapter extends RecyclerView.Adapter<ProcessingAdapter.MyViewHolder> {
    private final List<DangXuLi> dangXuLiList;

    private final Context context;
    private OnClickListener onClickListener;

    public ProcessingAdapter(Context context, List<DangXuLi> dangXuLiList) {
        this.context = context;
        this.dangXuLiList = dangXuLiList;
    }

    @NonNull
    @Override
    public ProcessingAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_process, parent, false);
        return new ProcessingAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProcessingAdapter.MyViewHolder holder, int position) {
        DangXuLi dangXuLi = dangXuLiList.get(position);

        holder.bind(dangXuLi);
    }

    @Override
    public int getItemCount() {
        return dangXuLiList.size();
    }

    public void setOnClickListener(ProcessingAdapter.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public interface OnClickListener {
        void onClick(int position, DangXuLi model);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView itemId;

        public MyViewHolder(View itemView) {
            super(itemView);
            itemId = itemView.findViewById(R.id.id_processing);
            itemView.setOnClickListener(this);
        }

        public void bind(DangXuLi dangXuLi) {
            itemId.setText(String.valueOf(dangXuLi.getId()));
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION && onClickListener != null) {
                onClickListener.onClick(position, dangXuLiList.get(position));
            }
        }

    }
}
