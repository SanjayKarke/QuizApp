package com.sanjay.quizapp.ui.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.sanjay.quizapp.R;
import com.sanjay.quizapp.database.dao.QuestionDao;
import com.sanjay.quizapp.model.Answer;
import com.sanjay.quizapp.model.Question;
import com.sanjay.quizapp.ui.adapters.MyPagerAdapter;
import com.sanjay.quizapp.ui.fragments.QuizFragment;
import com.sanjay.quizapp.utils.Apputils;
import com.sanjay.quizapp.utils.CustomSliderView;
import com.sanjay.quizapp.utils.rxbus.RxBus;
import com.sanjay.quizapp.utils.rxbus.events.AnswerClickedEvent;
import com.sanjay.quizapp.viewmodel.QuizViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    @Inject
    QuizViewModel quizViewModel;
    @Inject
    QuestionDao questionDao;

    private CompositeDisposable disposables = new CompositeDisposable();
    private long timerFullTime = 120000;
    private long intervalTime = 1000;
    private List<Question> questionList = new ArrayList<>();
    private ProgressBar progressBar;
    private Button startBtn;
    private CustomSliderView customSliderView;
    private ViewPager2 viewpager;
    private TextView timerCountTv;
    private Disposable disposable;
    private MyPagerAdapter adapter;
    private int count;
    private boolean hasTimerEnded;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        rxBusListener();
        getQuestions();
        //get data from api and save it to room db
        quizViewModel.questionLiveData.observe(this, questions -> {
            progressBar.setVisibility(View.GONE);
            clearDBAndStore(questions);
        });
        quizViewModel.errorObservable.observe(this, throwable -> {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(MainActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
        });

    }

    private void rxBusListener() {
        disposable = RxBus.getInstance()
                .toObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::accept);
        disposables.add(disposable);

    }

    private void clearDBAndStore(List<Question> questions) {
        new Thread(() -> {
            // Clear existing data
            questionDao.clearData();
            saveDataInToDB(questions);
        }).start();
    }

    private void initView() {
        progressBar = findViewById(R.id.progressBar);
        startBtn = findViewById(R.id.start_btn);
        startBtn.setOnClickListener(this);
        customSliderView = findViewById(R.id.custom_slider_view);
        customSliderView.setPercentage(0f);
        viewpager = findViewById(R.id.viewPager);
        timerCountTv = findViewById(R.id.timer_tv);
    }

    private void setupViewPager() {
        adapter = new MyPagerAdapter(this, questionList);
        viewpager.setAdapter(adapter);
        startTimer();

    }

    private void startTimer() {

           new CountDownTimer(timerFullTime, intervalTime) {
               @SuppressLint("DefaultLocale")
               public void onTick(long millisUntilFinished) {
                   Log.d("timer", "millisUntilFinished" + millisUntilFinished);
                   long secondsRemaining = millisUntilFinished / intervalTime;
                   timerCountTv.setText(String.format("%d sec remaining", secondsRemaining + 1));
                   customSliderView.setPercentage((float) millisUntilFinished / timerFullTime);

               }

               public void onFinish() {
                   hasTimerEnded = true;
                   onAnimationEnded();
               }
           }.start();
       }




    private void onAnimationEnded() {
        customSliderView.setPercentage(0f);
        timerCountTv.setText(getResources().getString(R.string.timeup));
        timerCountTv.setTextColor(getColor(R.color.red));
        new Handler().postDelayed(() -> {
            timerCountTv.setTextColor(getColor(R.color.colorPrimary));
        }, 2000);
        calculateCorrectAns();
    }

    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    private void calculateCorrectAns() {
        new Thread(() -> {
            questionList = questionDao.getAllQuestions();
        }).start();
        for (int i = 0; i < questionList.size(); i++) {
            Question question = questionList.get(i);
            for (int j = 0; j < question.getAnswerList().size(); j++) {
                Answer answer = question.getAnswerList().get(j);
                if (answer.isSelected() && answer.isCorrect()) {
                    count++;
                }
            }
        }
        timerCountTv.setText(String.format("Hurray!! You have answered %d/%d", count, questionList.size()));

        startBtn.setVisibility(View.VISIBLE);
        startBtn.setText("Play Again");
        questionList = null;
        getQuestions();
        rxBusListener();
    }


    private void saveDataInToDB(List<Question> questions) {
        Observable.fromIterable(questions)
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Question>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposables.add(d);
                    }

                    @Override
                    public void onNext(@NonNull Question question) {
                        List<Answer> answerList = new ArrayList<>();
                        for (int i = 0; i < question.getIncorrectAnswers().size(); i++) {
                            Answer answer = new Answer();
                            answer.setAns(question.getIncorrectAnswers().get(i));
                            answerList.add(answer);
                        }
                        Answer correctAns = new Answer();
                        correctAns.setAns(question.getCorrectAnswer());
                        correctAns.setCorrect(true);
                        answerList.add(correctAns);
                        Collections.shuffle(answerList);
                        question.setAnswerList(answerList);
                        questionDao.insert(question);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Toast.makeText(MainActivity.this, e.getMessage() + "", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {
                        questionList = questionDao.getAllQuestions();
                    }
                });
    }

    private void getQuestions() {
        if (Apputils.isNetworkConnected(this)) {
            /*do api call*/
            progressBar.setVisibility(View.VISIBLE);
            quizViewModel.getQuestions();
        } else {
            /*get data from roomdb*/
            Single.fromCallable(() -> {
                        questionList = questionDao.getAllQuestions();
                        return questionList;
                    })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new SingleObserver<List<Question>>() {
                        @Override
                        public void onSubscribe(@NonNull Disposable d) {
                            disposables.add(d);
                        }

                        @Override
                        public void onSuccess(@NonNull List<Question> aBoolean) {
                            List<Question> questions = new ArrayList<>();
                            for (int i = 0; i < questionList.size(); i++) {
                                Question question = questionList.get(i);
                                for (int j = 0; j < question.getAnswerList().size(); j++) {
                                    question.getAnswerList().get(j).setSelected(false);
                                }
                                questions.add(question);
                            }
                            saveNewData(questions);

                        }

                        @Override
                        public void onError(@NonNull Throwable e) {

                        }
                    });
        }

    }

    private void saveNewData(List<Question> questions) {
        Observable.fromIterable(questions).subscribeOn(Schedulers.io()).subscribe(question -> questionDao.insert(question));
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.start_btn) {
            if(hasTimerEnded){
                hasTimerEnded = false;
                timerCountTv.setText("");
                timerCountTv.setTextColor(getColor(R.color.white));

            }
            if (questionList != null && questionList.size() > 0) {
                setupViewPager();
                startBtn.setVisibility(View.GONE);
            } else {
                Toast.makeText(this, R.string.no_data_saved_msg, Toast.LENGTH_SHORT).show();
            }


        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposables.dispose();
    }

    private void accept(Object event) {
        int pageIndex = viewpager.getCurrentItem();
//        Log.d(TAG, "current page index::" + pageIndex);
//        Log.d(TAG, "number of question::" + questionList.size());
        if (event instanceof AnswerClickedEvent) {
            if (!hasTimerEnded) {
                Question questionToupdate = ((AnswerClickedEvent) event).getQuestion();
                Answer selectedAns = ((AnswerClickedEvent) event).getAnswerItem();
                for (int i = 0; i < questionToupdate.getAnswerList().size(); i++) {
                    questionToupdate.getAnswerList().get(i).setSelected(questionToupdate.getAnswerList().get(i).getAns().equals(selectedAns.getAns()));
                }
                Single.fromCallable(() -> {
                            questionDao.insert(questionToupdate);
                            return true;
                        })
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new SingleObserver<Boolean>() {
                            @Override
                            public void onSubscribe(@NonNull Disposable d) {
                                disposables.add(d);
                            }

                            @Override
                            public void onSuccess(@NonNull Boolean aBoolean) {
                                if (aBoolean && adapter != null) {
                                    QuizFragment quizFragment = adapter.getFragment(viewpager.getCurrentItem());
                                    quizFragment.updateAnswer(questionToupdate.getAnswerList());
                                }
                                new Handler().postDelayed(() -> {
                                    viewpager.setCurrentItem(pageIndex + 1);
                                }, 1000);

                            }

                            @Override
                            public void onError(@NonNull Throwable e) {
                                e.printStackTrace();
                            }
                        });
            } else {
                Toast.makeText(this, "Time Up!!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}