package com.udacity.ahmed_eid.jobsallapp.Model;

public class Category {
    private int categoryIcon ;
    private String categoryName ;

    public Category(int categoryIcon, String categoryName) {
        this.categoryIcon = categoryIcon;
        this.categoryName = categoryName;
    }

    public int getCategoryIcon() {
        return categoryIcon;
    }

    public String getCategoryName() {
        return categoryName;
    }
}
