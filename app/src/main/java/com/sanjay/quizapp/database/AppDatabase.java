package com.sanjay.quizapp.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.sanjay.quizapp.database.dao.QuestionDao;
import com.sanjay.quizapp.model.Answer;
import com.sanjay.quizapp.model.Question;

@Database(entities = {Question.class, Answer.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract QuestionDao quizDao();

}