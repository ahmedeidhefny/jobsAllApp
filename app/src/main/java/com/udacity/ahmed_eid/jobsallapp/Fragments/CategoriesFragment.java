package com.udacity.ahmed_eid.jobsallapp.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
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
    private final int[] categoryImages = {
            R.drawable.programming,
            R.drawable.it,
            R.drawable.enginnering,
            R.drawable.realstate,
            R.drawable.sales,
            R.drawable.artdesign,
            R.drawable.accounting,
            R.drawable.marketing,
            R.drawable.other
    };
    private Unbinder unbinder;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        getActivity().findViewById(R.id.search_EText).setVisibility(View.GONE);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
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

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.app_bar_search).setVisible(false);
    }

    private void setDataToCategories() {
        for (int i = 0; i < categoryNames.length; i++) {
            Category category = new Category(categoryImages[i], categoryNames[i]);
            categories.add(i, category);
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
