package com.project.demo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.HashMap;
import java.util.Map;

public class EditActivity extends AppCompatActivity {

    private EditText productType, brandName, price, color;
    private Button updateBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        productType = findViewById(R.id.product_type);
        brandName = findViewById(R.id.brand_name);
        price = findViewById(R.id.price);
        color = findViewById(R.id.color);
        updateBtn = findViewById(R.id.update_btn);

        Bundle receivedBundle = getIntent().getExtras();
        if (receivedBundle == null) return;

        getData(receivedBundle.getString("id"));

        updateBtn.setOnClickListener(view -> {
            boolean isEmpty = productType.getText().toString().trim().isEmpty() || brandName.getText().toString().trim().isEmpty() || price.getText().toString().trim().isEmpty() || color.getText().toString().trim().isEmpty();
            if (isEmpty) {
                Toast.makeText(this, "Please fill up all the fields", Toast.LENGTH_SHORT).show();
                return;
            }
            updateData(receivedBundle.getString("id"));
        });
    }

    private void getData(String id) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("products")
                .document(id)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            ProductModel productItem = document.toObject(ProductModel.class);
                            productType.setText(productItem.getProductType());
                            brandName.setText(productItem.getBrandName());
                            price.setText(productItem.getPrice());
                            color.setText(productItem.getColor());
                        } else {
                            Toast.makeText(this, "Error getting documents", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void updateData(String id) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference ref = db.collection("products").document(id);
        ProductModel productItem = new ProductModel(productType.getText().toString(), brandName.getText().toString(), price.getText().toString(), color.getText().toString());
        Map<String, Object> updatedData = new HashMap<>();
        updatedData.put("productType", productItem.getProductType());
        updatedData.put("brandName", productItem.getBrandName());
        updatedData.put("price", productItem.getPrice());
        updatedData.put("color", productItem.getColor());

        ref.update(updatedData)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Update Successful", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(EditActivity.this, MainActivity.class));
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Error updating document", Toast.LENGTH_SHORT).show());
    }

}