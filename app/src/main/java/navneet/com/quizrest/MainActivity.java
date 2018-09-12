package navneet.com.quizrest;

import android.app.AlertDialog;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.github.javiersantos.materialstyleddialogs.enums.Style;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import okhttp3.OkHttpClient;
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
    private TextView question,answer,points_t,que_count,correct_count,score;
    private LiveDataTimerViewModel mLiveDataTimerViewModel;
    private Observer<Long> elapsedTimeObserver;
    private Button time_up;
    private boolean checked=false,showAns=false;
    CountDownTimer timer;
    private ProgressBar progressBar,progressLine;
    private String correctans;
    private int time_pts=0,que_no=0,cor_count=0;
    Animation shake;
    private ImageButton clock,trophy;
    private ImageView response_gif;
    private FloatingActionButton cat_switch;
    private long mLastClickTime = 0,mLastClickTime1 = 0,fabLastCheck = 0;

    private BottomSheetBehavior mBottomSheetBehavior;
    private RecyclerView mRecyclerView;
    private View bottomSheet;

    private RelativeLayout quiz_layout;
    private TextView error_view;
    private FirebaseAuth mAuth;
    private DatabaseReference ref;
    private String userID="";
    private String category="9";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth=FirebaseAuth.getInstance();
        ref = FirebaseDatabase.getInstance().getReference();

        FirebaseUser user=mAuth.getCurrentUser();

        if (user!=null) {
            userID=user.getUid();
        }

        Intent intent=getIntent();
        if (intent!=null){
            category=intent.getStringExtra("quiz_cat");
        } else {
            category="9";
        }

        shake = AnimationUtils.loadAnimation(this, R.anim.shakeanim);

        quiz_layout = (RelativeLayout) findViewById(R.id.quiz_layout);
        bottomSheet = findViewById(R.id.bottom_sheet_new);
        mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        error_view=(TextView)findViewById(R.id.error_view);
//        cat_switch =(FloatingActionButton) findViewById(R.id.switch_cat) ;
//        cat_switch.setVisibility(View.GONE);
        question=(TextView)findViewById(R.id.question);
        score=(TextView)findViewById(R.id.score);
        points_t=(TextView)findViewById(R.id.points);
        que_count=(TextView)findViewById(R.id.que_count);
        correct_count=(TextView)findViewById(R.id.correct_count);
        clock=(ImageButton)findViewById(R.id.clock);
        trophy=(ImageButton)findViewById(R.id.trophy);

        response_gif=(ImageView)findViewById(R.id.response);

//        cat_switch.setOnClickListener(new_high View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (SystemClock.elapsedRealtime() - fabLastCheck < 1000){
//                    return;
//                }
//                fabLastCheck = SystemClock.elapsedRealtime();
////                que_no =0;
////                que_count.setText("0");
////                points_t.setText("0");
////                time_pts=0;
////                correct_count.setText("0");
//                if (que_no > 10) {
//                    progressBar.setVisibility(View.GONE);
//                    response_gif.setVisibility(View.GONE);
//                    new_high MaterialStyledDialog.Builder(MainActivity.this)
//                            .setTitle("Great Work !!")
//                            .setStyle(Style.HEADER_WITH_ICON)
//                            .setIcon(R.drawable.trophy_big)
//                            .setDescription("Your Score is " + String.valueOf(time_pts) + "\n Do you prefer to start over again ?")
//                            .setPositiveText("Sure")
//                            .onPositive(new_high MaterialDialog.SingleButtonCallback() {
//                                @Override
//                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
//                                    resetquiz();
//                                }
//                            })
//                            .onNegative(new_high MaterialDialog.SingleButtonCallback() {
//                                @Override
//                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
//
//                                }
//                            })
//                            .setNegativeText("Later")
//                            .show();
//                    Toast.makeText(MainActivity.this, "Score   : " + String.valueOf(time_pts), Toast.LENGTH_SHORT).show();
//                } else {
//                    progressBar.setVisibility(View.VISIBLE);
//                    loadJSON("21");
//                }
//            }
//        });

        time_up=(Button)findViewById(R.id.time_up);
        progressBar=(ProgressBar)findViewById(R.id.progress);
        progressLine=(ProgressBar)findViewById(R.id.progress_line);

        mLiveDataTimerViewModel = ViewModelProviders.of(this).get(LiveDataTimerViewModel.class);
        time_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                progressBar.setVisibility(View.VISIBLE);
                loadJSON(category);
            }
        });

//        customToast("This is a custom toast","");
//        answer=(TextView)findViewById(R.id.answer);
        if (Connectivity.isConnected(MainActivity.this)) {
            initViews();
        } else {
            quiz_layout.setVisibility(View.GONE);
            error_view.setVisibility(View.VISIBLE);
        }
//        loadJSON();
    }

    private void customToast(String message,String state) {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.costom_toast,
                (ViewGroup) findViewById(R.id.custom_toast_container));

        Typeface typface= Typeface.createFromAsset(getAssets(),"raleway.ttf");

        TextView text = (TextView) layout.findViewById(R.id.text);
        ImageView status = (ImageView) layout.findViewById(R.id.status);
        text.setText(message);
        text.setTypeface(typface);

        if (state.equals("happy")){
            status.setImageResource(R.drawable.trophy_big);
        } else if (state.equals("sad")){
            status.setImageResource(R.drawable.sad);
        } else {
            status.setImageResource(R.drawable.trophy_big);
        }

        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.TOP, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        new_high MaterialStyledDialog.Builder(MainActivity.this)
//                .setTitle("Quit Quiz")
//                .setStyle(Style.HEADER_WITH_ICON)
//                .setIcon(R.drawable.trophy_big)
//                .setDescription("Do you prefer to quit the quiz ?")
//                .setPositiveText("Save Progress")
//                .onPositive(new_high MaterialDialog.SingleButtonCallback() {
//                    @Override
//                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
//                        finish();
//                    }
//                })
//                .onNegative(new_high MaterialDialog.SingleButtonCallback() {
//                    @Override
//                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
//
//                    }
//                })
//                .setNegativeText("Don't save")
//                .show();

    }

    private void startCountdown(){
        final int[] i = {9};
        progressLine.setProgress(i[0]);
        timer=new CountDownTimer(10000, 1000) {

            public void onTick(long millisUntilFinished) {
                i[0]--;
                progressLine.setProgress((int)i[0]*100/(10000/1000));
                ((TextView) findViewById(R.id.time)).setText(" " + millisUntilFinished / 1000);
                //here you can have your logic to set text to edittext
                if (i[0]<4){
                 clock.setAnimation(shake);
//                 clock.setBackgroundColor(Color.RED);
                }
            }

            public void onFinish() {
                ((TextView) findViewById(R.id.time)).setText("Time's UP!");
                time_up.setVisibility(View.VISIBLE);
//                cat_switch.setVisibility(View.VISIBLE);
                response_gif.setVisibility(View.VISIBLE);
                Glide.with(getApplicationContext()).asGif().load(R.drawable.sarcasm).into(response_gif);
//                showAns=true;
//                adapter.notifyDataSetChanged();
                adapter = new OptionsAdapter(options,MainActivity.this,correctans,MainActivity.this,checked,true,true);
                recyclerView.setAdapter(adapter);
            }

        }.start();
    }


    private void subscribe() {
        elapsedTimeObserver = new Observer<Long>() {
            @Override
            public void onChanged(@Nullable final Long aLong) {
                if (aLong>=9) {
                    time_up.setVisibility(View.VISIBLE);
                    mLiveDataTimerViewModel.getElapsedTime().removeObserver(elapsedTimeObserver);
                }
                    String newText = MainActivity.this.getResources().getString(
                            R.string.seconds, aLong);
                    ((TextView) findViewById(R.id.time)).setText(newText);
                    Log.d("ChronoActivity3", "Updating timer");
//                } else {

//                    mLiveDataTimerViewModel.getElapsedTime().removeObserver(elapsedTimeObserver);
//                }
            }
        };

        mLiveDataTimerViewModel.getElapsedTime().observe((LifecycleOwner) this, elapsedTimeObserver);
    }

    private void initViews(){
        recyclerView = (RecyclerView)findViewById(R.id.card_recycler_view);
        recyclerView.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager=new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(gridLayoutManager);
        loadJSON(category);
    }



    private void loadJSON(String category){

        OkHttpClient client=new OkHttpClient();
        try {
            client = new OkHttpClient.Builder()
                    .sslSocketFactory(new TLSSocketFactory())
                    .build();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        Retrofit retrofit = new Retrofit.Builder()

                .baseUrl("https://opentdb.com/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RequestInterface request = retrofit.create(RequestInterface.class);
        Call<AndroidVersion> call = request.getJSON(category);
        call.enqueue(new Callback<AndroidVersion>() {
            @Override
            public void onResponse(Call<AndroidVersion> call, Response<AndroidVersion> response) {
                if (response.body()!=null) {
                    que_no += 1;
                    if (que_no > 10) {
                        response_gif.setVisibility(View.GONE);
                        if (!userID.equals("")){
                            final boolean[] dShown = {true};
//                            ref.child("users").child(userID).setValue(String.valueOf(time_pts));
                            ref.child("users").child(userID).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    progressBar.setVisibility(View.GONE);
                                    if(dataSnapshot.exists()){
                                        int statscore=100000000;
                                        int highscore= Integer.parseInt(dataSnapshot.child("highScore").getValue().toString());
                                        int total_correct= Integer.parseInt(dataSnapshot.child("totalCorrect").getValue().toString());
                                        int descOrder= Integer.parseInt(dataSnapshot.child("descOrder").getValue().toString());
                                        int add_correct=total_correct+cor_count;
                                        int descOrder_correct=statscore-time_pts;
                                        cor_count=0;
//                                        if (total_correct!=add_correct)
                                        ref.child("users").child(userID).child("totalCorrect").setValue(String.valueOf(add_correct));
                                        if (time_pts>highscore){
                                            ref.child("users").child(userID).child("highScore").setValue(String.valueOf(time_pts));
                                            ref.child("users").child(userID).child("descOrder").setValue(String.valueOf(descOrder_correct));
                                            Toast.makeText(MainActivity.this,"New High Score!",Toast.LENGTH_SHORT).show();
//                                            time_pts=0;
                                            if (dShown[0]) {
                                                dShown[0] =false;
                                                new MaterialStyledDialog.Builder(MainActivity.this)
                                                        .setTitle("Congratz, your new best score !!!")
                                                        .setStyle(Style.HEADER_WITH_ICON)
                                                        .setIcon(R.drawable.trophy_big)
                                                        .setDescription("Your Score is " + String.valueOf(time_pts) + "\n Do you prefer to start over again ?")
                                                        .setPositiveText("Sure")
                                                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                                                            @Override
                                                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                                                if (SystemClock.elapsedRealtime() - mLastClickTime1 < 1000) {
                                                                    return;
                                                                }
                                                                mLastClickTime1 = SystemClock.elapsedRealtime();
                                                                resetquiz();
                                                            }
                                                        })
                                                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                                                            @Override
                                                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                                                Intent intent=new Intent(MainActivity.this,GoogleSignInActivity.class);
                                                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                                startActivity(intent);
                                                            }
                                                        })
                                                        .setNegativeText("Later")
                                                        .show();
//                                                time_pts = 0;
                                            }
                                        } else {
                                            if (dShown[0]) {
                                                dShown[0] = false;
                                                new MaterialStyledDialog.Builder(MainActivity.this)
                                                        .setTitle("Great Work !!")
                                                        .setStyle(Style.HEADER_WITH_ICON)
                                                        .setIcon(R.drawable.trophy_big)
                                                        .setDescription("Your Score is " + String.valueOf(time_pts) + "\n Do you prefer to start over again ?")
                                                        .setPositiveText("Sure")
                                                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                                                            @Override
                                                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                                                if (SystemClock.elapsedRealtime() - mLastClickTime1 < 1000) {
                                                                    return;
                                                                }
                                                                mLastClickTime1 = SystemClock.elapsedRealtime();
                                                                resetquiz();
                                                            }
                                                        })
                                                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                                                            @Override
                                                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                                                Intent intent=new Intent(MainActivity.this,GoogleSignInActivity.class);
                                                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                                startActivity(intent);
                                                            }
                                                        })
                                                        .setNegativeText("Later")
                                                        .show();
                                                Toast.makeText(MainActivity.this, "Score   : " + String.valueOf(time_pts), Toast.LENGTH_SHORT).show();
//                                                time_pts = 0;
                                            }
                                        }
                                        // use "username" already exists

                                    } else {
                                        // "username" does not exist yet.
//                                        User user = new_high User(name, email,"0");
//
//                                        ref.child("users").child(userId).setValue(user);
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    progressBar.setVisibility(View.GONE);
                                }
                            });
                        }
//                        new_high MaterialStyledDialog.Builder(MainActivity.this)
//                                .setTitle("Great Work !!")
//                                .setStyle(Style.HEADER_WITH_ICON)
//                                .setIcon(R.drawable.trophy_big)
//                                .setDescription("Your Score is " + String.valueOf(time_pts) + "\n Do you prefer to start over again ?")
//                                .setPositiveText("Sure")
//                                .onPositive(new_high MaterialDialog.SingleButtonCallback() {
//                                    @Override
//                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
//                                        resetquiz();
//                                    }
//                                })
//                                .onNegative(new_high MaterialDialog.SingleButtonCallback() {
//                                    @Override
//                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
//
//                                    }
//                                })
//                                .setNegativeText("Later")
//                                .show();
//                        Toast.makeText(MainActivity.this, "Score   : " + String.valueOf(time_pts), Toast.LENGTH_SHORT).show();
                    } else {
                        score.setVisibility(View.INVISIBLE);
                        time_up.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);
                        response_gif.setVisibility(View.GONE);

                        que_count.setText(String.valueOf(que_no));
                        String ques = "";
                        AndroidVersion jsonResponse = response.body();
                        int size = response.body().getResults().size();
                        int newSize = getRandomSize(size);
                        data = jsonResponse.getResults().get(newSize);
                        options = data.getIncorrectAnswers();
                        correctans = data.getCorrectAnswer();
                        options.add(correctans);
                        Collections.shuffle(options);
//                try {
//                    ques= URLDecoder.decode(data.getQuestion(), "ISO-8859-1");
//                } catch (UnsupportedEncodingException e) {
//                    e.printStackTrace();
//                }
//                question.setText(ques.replace("&quot;","\""));
                        question.setText(Html.fromHtml(data.getQuestion()));
                        adapter = new OptionsAdapter(options, MainActivity.this, correctans, MainActivity.this, checked, false, false);
                        recyclerView.setAdapter(adapter);
                        startCountdown();
                    }
                } else {
                    new MaterialStyledDialog.Builder(MainActivity.this)
                            .setTitle("Oops.. !!")
                            .setStyle(Style.HEADER_WITH_ICON)
                            .setIcon(R.drawable.sad)
                            .setDescription("Something went wrong, please try after some time !!")
                            .setPositiveText("RETRY")
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    resetquiz();
                                }
                            })
                            .onNegative(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    Intent intent=new Intent(MainActivity.this,GoogleSignInActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                }
                            })
                            .setNegativeText("LATER")
                            .show();
                }
            }

            @Override
            public void onFailure(Call<AndroidVersion> call, Throwable t) {
                Log.d("Error",t.getMessage());
                progressBar.setVisibility(View.GONE);
                quiz_layout.setVisibility(View.GONE);
                error_view.setVisibility(View.VISIBLE);
            }
        });
    }

    private void resetquiz() {
        que_no =0;
        cor_count=0;
        que_count.setText("0");
        time_pts=0;
        points_t.setText("0");
        correct_count.setText("0");
        progressBar.setVisibility(View.VISIBLE);
        loadJSON(category);
    }

    private int getRandomSize(int size) {
        Random r = new Random();
        int ran=r.nextInt(size);
        return ran;
    }

    @Override
    public void onOptionCorrect() {
//        customToast("nailed it!","happy");
//        if (que_no==10){
//            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
//        }
//        cat_switch.setVisibility(View.VISIBLE);
        time_up.setText("NEXT");
        trophy.setAnimation(shake);
        response_gif.setVisibility(View.VISIBLE);
        score.setVisibility(View.VISIBLE);
        timer.cancel();
        cor_count+=1;
        int cur_pt=Integer.parseInt(((TextView) findViewById(R.id.time)).getText().toString().trim());
        if (cur_pt>=6){
            Glide.with(MainActivity.this).asGif().load(R.drawable.cool).into(response_gif);
        } else if (cur_pt>3 && cur_pt<6){
            Glide.with(MainActivity.this).asGif().load(R.drawable.just_cleared).into(response_gif);
        } else {
            Glide.with(MainActivity.this).asGif().load(R.drawable.giphy).into(response_gif);
        }
        if (cur_pt<6){
            score.setText("+5");
            time_pts += 5;
            points_t.setText(String.valueOf(time_pts));
        } else {
            score.setText("+" + ((TextView) findViewById(R.id.time)).getText().toString().trim());
            time_pts += Integer.parseInt(((TextView) findViewById(R.id.time)).getText().toString().trim());
            points_t.setText(String.valueOf(time_pts));
        }
        correct_count.setText(String.valueOf(cor_count));
        time_up.setVisibility(View.VISIBLE);
        adapter.notifyDataSetChanged();
//        timer.onFinish();
//        MyDialogFragment myDiag = new_high MyDialogFragment();
//        myDiag.show(getFragmentManager(), "Diag");
//        AlertDialog.Builder builder;
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            builder = new_high AlertDialog.Builder(MainActivity.this, android.R.style.Theme_Material_Dialog_Alert);
//        } else {
//            builder = new_high AlertDialog.Builder(MainActivity.this);
//        }
//        builder.setTitle("Correct Answer !!!")
//                .setMessage("Try another one ?")
//                .setPositiveButton(android.R.string.yes, new_high DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        progressBar.setVisibility(View.VISIBLE);
//                       loadJSON();
//                    }
//                })
//                .setNegativeButton(android.R.string.no, new_high DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        // do nothing
//                        dialog.dismiss();
//                    }
//                })
//                .setIcon(android.R.drawable.ic_dialog_alert)
//                .show();
    }

    @Override
    public void onOptionWrong() {
//        cat_switch.setVisibility(View.VISIBLE);
        time_up.setText("NEXT");
//        customToast("sorry :(","sad");
//        if (que_no==10){
//            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
//        }
        response_gif.setVisibility(View.VISIBLE);
        timer.cancel();
        Glide.with(MainActivity.this).asGif().load(R.drawable.not_good).into(response_gif);
        time_up.setVisibility(View.VISIBLE);
//        timer.onFinish();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onSectionComplete() {

    }
}
