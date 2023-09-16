package com.sanjay.quizapp.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.sanjay.quizapp.model.Question;

import java.util.List;

@Dao
public interface QuestionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Question question);

    @Query("SELECT * FROM question_table WHERE id = :questionId LIMIT 1")
    Question getQuestionById(long questionId);

    @Query("SELECT * FROM question_table")
    List<Question> getAllQuestions();

    @Query("DELETE FROM question_table")
    void clearData();
}
