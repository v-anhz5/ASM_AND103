package fpoly.account.asm_and103.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import fpoly.account.asm_and103.R;

public class CRUDActivity extends AppCompatActivity {

    Button btnAdd, btnEdit, btnDelete, btnView,btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_crudactivity);

        // Ánh xạ các view
        btnAdd = findViewById(R.id.btnAdd);
        btnLogout = findViewById(R.id.btnLogout);
//        btnEdit = findViewById(R.id.btnEdit);
//        btnDelete = findViewById(R.id.btnDelete);
//        btnView = findViewById(R.id.btnView);

        // Xử lý sự kiện click cho các button
        btnAdd.setOnClickListener(v -> {
            Intent intent = new Intent(CRUDActivity.this, AddProductActivity.class);
            startActivity(intent);
        });

        btnLogout.setOnClickListener(v -> {
            Intent intent = new Intent(CRUDActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();

                });

//        btnEdit.setOnClickListener(v -> {
//            Intent intent = new Intent(CRUDActivity.this, EditProductActivity.class);
//            startActivity(intent);
//        });
//
//        btnDelete.setOnClickListener(v -> {
//            Intent intent = new Intent(CRUDActivity.this, DeleteProductActivity.class);
//            startActivity(intent);
//        });
//
//        btnView.setOnClickListener(v -> {
//            Intent intent = new Intent(CRUDActivity.this, ViewProductListActivity.class);
//            startActivity(intent);
//        });
    }
}