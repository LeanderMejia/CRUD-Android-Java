package com.project.demo;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.MyViewHolder> {

    private ArrayList<String> idArrayList = new ArrayList<>();
    private ArrayList<String> productTypeArrayList = new ArrayList<>();
    private ArrayList<String> brandNameArrayList = new ArrayList<>();
    private ArrayList<String> priceArrayList = new ArrayList<>();
    private ArrayList<String> colorArrayList = new ArrayList<>();
    private Context context;

    public ProductListAdapter(ArrayList<String> productTypeArrayList, ArrayList<String> brandNameArrayList, ArrayList<String> priceArrayList, ArrayList<String> colorArrayList, ArrayList<String> idArrayList, Context context) {
        this.productTypeArrayList = productTypeArrayList;
        this.brandNameArrayList = brandNameArrayList;
        this.priceArrayList = priceArrayList;
        this.colorArrayList = colorArrayList;
        this.idArrayList = idArrayList;
        this.context = context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView typeAndBrand, price, color;
        private CardView card;

        public MyViewHolder(View itemView) {
            super(itemView);
            typeAndBrand = itemView.findViewById(R.id.type_and_brand);
            price = itemView.findViewById(R.id.price);
            color = itemView.findViewById(R.id.color);
            card = itemView.findViewById(R.id.card);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_product_list, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.typeAndBrand.setText(productTypeArrayList.get(position) + " - " + brandNameArrayList.get(position));
        holder.price.setText(priceArrayList.get(position));
        holder.color.setText(colorArrayList.get(position));

        holder.card.setOnClickListener(view -> {
            String url = "https://www.google.com";
            openWebView(url);
        });

        holder.card.setOnLongClickListener(view -> openDialog(position));
    }

    private void openWebView(String url) {
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.layout_web_view);

        WebView webView = dialog.findViewById(R.id.web_view);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.loadUrl(url);

        dialog.show();
    }

    private boolean openDialog(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("OPTION")
                .setPositiveButton("EDIT", (dialog, which) -> {
                    Bundle bundle = new Bundle();
                    bundle.putString("id", idArrayList.get(position));
                    Intent intent = new Intent(context, EditActivity.class);
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                    //context.startActivity(new Intent(context, EditActivity.class));
                })
                .setNegativeButton("DELETE", (dialog, which) -> {
                    deleteData(position);
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        return true;
    }

    private void deleteData(int position) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("products").document(idArrayList.get(position))
                .delete()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(context, "Delete Successful", Toast.LENGTH_SHORT).show();
                    idArrayList.remove(position);
                    productTypeArrayList.remove(position);
                    brandNameArrayList.remove(position);
                    priceArrayList.remove(position);
                    colorArrayList.remove(position);
                    notifyDataSetChanged();

                })
                .addOnFailureListener(e -> Toast.makeText(context, "Error deleting document", Toast.LENGTH_SHORT).show());

        Toast.makeText(context, "Delete " + idArrayList.get(position), Toast.LENGTH_SHORT).show();
    }

    @Override
    public int getItemCount() {
        return productTypeArrayList.size();
    }
}
