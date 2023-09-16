package com.sanjay.quizapp.database.converters;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sanjay.quizapp.model.Answer;

import java.lang.reflect.Type;
import java.util.List;

public class AnswerTypeConverter {
    @TypeConverter
    public String fromCustomAnswerList(List<Answer> answerList) {
        Gson gson = new Gson();
        return gson.toJson(answerList);
    }

    @TypeConverter
    public List<Answer> toCustomAnswerList(String answerListString) {
        Gson gson = new Gson();
        Type listType = new TypeToken<List<Answer>>() {
        }.getType();
        return gson.fromJson(answerListString, listType);
    }
}
