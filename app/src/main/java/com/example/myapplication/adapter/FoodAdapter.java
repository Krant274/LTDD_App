package com.example.myapplication.adapter;

import android.content.Context;
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
import com.example.myapplication.model.Drink;
import com.example.myapplication.model.Food;

import java.util.List;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.MyViewHolder> {

    private  List<Food> foodList;
    private  Context context;
    private OnClickListener onClickListener;

    private boolean isFoodnDrinkFragment;

    private int stt=1;
    public void setFilteredList(List<Food> filteredList) {
        this.foodList = filteredList;
        notifyDataSetChanged();
    }

    public FoodAdapter(Context context, List<Food> foodList, boolean isFoodnDrinkFragment) {
        this.context = context;
        this.foodList = foodList;
        this.isFoodnDrinkFragment = isFoodnDrinkFragment;
    }
    public FoodAdapter(Context context, List<Food> foodList) {
        this.context = context;
        this.foodList = foodList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_menu, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Food food = foodList.get(position);

        // Lấy giá trị từ SharedPreferences
        int quantity = SharedPrefManager.getInstance(context).loadFoodQuantity(food.getId());
        food.setQuantity(quantity);

        if (isFoodnDrinkFragment) {
            holder.itemAmount.setVisibility(View.GONE);
            holder.tvAmount.setVisibility(View.GONE);// Hide itemAmount in FoodnDrinkFragment
        }

        // Thiết lập giá trị ban đầu cho EditText
        holder.bind(food, position);
        holder.itemAmount.setText(String.valueOf(quantity));
        //holder.imageView.setImageResource(food.getImageResource());
        Glide.with(context).load(food.getUrlImage()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        if (foodList == null ) {
            return 0;
        } else return foodList.size();
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public interface OnClickListener {
        void onClick(int position, Food model);
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

                // Cập nhật giá trị trong danh sách `Food`
                foodList.get(getAdapterPosition()).setQuantity(newQuantity);
                // Lưu giá trị mới vào `SharedPreferences`
                SharedPrefManager.getInstance(context).saveFoodQuantity(foodList.get(getAdapterPosition()).getId(), newQuantity);
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

        public void bind(Food food, int position) {
            itemId.setText(String.valueOf(position+1));
            itemName.setText(food.getName());
            itemPrice.setText(String.valueOf(food.getPrice()));
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION && onClickListener != null) {
                onClickListener.onClick(position, foodList.get(position));
            }
        }
    }

}
