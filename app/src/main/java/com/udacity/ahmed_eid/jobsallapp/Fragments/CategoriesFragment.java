package com.udacity.ahmed_eid.jobsallapp.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.udacity.ahmed_eid.jobsallapp.Adapters.CategoryAdapter;
import com.udacity.ahmed_eid.jobsallapp.Model.Category;
import com.udacity.ahmed_eid.jobsallapp.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class CategoriesFragment extends Fragment {


    @BindView(R.id.category_recycler)
    RecyclerView categoryRecycler;
    private ArrayList<Category> categories;
    private String[] categoryNames;
    private int[] categoryImages = {
            R.drawable.p1,
            R.drawable.p2,
            R.drawable.p3,
            R.drawable.p4,
            R.drawable.p5,
            R.drawable.p6,
            R.drawable.p7,
            R.drawable.p8,
            R.drawable.p1
    };
    Unbinder unbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View myView;
        myView = inflater.inflate(R.layout.fragment_categories, container, false);
        unbinder = ButterKnife.bind(this, myView);
        categories = new ArrayList<>();
        categoryNames = getResources().getStringArray(R.array.categories);
        setDataToCategories();
        GridLayoutManager manager = new GridLayoutManager(getActivity(), 2);
        categoryRecycler.setLayoutManager(manager);


        CategoryAdapter adapter = new CategoryAdapter(getActivity(), categories);
        categoryRecycler.setAdapter(adapter);
        return myView;
    }

    private void setDataToCategories() {
        for (int i = 0; i < categoryNames.length; i++) {
            Category category = new Category(categoryImages[i], categoryNames[i]+" Jobs");
            categories.add(i, category);
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
