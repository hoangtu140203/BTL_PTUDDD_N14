package com.example.btlptudddn14.models;

import com.example.btlptudddn14.R;

public enum Category {

    FOOD_BEVERAGE("Food & beverage", R.drawable.dinner_icon),
    HEALTH("Health", R.drawable.health_icon),
    HOUSING("Housing", R.drawable.housing_icon),
    INVESMENT("Investment", R.drawable.invesment_icon),
    TRANSPORT("Transport", R.drawable.transport_icon),
    TRAVEL("Travel", R.drawable.travel_icon),
    UNCATEGORIZED("Uncategorized", R.drawable.question_icon);

    Category(String name, int drawable) {
        this.name = name;
        this.drawable = drawable;
    }

    public String name;
    public int drawable;
}