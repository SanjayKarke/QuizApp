package com.sanjay.quizapp.di;

import android.app.Application;
import android.content.Context;

import androidx.room.Room;

import com.sanjay.quizapp.BuildConfig;
import com.sanjay.quizapp.database.AppDatabase;
import com.sanjay.quizapp.database.dao.QuestionDao;
import com.sanjay.quizapp.network.ApiService;
import com.sanjay.quizapp.repositories.QuizRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
@InstallIn(SingletonComponent.class)
public class AppModule {


    @Provides
    public static QuestionDao provideUserDao(AppDatabase database) {
        return database.quizDao();
    }


    @Provides
    @Singleton
    Context provideContext(Application application) {
        return application;
    }

    @Provides
    @Singleton
    public AppDatabase provideAppDatabase(Context context) {
        return Room.databaseBuilder(context, AppDatabase.class, "my_database.db")
                .build();
    }

    @Provides
    public OkHttpClient provideOkHttpClient() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        return new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build();
    }

    @Provides
    public Retrofit provideRetrofit(OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    @Provides
    public ApiService provideApiService(Retrofit retrofit) {
        return retrofit.create(ApiService.class);
    }


    @Provides
    public QuizRepository provideUserRepository(ApiService apiService) {
        return new QuizRepository(apiService);
    }

}