package fpoly.account.asm_and103.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;

import java.util.concurrent.atomic.AtomicInteger;

import fpoly.account.asm_and103.R;
import fpoly.account.asm_and103.model.Product;

public class ViewDetailProductActivity extends AppCompatActivity {

    ImageView ivProductImage;
    TextView tvProductName, tvPrice, tvNumReviews, tvWeight, tvDescription;
    RatingBar ratingBar;
    Button btnAddToCart, btnAddToFavorite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view_detail_product);

        // Ánh xạ các view
        ivProductImage = findViewById(R.id.iv_product_image);
        tvProductName = findViewById(R.id.tv_product_name);
        tvPrice = findViewById(R.id.tv_price);
        tvNumReviews = findViewById(R.id.tv_num_reviews);
        tvWeight = findViewById(R.id.tv_weight);
        tvDescription = findViewById(R.id.tv_description);
        ratingBar = findViewById(R.id.ratingBar);
        btnAddToCart = findViewById(R.id.btn_add_to_cart);
        btnAddToFavorite = findViewById(R.id.btn_add_to_favorite);
        TextView tvQuantity = findViewById(R.id.tv_quantity);
        Button btnDecrease = findViewById(R.id.btn_decrease);
        Button btnIncrease = findViewById(R.id.btn_increase);

        // Nhận dữ liệu Product từ Intent
        Product product = getIntent().getParcelableExtra("product");

        // Hiển thị thông tin sản phẩm lên các view
        if (product != null) {
            Glide.with(this).load(product.getProductImage()).into(ivProductImage);
            tvProductName.setText(product.getProductName());
            tvPrice.setText("$" + product.getProductPrice());
//            tvNumReviews.setText("(" + product.getNumReviews() + " reviews)");
//            tvWeight.setText(product.getWeight());
            tvDescription.setText(product.getProductDescription());
//            ratingBar.setRating(product.getRating());


            AtomicInteger quantity = new AtomicInteger(0); // Sử dụng AtomicInteger

            tvQuantity.setText(String.valueOf(quantity.get()));

            btnIncrease.setOnClickListener(v -> {
                int newQuantity = quantity.incrementAndGet(); // Tăng quantity và lấy giá trị mới
                tvQuantity.setText(String.valueOf(newQuantity));
            });

            btnDecrease.setOnClickListener(v -> {
                int newQuantity = quantity.decrementAndGet(); // Giảm quantity và lấy giá trị mới
                if (newQuantity >= 0) { // Không giảm xuống dưới 0
                    tvQuantity.setText(String.valueOf(newQuantity));
                } else {
                    quantity.incrementAndGet(); // Nếu newQuantity < 0, tăng lại quantity
                }
            });
            // Xử lý sự kiện click cho Button "Add to Cart"
            btnAddToCart.setOnClickListener(v -> {
                Intent intent = new Intent(ViewDetailProductActivity.this, CheckOutActivity.class);

                // Gửi dữ liệu sản phẩm và số lượng sang CheckOutActivity
                intent.putExtra("product", product);

                // Chuyển sang CheckOutActivity
                startActivity(intent);
            });

            // Xử lý sự kiện click cho Button "Add to Favorite"
            btnAddToFavorite.setOnClickListener(v -> {
                // Xử lý sự kiện thêm vào danh sách yêu thích
            });
        }
    }

}