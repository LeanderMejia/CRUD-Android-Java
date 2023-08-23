package com.project.demo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class AddActivity extends AppCompatActivity {

    private EditText productType, brandName, price, color;
    private Button postBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        productType = findViewById(R.id.product_type);
        brandName = findViewById(R.id.brand_name);
        price = findViewById(R.id.price);
        color = findViewById(R.id.color);
        postBtn = findViewById(R.id.post_btn);

        postBtn.setOnClickListener(view -> {
            boolean isEmpty = productType.getText().toString().trim().isEmpty() || brandName.getText().toString().trim().isEmpty() || price.getText().toString().trim().isEmpty() || color.getText().toString().trim().isEmpty();
            if (isEmpty) {
                Toast.makeText(this, "Please fill up all the fields", Toast.LENGTH_SHORT).show();
                return;
            }
            addData();
        });
    }

    private void addData() {
        ProductModel productItem = new ProductModel(productType.getText().toString(), brandName.getText().toString(), price.getText().toString(), color.getText().toString());
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference productsCollection = db.collection("products");
        // Debugging: Log the data before adding

        productsCollection.add(productItem)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(this, "Product Added", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(AddActivity.this, MainActivity.class));
                }).addOnFailureListener(e -> {
            Toast.makeText(this, "ERROR: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });

    }
}