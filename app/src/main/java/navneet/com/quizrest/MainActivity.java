package navneet.com.quizrest;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements OptionSelected{

    private RecyclerView recyclerView;
    private Result data;
    private ArrayList<String> options=new ArrayList<>();
    private OptionsAdapter adapter;
    private TextView question,answer;
    private LiveDataTimerViewModel mLiveDataTimerViewModel;
    private Observer<Long> elapsedTimeObserver;
    private Button time_up;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        question=(TextView)findViewById(R.id.question);
        time_up=(Button)findViewById(R.id.time_up);
        mLiveDataTimerViewModel = ViewModelProviders.of(this).get(LiveDataTimerViewModel.class);

        subscribe();
//        answer=(TextView)findViewById(R.id.answer);
        initViews();
//        loadJSON();
    }

    private void subscribe() {
        elapsedTimeObserver = new Observer<Long>() {
            @Override
            public void onChanged(@Nullable final Long aLong) {
                if (aLong<=10) {
                    String newText = MainActivity.this.getResources().getString(
                            R.string.seconds, aLong);
                    ((TextView) findViewById(R.id.time)).setText(newText);
                    Log.d("ChronoActivity3", "Updating timer");
                } else {
                    time_up.setVisibility(View.VISIBLE);
                    mLiveDataTimerViewModel.getElapsedTime().removeObserver(elapsedTimeObserver);
                }
            }
        };

        mLiveDataTimerViewModel.getElapsedTime().observe((LifecycleOwner) this, elapsedTimeObserver);
    }

    private void initViews(){
        recyclerView = (RecyclerView)findViewById(R.id.card_recycler_view);
        recyclerView.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager=new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(gridLayoutManager);
        loadJSON();
    }



    private void loadJSON(){

        Retrofit retrofit = new Retrofit.Builder()

                .baseUrl("https://opentdb.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RequestInterface request = retrofit.create(RequestInterface.class);
        Call<AndroidVersion> call = request.getJSON();
        call.enqueue(new Callback<AndroidVersion>() {
            @Override
            public void onResponse(Call<AndroidVersion> call, Response<AndroidVersion> response) {

                AndroidVersion jsonResponse = response.body();
                int size=response.body().getResults().size();
                int newSize=getRandomSize(size);
                data = jsonResponse.getResults().get(newSize);
                options=data.getIncorrectAnswers();
                String correctans=data.getCorrectAnswer();
                options.add(correctans);
                Collections.shuffle(options);
                question.setText(data.getQuestion());
                adapter = new OptionsAdapter(options,MainActivity.this,correctans,MainActivity.this);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<AndroidVersion> call, Throwable t) {
                Log.d("Error",t.getMessage());
            }
        });
    }

    private int getRandomSize(int size) {
        Random r = new Random();
        int ran=r.nextInt(size);
        return ran;
    }

    @Override
    public void onOptionCorrect() {
        MyDialogFragment myDiag = new MyDialogFragment();
        myDiag.show(getFragmentManager(), "Diag");
    }

    @Override
    public void onOptionWrong() {

    }
}
