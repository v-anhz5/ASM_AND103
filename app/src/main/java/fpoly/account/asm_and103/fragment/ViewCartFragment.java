package fpoly.account.asm_and103.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import fpoly.account.asm_and103.R;
import fpoly.account.asm_and103.activity.CheckOutActivity;
import fpoly.account.asm_and103.adapter.CartAdapter;
import fpoly.account.asm_and103.model.Product;

public class ViewCartFragment extends Fragment {

    RecyclerView rvCartItems;
    TextView tvTotalPrice;
    Button btnCheckout;
    List<Product> cartList;
    CartAdapter cartAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_cart, container, false);

        rvCartItems = view.findViewById(R.id.rvCartItems);
        tvTotalPrice = view.findViewById(R.id.tvTotalPrice);
        btnCheckout = view.findViewById(R.id.btnCheckout);

        cartList = new ArrayList<>(); // Khởi tạo danh sách giỏ hàng
        cartAdapter = new CartAdapter(cartList, getContext()); // Khởi tạo CartAdapter
        rvCartItems.setLayoutManager(new LinearLayoutManager(getContext()));
//        rvCartItems.setAdapter(cartAdapter); // Gán adapter cho RecyclerView

        // Xử lý sự kiện click button Checkout (nếu cần)
        btnCheckout.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), CheckOutActivity.class);
            startActivity(intent);
        });

        return view;
    }

    // Phương thức để thêm sản phẩm vào giỏ hàng
    public void addProductToCart(Product product) {
        cartList.add(product);
        cartAdapter.notifyItemInserted(cartList.size() - 1); // Cập nhật RecyclerView
        updateTotalPrice(); // Cập nhật tổng giá
    }

    // Phương thức để cập nhật tổng giá
    private void updateTotalPrice() {
        double totalPrice = 0;
        for (Product product : cartList) {
            totalPrice += product.getProductPrice();
        }
        tvTotalPrice.setText(String.format("$%.2f", totalPrice));
    }
}