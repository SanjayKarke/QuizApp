package com.sanjay.quizapp.repositories;

import com.sanjay.quizapp.model.QuizPojo;
import com.sanjay.quizapp.network.ApiService;

import java.util.HashMap;

import javax.inject.Inject;

import retrofit2.Call;

public class QuizRepository {
    private ApiService apiService;

    @Inject
    public QuizRepository( ApiService apiService) {
        this.apiService = apiService;
    }

    public Call<QuizPojo> getQuestions(HashMap<String,String> map) {
        return apiService.getQuizList(map);
    }
}
