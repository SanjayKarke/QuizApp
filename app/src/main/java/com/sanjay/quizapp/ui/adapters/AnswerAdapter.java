package com.sanjay.quizapp.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.sanjay.quizapp.R;
import com.sanjay.quizapp.model.Answer;
import com.sanjay.quizapp.model.Question;
import com.sanjay.quizapp.utils.rxbus.RxBus;
import com.sanjay.quizapp.utils.rxbus.events.AnswerClickedEvent;

import java.util.List;

public class AnswerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<Answer> answers;
    Question question;
    Context context;

    public AnswerAdapter(Context context, Question question, List<Answer> answers) {
        this.answers = answers;
        this.question = question;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.single_item_answer, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MyViewHolder myViewHolder = (MyViewHolder) holder;
        Answer answerItem = answers.get(position);
        myViewHolder.answerTv.setText(answerItem.getAns());
        myViewHolder.container.setOnClickListener(view -> {
            RxBus.getInstance().send(new AnswerClickedEvent(question, answerItem));
        });
        if (answerItem.isCorrect() && answerItem.isSelected()) {
            myViewHolder.container.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.correct_rounded_drawable));
        } else if (answerItem.isSelected()) {
            myViewHolder.container.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.incorrect_rounded_drawable));
        } else {
            myViewHolder.container.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.grey_rounded_drawable));
        }

    }

    @Override
    public int getItemCount() {
        return answers.size();
    }

    public void clear() {
        this.answers.clear();
        notifyDataSetChanged();
    }

    public void update(List<Answer> answerList) {
        this.answers = answerList;
        notifyDataSetChanged();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        private ConstraintLayout container;
        private TextView answerTv;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            container = itemView.findViewById(R.id.container);
            answerTv = itemView.findViewById(R.id.answer_tv);
        }
    }
}