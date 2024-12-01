package fpoly.account.asm_and103.activity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import fpoly.account.asm_and103.R;
import fpoly.account.asm_and103.adapter.AddProductAdapter;
import fpoly.account.asm_and103.adapter.ProductAdapter;
import fpoly.account.asm_and103.model.Product;
import fpoly.account.asm_and103.model.Response;
import fpoly.account.asm_and103.services.ApiServices;
import fpoly.account.asm_and103.services.HttpRequest;
import retrofit2.Call;
import retrofit2.Callback;

public class AddProductActivity extends AppCompatActivity {

    RecyclerView rcvFruits, rcvVegetables, rcvMeats;
    AddProductAdapter fruitAdapter, vegetableAdapter, meatAdapter;
    FloatingActionButton fabAddProduct;
    ApiServices apiServices;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        // Ánh xạ các view
        rcvFruits = findViewById(R.id.rcvFruits);
        rcvVegetables = findViewById(R.id.rcvVegetables);
        rcvMeats = findViewById(R.id.rcvMeats);
        fabAddProduct = findViewById(R.id.fabAddProduct);
        toolbar = findViewById(R.id.toolbar);

        // Thiết lập Toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Hiển thị nút back
        toolbar.setNavigationOnClickListener(v -> finish()); // Xử lý sự kiện click nút back

        // Khởi tạo LayoutManager cho các RecyclerView
        LinearLayoutManager layoutManagerFruits = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager layoutManagerVegetables = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager layoutManagerMeats = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rcvFruits.setLayoutManager(layoutManagerFruits);
        rcvVegetables.setLayoutManager(layoutManagerVegetables);
        rcvMeats.setLayoutManager(layoutManagerMeats);

        // Khởi tạo Adapter cho các RecyclerView
        fruitAdapter = new AddProductAdapter(new ArrayList<>(), this);
        vegetableAdapter = new AddProductAdapter(new ArrayList<>(), this);
        meatAdapter = new AddProductAdapter(new ArrayList<>(), this);
        rcvFruits.setAdapter(fruitAdapter);
        rcvVegetables.setAdapter(vegetableAdapter);
        rcvMeats.setAdapter(meatAdapter);

        // Khởi tạo ApiServices
        apiServices = new HttpRequest().callAPI();

        // Tải dữ liệu sản phẩm
        loadProducts();

        // Xử lý sự kiện click fabAddProduct
        fabAddProduct.setOnClickListener(v -> showAddProductDialog());
    }

    private void loadProducts() {
        apiServices.getAllListFruit().enqueue(new Callback<Response<ArrayList<Product>>>() {
            @Override
            public void onResponse(Call<Response<ArrayList<Product>>> call, retrofit2.Response<Response<ArrayList<Product>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ArrayList<Product> productList = response.body().getData();

                    // Phân loại sản phẩm theo category
                    ArrayList<Product> fruitList = new ArrayList<>();
                    ArrayList<Product> vegetableList = new ArrayList<>();
                    ArrayList<Product> meatList = new ArrayList<>();
                    for (Product product : productList) {
                        switch (product.getCategoryid()) {
                            case "674967321c44585f0b4ab455":
                                fruitList.add(product);
                                break;
                            case "674967321c44585f0b4ab456":
                                vegetableList.add(product);
                                break;
                            case "674967321c44585f0b4ab457":
                                meatList.add(product);
                                break;
                        }
                    }

                    // Cập nhật dữ liệu cho từng Adapter
                    fruitAdapter.setProductList(fruitList);
                    vegetableAdapter.setProductList(vegetableList);
                    meatAdapter.setProductList(meatList);

                    // Thông báo cho Adapter biết dữ liệu đã thay đổi
                    fruitAdapter.notifyDataSetChanged();
                    vegetableAdapter.notifyDataSetChanged();
                    meatAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(AddProductActivity.this, "Lỗi khi lấy dữ liệu", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Response<ArrayList<Product>>> call, Throwable t) {
                Toast.makeText(AddProductActivity.this, "Lỗi kết nối server: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("API_ERROR", "Lỗi kết nối server: " + t.getMessage());
            }
        });
    }

    private void showAddProductDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_product, null);
        builder.setView(dialogView);


        // Ánh xạ các view trong dialog
        EditText edtProductID = dialogView.findViewById(R.id.edtProductID);
        EditText edtProductName = dialogView.findViewById(R.id.edtProductName);
        EditText edtProductImage = dialogView.findViewById(R.id.edtProductImage);
        EditText edtProductDescription = dialogView.findViewById(R.id.edtProductDescription);
        EditText edtProductPrice = dialogView.findViewById(R.id.edtProductPrice);
        Spinner spnCategory = dialogView.findViewById(R.id.spnCategory);
        Button btnAdd = dialogView.findViewById(R.id.btnAdd);

        // Tạo adapter cho Spinner (Category)
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.categories_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spnCategory.setAdapter(adapter);

        // Hiển thị dialog
        AlertDialog dialog = builder.create();
        dialog.show();
        // Xử lý sự kiện click button Add
        btnAdd.setOnClickListener(v -> {
            // Lấy dữ liệu từ các view
            String productID = edtProductID.getText().toString();
            String productName = edtProductName.getText().toString();
            String productImage = edtProductImage.getText().toString();
            String productDescription = edtProductDescription.getText().toString();
            double productPrice = Double.parseDouble(edtProductPrice.getText().toString());

            String category = spnCategory.getSelectedItem().toString().trim();

            // Lấy categoryid dựa trên category được chọn từ Spinner
            final String categoryId; // Khai báo categoryId là final
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
                default:
                    categoryId = ""; // Gán giá trị mặc định nếu không khớp
                    break;
            }

            // Tạo đối tượng Product
            Product newProduct = new Product();
            newProduct.setProductID(productID);
            newProduct.setProductName(productName);
            newProduct.setProductImage(productImage);

            newProduct.setProductDescription(productDescription);
            newProduct.setProductPrice(productPrice);
            newProduct.setCategoryid(categoryId);

            // Gọi API để thêm sản phẩm
            apiServices.addFruit(newProduct).enqueue(new Callback<Response<Product>>() {
                @Override
                public void onResponse(Call<Response<Product>> call, retrofit2.Response<Response<Product>> response) {
                    if (response.isSuccessful()) {
                        Product addedProduct = response.body().getData(); // Lấy sản phẩm vừa thêm

                        // Thêm sản phẩm vào Adapter tương ứng
                        switch (categoryId) {
                            case "674967321c44585f0b4ab455":
                                fruitAdapter.getProductList().add(addedProduct);
                                fruitAdapter.notifyItemInserted(fruitAdapter.getProductList().size() - 1);
                                break;
                            case "674967321c44585f0b4ab456":
                                vegetableAdapter.getProductList().add(addedProduct);
                                vegetableAdapter.notifyItemInserted(vegetableAdapter.getProductList().size() - 1);
                                break;
                            case "674967321c44585f0b4ab457":
                                meatAdapter.getProductList().add(addedProduct);
                                meatAdapter.notifyItemInserted(meatAdapter.getProductList().size() - 1);
                                break;
                        }

                        Toast.makeText(AddProductActivity.this, "Thêm sản phẩm thành công", Toast.LENGTH_SHORT).show();
                        Log.e("AddProductActivity", "productID: " + productID);
                        Log.e("AddProductActivity", "productName: " + productName);
                        Log.e("AddProductActivity", "productImage: " + productImage);
                        Log.e("AddProductActivity", "productDescription: " + productDescription);
                        Log.e("AddProductActivity", "productPrice: " + productPrice);
                        dialog.dismiss();
                    } else {
                        Toast.makeText(AddProductActivity.this, "Lỗi khi thêm sản phẩm", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(Call<Response<Product>> call, Throwable t) {
                    Toast.makeText(AddProductActivity.this, "Lỗi kết nối server: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("API_ERROR", "Lỗi kết nối server: " + t.getMessage());
                }
            });
        });


    }}