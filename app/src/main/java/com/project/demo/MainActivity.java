package com.project.demo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private FloatingActionButton addBtn;
    private RecyclerView productListRecView;

    private ArrayList<String> idArrayList = new ArrayList<>();
    private ArrayList<String> productTypeArrayList = new ArrayList<>();
    private ArrayList<String> brandNameArrayList = new ArrayList<>();
    private ArrayList<String> priceArrayList = new ArrayList<>();
    private ArrayList<String> colorArrayList = new ArrayList<>();

    private ProductListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addBtn = findViewById(R.id.add_btn);
        productListRecView = findViewById(R.id.product_list_rec_view);

        addBtn.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, AddActivity.class));
        });

        getData();

        RecyclerView.LayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        productListRecView.setLayoutManager(linearLayoutManager);
        adapter = new ProductListAdapter(productTypeArrayList, brandNameArrayList, priceArrayList, colorArrayList, idArrayList, this);
        productListRecView.setAdapter(adapter);
    }

    private void getData() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("products").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (queryDocumentSnapshots.isEmpty()) {
                        Toast.makeText(this, "Empty Product List", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        ProductModel productItem = documentSnapshot.toObject(ProductModel.class);
                        productTypeArrayList.add(productItem.getProductType());
                        brandNameArrayList.add(productItem.getBrandName());
                        priceArrayList.add(productItem.getPrice());
                        colorArrayList.add(productItem.getColor());
                        idArrayList.add(documentSnapshot.getId());
                    }
                    adapter.notifyDataSetChanged();
                });
    }
}