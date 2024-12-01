package fpoly.account.asm_and103.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import fpoly.account.asm_and103.R;
import fpoly.account.asm_and103.model.Product;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private List<Product> cartList;
    private Context context;

    public CartAdapter(List<Product> cartList, Context context) {
        this.cartList = cartList;
        this.context = context;
    }

    @Override
    public CartViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CartViewHolder holder, int position) {
        Product product = cartList.get(position);
        holder.tvCartItemName.setText(product.getProductName());
        holder.tvCartItemPrice.setText("$" + product.getProductPrice());
        Glide.with(context).load(product.getProductImage()).into(holder.ivCartItemImage);

        // Xử lý số lượng
        AtomicInteger quantity = new AtomicInteger(0); // Sử dụng AtomicInteger

        holder.btnIncreaseQuantity.setOnClickListener(v -> {
            int newQuantity = quantity.incrementAndGet(); // Tăng quantity và lấy giá trị mới
            holder.tvCartItemQuantity.setText(String.valueOf(newQuantity));
        });

        holder.btnDecreaseQuantity.setOnClickListener(v -> {
            int newQuantity = quantity.decrementAndGet(); // Giảm quantity và lấy giá trị mới
            if (newQuantity >= 0) { // Không giảm xuống dưới 0
                holder.tvCartItemQuantity.setText(String.valueOf(newQuantity));
            } else {
                quantity.incrementAndGet(); // Nếu newQuantity < 0, tăng lại quantity
            }
        });

        // Xử lý xóa item
        holder.btnRemoveCartItem.setOnClickListener(v -> {
            cartList.remove(position);
            notifyItemRemoved(position);
            // Cập nhật tổng giá trong ViewCartFragment
        });
    }


    @Override
    public int getItemCount() {
        return cartList.size();
    }

    public class CartViewHolder extends RecyclerView.ViewHolder {
        public ImageView ivCartItemImage, btnRemoveCartItem, btnIncreaseQuantity, btnDecreaseQuantity;
        public TextView tvCartItemName, tvCartItemPrice, tvCartItemQuantity;

        public CartViewHolder(View view) {
            super(view);
            ivCartItemImage = view.findViewById(R.id.ivCartItemImage);
            tvCartItemName = view.findViewById(R.id.tvCartItemName);
            tvCartItemPrice = view.findViewById(R.id.tvCartItemPrice);
            tvCartItemQuantity = view.findViewById(R.id.tvCartItemQuantity);
            btnIncreaseQuantity = view.findViewById(R.id.btn_increase); // Ánh xạ ImageView "Increase"
            btnDecreaseQuantity = view.findViewById(R.id.btn_decrease); // Ánh xạ ImageView "Decrease"
            btnRemoveCartItem = view.findViewById(R.id.btnRemoveCartItem);
        }
    }
}