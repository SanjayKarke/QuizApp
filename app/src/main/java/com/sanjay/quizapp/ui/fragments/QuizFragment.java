package com.sanjay.quizapp.ui.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sanjay.quizapp.R;
import com.sanjay.quizapp.model.Answer;
import com.sanjay.quizapp.model.Question;
import com.sanjay.quizapp.ui.adapters.AnswerAdapter;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link QuizFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class QuizFragment extends Fragment {

    private static final String ARG_PARAM1 = "question_list";
    private static final String ARG_PARAM2 = "position";
    private Question question;
    private TextView questionTv;
    private RecyclerView optionsRv;
    private AnswerAdapter adapter;
    private int position;

    public QuizFragment() {
        // Required empty public constructor
    }

    public static QuizFragment newInstance(Question param1, int position) {
        QuizFragment fragment = new QuizFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM1, param1);
        args.putInt(ARG_PARAM2, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            question = getArguments().getParcelable(ARG_PARAM1);
            position = getArguments().getInt(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_quiz, container, false);
        questionTv = view.findViewById(R.id.question_tv);
        optionsRv = view.findViewById(R.id.answer_list);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        questionTv.setText(question.getQuestion());
        adapter = new AnswerAdapter(requireContext(), question, question.getAnswerList());
        optionsRv.setLayoutManager(new LinearLayoutManager(getActivity()));
        optionsRv.setAdapter(adapter);

    }

    public void updateAnswer(List<Answer> answerList) {
        if (adapter != null) {
            adapter.update(answerList);
        }
    }
}