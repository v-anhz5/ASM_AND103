package fpoly.account.asm_and103.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import fpoly.account.asm_and103.R;
import fpoly.account.asm_and103.adapter.ProductAdapter;
import fpoly.account.asm_and103.model.Product;
import fpoly.account.asm_and103.model.Response;
import fpoly.account.asm_and103.services.ApiServices;
import fpoly.account.asm_and103.services.HttpRequest;
import retrofit2.Call;
import retrofit2.Callback;

public class HomeFragment extends Fragment {

    private RecyclerView rcvFruit, rcvVegetables, rcvMeats;
    private ProductAdapter fruitAdapter, vegetableAdapter, meatAdapter;
    private TextView tvFruit, tvVegetables, tvMeats, tvAll;
    private RelativeLayout rlMeats, rlFruit, rlVegetable;

    private ApiServices apiServices;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        tvFruit = view.findViewById(R.id.tvFruits);
        tvVegetables = view.findViewById(R.id.tvVegetables);
        tvMeats = view.findViewById(R.id.tvMeats);
        tvAll = view.findViewById(R.id.tvAll);

        rlFruit = view.findViewById(R.id.rlFruits);
        rlVegetable = view.findViewById(R.id.rlVegetables);
        rlMeats = view.findViewById(R.id.rlMeats);

        clickOnCategory();

        // Ánh xạ các RecyclerView
        rcvFruit = view.findViewById(R.id.rcvFruits);
        rcvVegetables = view.findViewById(R.id.rcvVegetables);
        rcvMeats = view.findViewById(R.id.rcvMeats);

        // Khởi tạo LayoutManager cho các RecyclerView
        LinearLayoutManager layoutManagerFruits = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager layoutManagerVegetables = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager layoutManagerMeats = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);

        rcvFruit.setLayoutManager(layoutManagerFruits);
        rcvVegetables.setLayoutManager(layoutManagerVegetables);
        rcvMeats.setLayoutManager(layoutManagerMeats);

        // Khởi tạo Adapter cho các RecyclerView
        fruitAdapter = new ProductAdapter(new ArrayList<>(), getContext());
        vegetableAdapter = new ProductAdapter(new ArrayList<>(), getContext());
        meatAdapter = new ProductAdapter(new ArrayList<>(), getContext());

        rcvFruit.setAdapter(fruitAdapter);
        rcvVegetables.setAdapter(vegetableAdapter);
        rcvMeats.setAdapter(meatAdapter);

        apiServices = new HttpRequest().callAPI();
        loadDataFruit();
        loadDataVegetables();
        loadDataMeats();

        return view;
    }

    private void clickOnCategory() {
        tvFruit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rcvFruit.setVisibility(View.VISIBLE);
                rcvVegetables.setVisibility(View.GONE);
                rcvMeats.setVisibility(View.GONE);
                rlMeats.setVisibility(View.GONE);
                rlVegetable.setVisibility(View.GONE);
                rlFruit.setVisibility(View.VISIBLE);
            }
        });

        tvVegetables.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rcvFruit.setVisibility(View.GONE);
                rcvVegetables.setVisibility(View.VISIBLE);
                rcvMeats.setVisibility(View.GONE);
                rlMeats.setVisibility(View.GONE);
                rlVegetable.setVisibility(View.VISIBLE);
                rlFruit.setVisibility(View.GONE);
            }
        });

        tvMeats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rcvFruit.setVisibility(View.GONE);
                rcvVegetables.setVisibility(View.GONE);
                rcvMeats.setVisibility(View.VISIBLE);
                rlMeats.setVisibility(View.VISIBLE);
                rlVegetable.setVisibility(View.GONE);
                rlFruit.setVisibility(View.GONE);
            }
        });

        tvAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rcvFruit.setVisibility(View.VISIBLE);
                rcvVegetables.setVisibility(View.VISIBLE);
                rcvMeats.setVisibility(View.VISIBLE);
                rlMeats.setVisibility(View.VISIBLE);
                rlVegetable.setVisibility(View.VISIBLE);
                rlFruit.setVisibility(View.VISIBLE);
            }
        });

    }

    private void loadDataMeats() {
        String categoryId = "674967321c44585f0b4ab457"; // Thay thế bằng category ID của Meats
        apiServices.getListFruit(categoryId).enqueue(new Callback<Response<ArrayList<Product>>>() {
            @Override
            public void onResponse(Call<Response<ArrayList<Product>>> call, retrofit2.Response<Response<ArrayList<Product>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ArrayList<Product> meatList = response.body().getData(); // Đổi tên biến thành meatList
                    meatAdapter.setProductList(meatList); // Cập nhật dữ liệu cho meatAdapter
                    meatAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getContext(), "Lỗi khi lấy dữ liệu Meats", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Response<ArrayList<Product>>> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi kết nối server: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("API_ERROR", "Lỗi kết nối server: " + t.getMessage());
            }
        });
    }

    private void loadDataVegetables() {
        String categoryId = "674967321c44585f0b4ab456"; // Thay thế bằng category ID của Vegetables
        apiServices.getListFruit(categoryId).enqueue(new Callback<Response<ArrayList<Product>>>() {
            @Override
            public void onResponse(Call<Response<ArrayList<Product>>> call, retrofit2.Response<Response<ArrayList<Product>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ArrayList<Product> vegetableList = response.body().getData(); // Đổi tên biến thành vegetableList
                    vegetableAdapter.setProductList(vegetableList); // Cập nhật dữ liệu cho vegetableAdapter
                    vegetableAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getContext(), "Lỗi khi lấy dữ liệu Vegetables", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Response<ArrayList<Product>>> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi kết nối server: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("API_ERROR", "Lỗi kết nối server: " + t.getMessage());
            }
        });
    }

    private void loadDataFruit() {
        String categoryId = "674967321c44585f0b4ab455"; // ID của category bạn muốn lấy
        apiServices.getListFruit(categoryId).enqueue(new Callback<Response<ArrayList<Product>>>() {
            @Override
            public void onResponse(Call<Response<ArrayList<Product>>> call, retrofit2.Response<Response<ArrayList<Product>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ArrayList<Product> fruitList = response.body().getData();
                    fruitAdapter.setProductList(fruitList); // Cập nhật dữ liệu cho fruitAdapter
                    fruitAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getContext(), "Lỗi khi lấy dữ liệu Fruit", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Response<ArrayList<Product>>> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi kết nối server: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("API_ERROR", "Lỗi kết nối server: " + t.getMessage());
            }
        });
    }
}