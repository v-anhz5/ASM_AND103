package fpoly.account.asm_and103.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.IOException;
import java.util.ArrayList;

import fpoly.account.asm_and103.R;
import fpoly.account.asm_and103.adapter.Adapter_Item_District_Select_GHN;
import fpoly.account.asm_and103.adapter.Adapter_Item_Province_Select_GHN;
import fpoly.account.asm_and103.adapter.Adapter_Item_Ward_Select_GHN;
import fpoly.account.asm_and103.databinding.ActivityCheckOutBinding;
import fpoly.account.asm_and103.model.District;
import fpoly.account.asm_and103.model.DistrictRequest;
import fpoly.account.asm_and103.model.GHNItem;
import fpoly.account.asm_and103.model.GHNOrderRequest;
import fpoly.account.asm_and103.model.GHNOrderRespone;
import fpoly.account.asm_and103.model.Order;
import fpoly.account.asm_and103.model.Product;
import fpoly.account.asm_and103.model.Province;
import fpoly.account.asm_and103.model.ResponseGHN;
import fpoly.account.asm_and103.model.Ward;
import fpoly.account.asm_and103.services.GHNRequest;
import fpoly.account.asm_and103.services.HttpRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckOutActivity extends AppCompatActivity {

    ActivityCheckOutBinding binding;
    GHNRequest ghnRequest;
    FirebaseAuth mAuth;
    ArrayList<Province> list_Province;
    ArrayList<District> list_District;
    ArrayList<Ward> list_Ward;
    Adapter_Item_Province_Select_GHN adapterItemProvinceSelectGhn;
    Adapter_Item_District_Select_GHN adapterItemDistrictSelectGhn;
    Adapter_Item_Ward_Select_GHN adapterItemWardSelectGhn;
    int ProvinceID;
    int DistrictID;
    String WardCode;
    HttpRequest httpRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCheckOutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        httpRequest = new HttpRequest();
        ghnRequest = new GHNRequest();
        configGHN();
        mAuth = FirebaseAuth.getInstance();

        // Ánh xạ các view
        ImageButton btnBack = binding.btnBack;
        Spinner spProvince = binding.spProvince;
        Spinner spDistrict = binding.spDistrict;
        Spinner spWard = binding.spWard;
        EditText edLocation = binding.edLocation;
        EditText edName = binding.edName;
        EditText edPhone = binding.edPhone;
        Button btnOrder = binding.btnOrder;

        // Xử lý sự kiện click nút back
        btnBack.setOnClickListener(v -> finish());

        // Xử lý sự kiện click nút Order
        btnOrder.setOnClickListener(v -> {
            FirebaseUser user = mAuth.getCurrentUser();
            if (user != null) {
                String userId = user.getUid();
            }
            if (WardCode.equals("")) return;

            Product product = getIntent().getParcelableExtra("item"); // Get Parcelable
            GHNItem ghnItem = new GHNItem();

            Log.d("zzzzzz", "onBindViewHolder: "+product);

            ghnItem.setName(product.getProductName());
            ghnItem.setPrice((int) product.getProductPrice()); // Correct type casting
            ghnItem.setCode(product.getProductID());
            ghnItem.setQuantity(1);
            ghnItem.setWeight(50);

            ArrayList<GHNItem> items = new ArrayList<>();
            items.add(ghnItem);
            String name = binding.edName.getText().toString().trim();
            String phone = binding.edPhone.getText().toString().trim();
            String location = binding.edLocation.getText().toString().trim();

            GHNOrderRequest ghnOrderRequest = new GHNOrderRequest(
                    name,
                    phone,
                    location,
                    WardCode,
                    DistrictID,
                    items
            );
            ghnRequest.getApiService().GHNOrder(ghnOrderRequest).enqueue(responseOrder);
        });

    }

    Callback<ResponseGHN<GHNOrderRespone>> responseOrder = new Callback<ResponseGHN<GHNOrderRespone>>() {
        @Override
        public void onResponse(Call<ResponseGHN<GHNOrderRespone>> call, Response<ResponseGHN<GHNOrderRespone>> response) {
            Log.d("zzzzzz", "onResponse: "+response.code());
            if (response.code() == 400) {
                Log.e("CheckOutActivity", "GHNOrder Error Response: " + response.errorBody().toString());
            }
            if (response.isSuccessful()){
                if (response.body().getCode()==200) {
                    Toast.makeText(CheckOutActivity.this, "Đặt hàng thành công", Toast.LENGTH_SHORT).show();
                    Order order = new Order();
                    order.setOrder_code(response.body().getData().getOrder_code());

                    order.setId_user(getSharedPreferences("INFO", MODE_PRIVATE).getString("id",""));
                    httpRequest.callAPI().order(order).enqueue(responseOrderDatabase);
                    finish();
                }
            } else {
                try {
                    String errorBody = response.errorBody().string(); // Get the error message
                    Log.e("CheckOutActivity", "GHNOrder Error Response: " + errorBody);


                } catch (IOException e) {
                    Log.e("CheckOutActivity", "Error reading error body: " + e.getMessage());
                }
            }
        }

        @Override
        public void onFailure(Call<ResponseGHN<GHNOrderRespone>> call, Throwable t) {

        }
    };

    Callback<fpoly.account.asm_and103.model.Response<Order>> responseOrderDatabase = new Callback<fpoly.account.asm_and103.model.Response<Order>>() {
        @Override
        public void onResponse(Call<fpoly.account.asm_and103.model.Response<Order>> call, Response<fpoly.account.asm_and103.model.Response<Order>> response) { // Correct parameter type
            if (response.isSuccessful()){
                if (response.body().getStatus() == 200){ // Use correct method name (getStatus)
                    Toast.makeText(CheckOutActivity.this, "Cảm ơn đã đặt hàng", Toast.LENGTH_SHORT).show(); // Use CheckOutActivity.this
                    finish();
                }
            }
        }

        @Override
        public void onFailure(Call<fpoly.account.asm_and103.model.Response<Order>> call, Throwable t) { // Correct parameter type
            // ...
        }
    };

    private void configGHN() {
        Call<ResponseGHN<ArrayList<Province>>> call = ghnRequest.getApiService().getListProvince();
        call.enqueue(new Callback<ResponseGHN<ArrayList<Province>>>() {
            @Override
            public void onResponse(Call<ResponseGHN<ArrayList<Province>>> call, Response<ResponseGHN<ArrayList<Province>>> response) {
                if (response.isSuccessful()) {
                    if (response.body().getCode() == 200) {
                        list_Province = new ArrayList<>(response.body().getData());
                        adapterItemProvinceSelectGhn = new Adapter_Item_Province_Select_GHN(CheckOutActivity.this, list_Province);
                        binding.spProvince.setAdapter(adapterItemProvinceSelectGhn);
                    } else {
                        Log.e("TAG", "onResponse: " + "Call thất bại");
                    }
                } else {
                    Log.e("TAG", "onResponse: " + "Call thất bại");
                    Log.e("TAG", "onResponse: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseGHN<ArrayList<Province>>> call, Throwable t) {
                Log.e("TAG", "onFailure: " + t.getMessage());
            }
        });

        binding.spProvince.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ProvinceID = ((Province) parent.getAdapter().getItem(position)).getProvinceID();
                DistrictRequest districtRequest = new DistrictRequest(ProvinceID);
                ghnRequest.getApiService().getListDistrict(districtRequest).enqueue(new Callback<ResponseGHN<ArrayList<District>>>() {
                    @Override
                    public void onResponse(Call<ResponseGHN<ArrayList<District>>> call, Response<ResponseGHN<ArrayList<District>>> response) {
                        if (response.isSuccessful()) {
                            list_District = response.body().getData();
                            adapterItemDistrictSelectGhn = new Adapter_Item_District_Select_GHN(CheckOutActivity.this, list_District);
                            binding.spDistrict.setAdapter(adapterItemDistrictSelectGhn);
                        } else {
                            Log.e("TAG", "onResponse: " + "Call thất bại");
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseGHN<ArrayList<District>>> call, Throwable t) {
                        Log.e("TAG", "onFailure: " + t.getMessage());
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        binding.spDistrict.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                District district = list_District.get(position);
                DistrictID = district.getDistrictID();
                ghnRequest.getApiService().getListWard(district.getDistrictID()).enqueue(new Callback<ResponseGHN<ArrayList<Ward>>>() {
                    @Override
                    public void onResponse(Call<ResponseGHN<ArrayList<Ward>>> call, Response<ResponseGHN<ArrayList<Ward>>> response) {
                        if (response.isSuccessful()) {
                            list_Ward = new ArrayList<>(response.body().getData());
                            adapterItemWardSelectGhn = new Adapter_Item_Ward_Select_GHN(CheckOutActivity.this, list_Ward);
                            binding.spWard.setAdapter(adapterItemWardSelectGhn);
                        } else {
                            Toast.makeText(CheckOutActivity.this, "Xẩy ra lỗi", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseGHN<ArrayList<Ward>>> call, Throwable t) {
                        Log.e("TAG", "onFailure: " + t.getMessage());
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        binding.spWard.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                WardCode = list_Ward.get(position).getWardCode();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}