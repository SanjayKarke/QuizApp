package com.sanjay.quizapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "answer_table")
public class Answer implements Parcelable {
    public static final Creator<Answer> CREATOR = new Creator<Answer>() {
        @Override
        public Answer createFromParcel(Parcel in) {
            return new Answer(in);
        }

        @Override
        public Answer[] newArray(int size) {
            return new Answer[size];
        }
    };
    @PrimaryKey(autoGenerate = true)
    int id;
    private String ans;
    private boolean isSelected;

    // Constructor, getters, and setters for the class
    private boolean isCorrect;

    protected Answer(Parcel in) {
        ans = in.readString();
        isSelected = in.readByte() != 0;
        isCorrect = in.readByte() != 0;
    }

    public Answer() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAns() {
        return ans;
    }

    public void setAns(String ans) {
        this.ans = ans;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public boolean isCorrect() {
        return isCorrect;
    }

    public void setCorrect(boolean correct) {
        isCorrect = correct;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(ans);
        dest.writeByte((byte) (isSelected ? 1 : 0));
        dest.writeByte((byte) (isCorrect ? 1 : 0));
    }
}