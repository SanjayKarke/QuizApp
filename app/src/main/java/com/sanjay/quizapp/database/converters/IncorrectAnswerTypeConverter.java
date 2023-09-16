package com.sanjay.quizapp.database.converters;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class IncorrectAnswerTypeConverter {
    @TypeConverter
    public String fromIncorrectAnswers(List<String> incorrectAnswers) {
        Gson gson = new Gson();
        return gson.toJson(incorrectAnswers);
    }

    @TypeConverter
    public List<String> toIncorrectAnswers(String incorrectAnswersString) {
        Gson gson = new Gson();
        Type listType = new TypeToken<List<String>>() {
        }.getType();
        return gson.fromJson(incorrectAnswersString, listType);
    }
}
