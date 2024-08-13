package com.example.btlptudddn14.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class UserExpense implements Parcelable {
    private int id;
    private int userId;
    private int type;
    private String description;
    private String date;
    private double amount;

    public UserExpense(int id, int userId, int type, String description, String date, double amount) {
        this.id = id;
        this.userId = userId;
        this.type = type;
        this.description = description;
        this.date = date;
        this.amount = amount;
    }


    protected UserExpense(Parcel in) {
        id = in.readInt();
        userId = in.readInt();
        type = in.readInt();
        description = in.readString();
        date = in.readString();
        amount = in.readDouble();
    }

    public static final Creator<UserExpense> CREATOR = new Creator<UserExpense>() {
        @Override
        public UserExpense createFromParcel(Parcel in) {
            return new UserExpense(in);
        }

        @Override
        public UserExpense[] newArray(int size) {
            return new UserExpense[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeInt(userId);
        parcel.writeInt(type);
        parcel.writeString(description);
        parcel.writeString(date);
        parcel.writeDouble(amount);

    }

}
