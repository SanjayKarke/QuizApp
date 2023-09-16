package com.sanjay.quizapp.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.sanjay.quizapp.model.Question;
import com.sanjay.quizapp.model.QuizPojo;
import com.sanjay.quizapp.repositories.QuizRepository;
import com.sanjay.quizapp.utils.Constants;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuizViewModel extends ViewModel {
    public MutableLiveData<List<Question>> questionLiveData = new MutableLiveData<>();
    public MutableLiveData<Throwable> errorObservable = new MutableLiveData<>();
    private QuizRepository quizRepository;

    public QuizViewModel() {
    }

    @Inject
    public QuizViewModel(QuizRepository quizRepository) {
        this.quizRepository = quizRepository;
    }

    public void getQuestions() {
        /*?amount=10&category=9&difficulty=easy&type=multiple*/
        HashMap<String, String> queryMap = new HashMap<>();
        queryMap.put(Constants.AMOUNT, "10");
        queryMap.put(Constants.CATEGORY, "18");
        queryMap.put(Constants.DIFFICULTY, "easy");
        queryMap.put(Constants.TYPE, "multiple");
        quizRepository.getQuestions(queryMap).enqueue(new Callback<QuizPojo>() {
            @Override
            public void onResponse(Call<QuizPojo> call, Response<QuizPojo> response) {
                if (response.isSuccessful() && response.body() != null) {
                    questionLiveData.setValue(response.body().getQuestions());
                } else {
                    //handle error
                    errorObservable.setValue(new Throwable("Something went wrong!!"));
                }
            }

            @Override
            public void onFailure(Call<QuizPojo> call, Throwable t) {
                // Handle failure
                errorObservable.setValue(new Throwable("Something went wrong!!"));
            }
        });
    }

}