package com.example.myapplication.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.SharedPrefManager;
import com.example.myapplication.fragment.FoodnDrinkFragment_setting;
import com.example.myapplication.model.Drink;
import com.example.myapplication.model.Food;

import java.util.List;

public class DrinkAdapter extends RecyclerView.Adapter<DrinkAdapter.MyViewHolder> {

    private List<Drink> drinkList;
    private  Context context;
    private OnClickListener onClickListener;
    private boolean isFoodnDrinkFragment;

    public void setFilteredList2(List<Drink> filteredList2) {
        this.drinkList = filteredList2;
        notifyDataSetChanged();
    }

    public DrinkAdapter(Context context, List<Drink> drinkList, boolean isFoodnDrinkFragment) {
        this.context = context;
        this.drinkList = drinkList;
        this.isFoodnDrinkFragment = isFoodnDrinkFragment;
    }
    public DrinkAdapter(Context context, List<Drink> drinkList) {
        this.context = context;
        this.drinkList = drinkList;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_menu, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Drink drink = drinkList.get(position);

        // Lấy giá trị từ SharedPreferences
        int quantity = SharedPrefManager.getInstance(context).loadDrinkQuantity(drink.getId());
        drink.setQuantity(quantity);

        if (isFoodnDrinkFragment) {
            holder.itemAmount.setVisibility(View.GONE);
            holder.tvAmount.setVisibility(View.GONE);// Hide itemAmount in FoodnDrinkFragment
        }

        // Thiết lập giá trị ban đầu cho EditText
        holder.bind(drink, position);
        holder.itemAmount.setText(String.valueOf(quantity));
        // Load hình ảnh từ URL và hiển thị lên ImageView bằng Glide
        Glide.with(context).load(drink.getUrlImage()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        if (drinkList == null ) {
            return 0;
        } else return drinkList.size();
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public interface OnClickListener {
        void onClick(int position, Drink model);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView imageView;
        private final TextView itemId, itemName, itemPrice, tvAmount;
        private final EditText itemAmount;
        private final TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Không cần thực hiện gì trước khi văn bản thay đổi
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Không cần thực hiện gì khi văn bản thay đổi
            }

            @Override
            public void afterTextChanged(Editable s) {
                String quantityStr = s.toString().trim();
                if (quantityStr.isEmpty()) {
                    itemAmount.setText("0");
                    quantityStr = "0";
                }
                int newQuantity = Integer.parseInt(quantityStr);

                // Cập nhật giá trị trong danh sách `Drink`
                drinkList.get(getAdapterPosition()).setQuantity(newQuantity);

                // Lưu giá trị mới vào `SharedPreferences`
                SharedPrefManager.getInstance(context).saveDrinkQuantity(drinkList.get(getAdapterPosition()).getId(), newQuantity);
            }
        };

        public MyViewHolder(View itemView) {
            super(itemView);
            itemId = itemView.findViewById(R.id.item_id);
            itemName = itemView.findViewById(R.id.item_name);
            itemPrice = itemView.findViewById(R.id.item_price);
            tvAmount = itemView.findViewById(R.id.tvAmount);
            itemAmount = itemView.findViewById(R.id.item_amount);
            itemAmount.addTextChangedListener(textWatcher);
            imageView = itemView.findViewById(R.id.imageView);
            itemView.setOnClickListener(this);
        }

        public void bind(Drink drink, int position) {
            itemId.setText(String.valueOf(position+1));
            itemName.setText(drink.getName());
            itemPrice.setText(String.valueOf(drink.getPrice()));
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION && onClickListener != null) {
                onClickListener.onClick(position, drinkList.get(position));
            }

            if (itemAmount.getText().toString().equals("0")) {
                itemAmount.setText(null);
            }


        }
    }

}
