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
    Unbinder unbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View myView;
        myView = inflater.inflate(R.layout.fragment_categories, container, false);
        unbinder = ButterKnife.bind(this, myView);
        ArrayList<Category> categories = new ArrayList<>();
        categories.add(new Category(R.drawable.p1, "Programming"));
        //categories.add(new Category(R.drawable.p2, "Real State"));
        //categories.add(new Category(R.drawable.p7,"Real State"));
        //categories.add(new Category(R.drawable.p4,"Machine Learning"));
        categories.add(new Category(R.drawable.p8,"Real State"));
        categories.add(new Category(R.drawable.p6,"UI/UX Designer"));

        GridLayoutManager manager = new GridLayoutManager(getActivity(), 2);
        categoryRecycler.setLayoutManager(manager);
        CategoryAdapter adapter = new CategoryAdapter(getActivity(), categories);
        categoryRecycler.setAdapter(adapter);
        return myView;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
