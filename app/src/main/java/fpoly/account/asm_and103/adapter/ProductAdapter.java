package fpoly.account.asm_and103.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import fpoly.account.asm_and103.activity.CheckOutActivity;
import fpoly.account.asm_and103.activity.ViewDetailProductActivity;
import fpoly.account.asm_and103.fragment.ViewCartFragment;
import fpoly.account.asm_and103.model.Product;
import fpoly.account.asm_and103.R;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    public void setProductList(ArrayList<Product> productList) {
        this.productList = productList;
    }



    public ArrayList<Product> getProductList() {
        return productList;
    }

    public ProductAdapter(ArrayList<Product> productList, Context context) {
        this.productList = productList;
        this.context = context;
    }

    private ArrayList<Product> productList;
    private Context context;



    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_fruit, parent, false);
        return new ProductViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        Product product = productList.get(position);
        Glide.with(context).load(product.getProductImage()).into(holder.ivImage);
        holder.tvProductName.setText(product.getProductName());
//        holder.tvWeight.setText(product.getWeight()); // Gán weight
        holder.tvPrice.setText("$" + product.getProductPrice());

        holder.cvProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ViewDetailProductActivity.class);
                intent.putExtra("product", product);
                context.startActivity(intent);
            }
        });

        // Xử lý sự kiện click cho Button "Add to Cart" (nếu cần)
        holder.btnAddtoCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    Product selectedProduct = productList.get(position);

                    Intent intent = new Intent(context, CheckOutActivity.class);
                    intent.putExtra("item", selectedProduct);
                    Log.d("zzzzzz", "onBindViewHolder: "+selectedProduct);
                    context.startActivity(intent);
                }
            }
        });
    }


    private ViewCartFragment getViewCartFragment() {
        FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
        return (ViewCartFragment) fragmentManager.findFragmentById(R.id.fragmentContainer); // Thay R.id.fragmentContainer bằng id của FrameLayout chứa ViewCartFragment
    }
    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {
        public ImageView ivImage;
        public TextView tvProductName;
        public TextView tvWeight; // Thêm TextView cho weight
        public TextView tvPrice;
        public Button btnAddtoCart; // Thêm Button "Add to Cart"
        public CardView cvProduct;

        public ProductViewHolder(View view) {
            super(view);
            ivImage = view.findViewById(R.id.iv_image);
            cvProduct = view.findViewById(R.id.cvProduct);
            tvProductName = view.findViewById(R.id.tv_product_name);
            tvWeight = view.findViewById(R.id.tv_weight); // Ánh xạ TextView cho weight
            tvPrice = view.findViewById(R.id.tv_price);
            btnAddtoCart = view.findViewById(R.id.btn_add_to_cart); // Ánh xạ Button "Add to Cart"
        }
    }
}
