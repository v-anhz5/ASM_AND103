package fpoly.account.asm_and103.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import fpoly.account.asm_and103.R;
import fpoly.account.asm_and103.adapter.ProductAdapter;
import fpoly.account.asm_and103.fragment.AccountFragment;
import fpoly.account.asm_and103.fragment.HistoryFragment;
import fpoly.account.asm_and103.fragment.HomeFragment;
import fpoly.account.asm_and103.fragment.ViewCartFragment;
import fpoly.account.asm_and103.model.Product;
import fpoly.account.asm_and103.model.Response;
import fpoly.account.asm_and103.services.ApiServices;
import fpoly.account.asm_and103.services.HttpRequest;
import retrofit2.Call;
import retrofit2.Callback;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    EditText edtSearch;
    RecyclerView rcvSearch;
//    ProductAdapter productAdapter;
    ArrayList<Product> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

//        edtSearch = findViewById(R.id.edtSearch);
//        rcvSearch = findViewById(R.id.rcvSearch);
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
//        rcvSearch.setLayoutManager(linearLayoutManager);
//        productAdapter = new ProductAdapter(list, this);
//        rcvSearch.setAdapter(productAdapter);

//        edtSearch.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                // Không cần xử lý gì ở đây
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                // Gọi API tìm kiếm khi text thay đổi
//                searchProducts(s.toString());
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                // Không cần xử lý gì ở đây
//            }
//        });

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        replaceFragment(new HomeFragment());
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                replaceFragment(new HomeFragment());
            } else if (itemId == R.id.nav_cart) {
                replaceFragment(new ViewCartFragment());
            } else if (itemId == R.id.nav_notification) {
                replaceFragment(new HistoryFragment());
            } else if (itemId == R.id.nav_account) {
                replaceFragment(new AccountFragment());
            }
            return true;
        });
    }

    private void searchProducts(String keyword) {
        ApiServices apiService = new HttpRequest().callAPI();
        Call<Response<ArrayList<Product>>> call = apiService.searchFruits(keyword);

        call.enqueue(new Callback<Response<ArrayList<Product>>>() {
            @Override
            public void onResponse(Call<Response<ArrayList<Product>>> call, retrofit2.Response<Response<ArrayList<Product>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    list = response.body().getData();
//                    productAdapter.setProductList(list); // Update the adapter's data
                } else {
                    Log.d("MainActivity", "Lỗi API search: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Response<ArrayList<Product>>> call, Throwable t) {
                Log.d("MainActivity", "Lỗi kết nối API search: " + t.getMessage());
            }
        });
    }
    private void replaceFragment(Fragment fragment) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainer, fragment);
        fragmentTransaction.commit();

    }
}
