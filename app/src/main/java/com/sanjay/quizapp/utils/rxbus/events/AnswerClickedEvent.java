package com.sanjay.quizapp.utils.rxbus.events;

import com.sanjay.quizapp.model.Answer;
import com.sanjay.quizapp.model.Question;

public class AnswerClickedEvent {
    Question question;
    Answer answerItem;

    public AnswerClickedEvent(Question question, Answer answerItem) {
        this.question = question;
        this.answerItem = answerItem;
    }

    public Question getQuestion() {
        return question;
    }

    public Answer getAnswerItem() {
        return answerItem;
    }
}
