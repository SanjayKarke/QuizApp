package com.sanjay.quizapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.google.gson.annotations.SerializedName;
import com.sanjay.quizapp.database.converters.AnswerTypeConverter;
import com.sanjay.quizapp.database.converters.IncorrectAnswerTypeConverter;

import java.util.List;

@Entity(tableName = "question_table")
public class Question implements Parcelable {
    public static final Creator<Question> CREATOR = new Creator<Question>() {
        @Override
        public Question createFromParcel(Parcel in) {
            return new Question(in);
        }

        @Override
        public Question[] newArray(int size) {
            return new Question[size];
        }
    };
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "category")
    private String category;
    @ColumnInfo(name = "type")
    private String type;
    @ColumnInfo(name = "difficulty")
    private String difficulty;
    @ColumnInfo(name = "question")
    private String question;
    @ColumnInfo(name = "correctAnswer")
    @SerializedName("correct_answer")
    private String correctAnswer;

    @TypeConverters(IncorrectAnswerTypeConverter.class)
    @SerializedName("incorrect_answers")
    @ColumnInfo(name = "incorrect_answers")
    private List<String> incorrectAnswers;

    @TypeConverters(AnswerTypeConverter.class)
    @ColumnInfo(name = "answers")
    private List<Answer> answerList;

    public Question() {
    }

    // Implement Parcelable methods
    protected Question(Parcel in) {
        id = in.readInt();
        category = in.readString();
        type = in.readString();
        difficulty = in.readString();
        question = in.readString();
        correctAnswer = in.readString();
        in.readTypedList(answerList, Answer.CREATOR);

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(category);
        dest.writeString(type);
        dest.writeString(difficulty);
        dest.writeString(question);
        dest.writeString(correctAnswer);
        dest.writeStringList(incorrectAnswers);
        dest.writeTypedList(answerList);

    }

    public List<Answer> getAnswerList() {
        return answerList;
    }

    public void setAnswerList(List<Answer> answerList) {
        this.answerList = answerList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public List<String> getIncorrectAnswers() {
        return incorrectAnswers;
    }

    public void setIncorrectAnswers(List<String> incorrectAnswers) {
        this.incorrectAnswers = incorrectAnswers;
    }


}