package com.sanjay.quizapp.di;

import com.sanjay.quizapp.repositories.QuizRepository;
import com.sanjay.quizapp.viewmodel.QuizViewModel;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ViewModelComponent;
import dagger.hilt.android.scopes.ViewModelScoped;

@Module
@InstallIn(ViewModelComponent.class)
public class MyViewModelModule {

    @Provides
    @ViewModelScoped
    public QuizViewModel provideMyViewModel(QuizRepository repository) {
        return new QuizViewModel(repository);
    }
}
