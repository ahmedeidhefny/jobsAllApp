package com.udacity.ahmed_eid.jobsallapp.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.udacity.ahmed_eid.jobsallapp.Model.Category;
import com.udacity.ahmed_eid.jobsallapp.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    Context mContext;
    ArrayList<Category> categories;


    public CategoryAdapter(Context mContext, ArrayList<Category> categories) {
        this.mContext = mContext;
        this.categories = categories;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View myView = LayoutInflater.from(mContext).inflate(R.layout.item_category, parent, false);
        return new CategoryViewHolder(myView);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        holder.categoryIcon.setImageResource(categories.get(position).getCategoryIcon());
        holder.categoryName.setText(categories.get(position).getCategoryName());
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder {
        TextView categoryName;
        ImageView categoryIcon;

        public CategoryViewHolder(View itemView) {
            super(itemView);
            categoryIcon = itemView.findViewById(R.id.item_catg_icon);
            categoryName = itemView.findViewById(R.id.item_catg_name);

        }
    }
}
