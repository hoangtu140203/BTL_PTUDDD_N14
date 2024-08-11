package com.example.btlptudddn14.models;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Target implements Parcelable {
    private int id;
    private String planName;
    private double totalBudget;
    private double savedBudget;
    private String deadline;
    // mức dộ ưu tiên
    private int priorityLevel;

    // loại mục tiêu vừa, nhỏ, to
    private String targetType;
    private String imgSrc;
    private static Target instance;

    public Target() {
    }
    protected Target(Parcel in) {
        id = in.readInt();
        planName = in.readString();
        totalBudget = in.readDouble();
        savedBudget = in.readDouble();
        deadline = in.readString();
        priorityLevel = in.readInt();
        targetType = in.readString();
        imgSrc = in.readString();

    }

    public static final Creator<Target> CREATOR = new Creator<Target>() {
        @Override
        public Target createFromParcel(Parcel in) {
            return new Target(in);
        }

        @Override
        public Target[] newArray(int size) {
            return new Target[size];
        }
    };

    public static synchronized Target getInstance() {
        if (instance == null) {
            instance = new Target();
        }
        return instance;
    }

    public Target(int id, String planName, double totalBudget, double savedBudget, String deadline, String targetType, int priorityLevel, String imgSrc) {
        this.id = id;
        this.planName = planName;
        this.totalBudget = totalBudget;
        this.savedBudget = savedBudget;
        this.deadline = deadline;
        this.targetType = targetType;
        this.priorityLevel = priorityLevel;
        this.imgSrc = imgSrc;
    }

    public String getImgSrc() {
        return imgSrc;
    }

    public void setImgSrc(String imgSrc) {
        this.imgSrc = imgSrc;
    }

    public int getID() {
        return id;
    }


    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public double getTotalBudget() {
        return totalBudget;
    }

    public void setTotalBudget(double totalBudget) {
        this.totalBudget = totalBudget;
    }

    public double getSavedBudget() {
        return savedBudget;
    }

    public void setSavedBudget(double savedBudget) {
        this.savedBudget = savedBudget;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public int getPriorityLevel() {
        return priorityLevel;
    }

    public void setPriorityLevel(int priorityLevel) {
        this.priorityLevel = priorityLevel;
    }

    public String getTargetType() {
        return targetType;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(planName);
        parcel.writeDouble(totalBudget);
        parcel.writeDouble(savedBudget);
        parcel.writeString(deadline);
        parcel.writeString(targetType);
        parcel.writeInt(priorityLevel);
        parcel.writeString(imgSrc);
    }
}
