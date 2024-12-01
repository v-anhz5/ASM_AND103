package fpoly.account.asm_and103.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import fpoly.account.asm_and103.activity.ViewDetailProductActivity;
import fpoly.account.asm_and103.model.Product;
import fpoly.account.asm_and103.R;
import fpoly.account.asm_and103.model.Response;
import fpoly.account.asm_and103.services.ApiServices;
import fpoly.account.asm_and103.services.HttpRequest;
import retrofit2.Call;
import retrofit2.Callback;

public class AddProductAdapter extends RecyclerView.Adapter<AddProductAdapter.ProductViewHolder> {

    public void setProductList(ArrayList<Product> productList) {
        this.productList = productList;
    }

    public ArrayList<Product> getProductList() {
        return productList;
    }

    public AddProductAdapter(ArrayList<Product> productList, Context context) {
        this.productList = productList;
        this.context = context;
    }

    public ArrayList<Product> productList;
    public Context context;



    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_addfruit, parent, false);
        return new ProductViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        Product product = productList.get(position);
        Glide.with(context).load(product.getProductImage()).into(holder.ivImage);
        holder.tvProductName.setText(product.getProductName());
        holder.tvPrice.setText("$" + product.getProductPrice());

        // Xử lý sự kiện click cho ImageView "Edit"
        holder.btnEdit.setOnClickListener(v -> {
            showEditProductDialog(position);
        });

        // Xử lý sự kiện click cho ImageView "Delete"
        holder.btnDelete.setOnClickListener(v -> {
            // Hiển thị dialog xác nhận xóa
            new AlertDialog.Builder(context)
                    .setTitle("Xác nhận xóa")
                    .setMessage("Bạn có chắc chắn muốn xóa sản phẩm này?")
                    .setPositiveButton("Xóa", (dialog, which) -> {
                        // Gọi API để xóa sản phẩm
                        ApiServices apiServices = new HttpRequest().callAPI();
                        apiServices.deleteFruitByProductId(product.getProductID()).enqueue(new Callback<Response<Void>>() {
                            @Override
                            public void onResponse(Call<Response<Void>> call, retrofit2.Response<Response<Void>> response) {
                                if (response.isSuccessful()) {
                                    // Xóa sản phẩm khỏi danh sách và cập nhật RecyclerView
                                    productList.remove(position);
                                    notifyItemRemoved(position);
                                    notifyDataSetChanged(); // Cập nhật lại toàn bộ RecyclerView
                                    Toast.makeText(context, "Xóa sản phẩm thành công", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(context, "Lỗi khi xóa sản phẩm", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<Response<Void>> call, Throwable t) {
                                Toast.makeText(context, "Lỗi kết nối server: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    })
                    .setNegativeButton("Hủy", null)
                    .show();
        });
    }

    private void showEditProductDialog(int position) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_edit_product, null);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();
        dialog.show();
        Product product = productList.get(position);

        // Ánh xạ các view trong dialog edit
        EditText edtProductID = dialogView.findViewById(R.id.edtProductID);
        EditText edtProductName = dialogView.findViewById(R.id.edtProductName);
        EditText edtProductImage = dialogView.findViewById(R.id.edtProductImage);
        EditText edtProductDescription = dialogView.findViewById(R.id.edtProductDescription);
        EditText edtProductPrice = dialogView.findViewById(R.id.edtProductPrice);
        Spinner spnCategory = dialogView.findViewById(R.id.spnCategory);
        Button btnUpdate = dialogView.findViewById(R.id.btnUpdate);

        // Khởi tạo giá trị cho các view
        edtProductID.setText(product.getProductID());
        edtProductName.setText(product.getProductName());
        edtProductImage.setText(product.getProductImage());
        edtProductDescription.setText(product.getProductDescription());
        edtProductPrice.setText(String.valueOf(product.getProductPrice()));

        // Khởi tạo giá trị cho Spinner (Category)
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context,
                R.array.categories_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnCategory.setAdapter(adapter);
        if(product.getCategoryid().equals("674967321c44585f0b4ab455")){
            spnCategory.setSelection(0);
        }else if(product.getCategoryid().equals("674967321c44585f0b4ab456")){
            spnCategory.setSelection(1);
        } else {
            spnCategory.setSelection(2);
        }

        // Xử lý sự kiện click button Update
        btnUpdate.setOnClickListener(v -> {
            // Lấy dữ liệu từ các view
            String productName = edtProductName.getText().toString();
            String productImage = edtProductImage.getText().toString();
            String productDescription = edtProductDescription.getText().toString();
            double productPrice = Double.parseDouble(edtProductPrice.getText().toString());

            // Lấy category từ Spinner
            String category = spnCategory.getSelectedItem().toString().trim();

            // Lấy categoryid dựa trên category được chọn từ Spinner
            String categoryId = "";
            switch (category) {
                case "Trái cây":
                    categoryId = "674967321c44585f0b4ab455";
                    break;
                case "Rau củ":
                    categoryId = "674967321c44585f0b4ab456";
                    break;
                case "Thịt":
                    categoryId = "674967321c44585f0b4ab457";
                    break;
            }

            // Cập nhật thông tin sản phẩm
            product.setProductName(productName);
            product.setProductImage(productImage);
            product.setProductDescription(productDescription);
            product.setProductPrice(productPrice);
            product.setCategoryid(categoryId);

            // Gọi API để cập nhật sản phẩm
            ApiServices apiServices = new HttpRequest().callAPI();
            apiServices.updateFruitByProductId(product.getProductID(), product).enqueue(new Callback<Response<Void>>() {
                @Override
                public void onResponse(Call<Response<Void>> call, retrofit2.Response<Response<Void>> response) {
                    if (response.isSuccessful()) {
                        // Cập nhật RecyclerView
                        notifyItemChanged(position);
                        Toast.makeText(context, "Cập nhật sản phẩm thành công", Toast.LENGTH_SHORT).show();
                        dialog.dismiss(); // Đóng dialog sau khi cập nhật thành công
                    } else {
                        Toast.makeText(context, "Lỗi khi cập nhật sản phẩm", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Response<Void>> call, Throwable t) {
                    Toast.makeText(context, "Lỗi kết nối server: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });

    }


    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {
        public ImageView ivImage, btnEdit, btnDelete; // Thêm ImageView cho "Edit" và "Delete"
        public TextView tvProductName;
        public TextView tvPrice;
        public CardView cvProduct;

        public ProductViewHolder(View view) {
            super(view);
            ivImage = view.findViewById(R.id.iv_image);
            cvProduct = view.findViewById(R.id.cvProduct);
            tvProductName = view.findViewById(R.id.tv_product_name);
            tvPrice = view.findViewById(R.id.tv_price);
            btnEdit = view.findViewById(R.id.btn_edit); // Ánh xạ ImageView "Edit"
            btnDelete = view.findViewById(R.id.btn_delete); // Ánh xạ ImageView "Delete"
        }
    }
}
