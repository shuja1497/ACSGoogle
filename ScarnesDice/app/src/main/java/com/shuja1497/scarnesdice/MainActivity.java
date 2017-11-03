package com.shuja1497.scarnesdice;

import android.content.Context;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Currency;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String TAG = "MAinActivity";

    TextView tv_player_score , tv_computer_score, turn , tv_userCurrentScore, tv_compCurrentScore;

    Button roll , hold, reset;

    ImageView dice;
    boolean compturn, userTurn;

    int userCurrentScore , userTotalScore, compCurrentScore , compTotalScore , dicenum;

    Context context;

    int dice_images[] = {R.drawable.dice1,
            R.drawable.dice2,
            R.drawable.dice3,
            R.drawable.dice4,
            R.drawable.dice5,
            R.drawable.dice6};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userCurrentScore=0;
        userTotalScore=0;
        compCurrentScore=0;
        compTotalScore=0;


        userTurn = true;
        compturn = false;

        tv_player_score = findViewById(R.id.player_score);
        tv_computer_score = findViewById(R.id.computer_score);
        tv_compCurrentScore = findViewById(R.id.comp_current_score_tv);
        tv_userCurrentScore = findViewById(R.id.user_current_score_tv);
        turn = findViewById(R.id.turn);

        roll = findViewById(R.id.button_roll);
        hold= findViewById(R.id.button_hold);
        reset = findViewById(R.id.button_reset);

        dice = findViewById(R.id.dice);

        roll.setOnClickListener(this);
        hold.setOnClickListener(this);
        reset.setOnClickListener(this);

        context = getApplicationContext();

    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {

            case R.id.button_roll:
                makeRoll();
                updateUI();
                break;

            case R.id.button_hold:
                Log.d(TAG, "onClick: user current score is"+userCurrentScore);
                userTotalScore += userCurrentScore;
                Log.d(TAG, "onClick: user total score is"+userTotalScore);
                userCurrentScore=0;
                userTurn=false;
                compturn=true;
                updateUI();
                break;

            case R.id.button_reset:
                userCurrentScore=0;
                userTotalScore=0;
                compCurrentScore=0;
                compTotalScore=0;
                userTurn=true;
                compturn=false;
                updateUI();
                break;

        }

    }

    private void updateUI() {

        Log.d(TAG, "updateUI: total score "+userTotalScore);
        String userString = String.valueOf(userTotalScore);
        String compString = String.valueOf(compTotalScore);
        String userCurrentString = String.valueOf(userCurrentScore);
        String compCurrentString = String.valueOf(compCurrentScore);

        tv_player_score.setText(userString);
        tv_computer_score.setText(compString);

        tv_userCurrentScore.setText(userCurrentString);
        tv_compCurrentScore.setText(compCurrentString);

        if (userTotalScore>=50 || compTotalScore>= 50){
            userTurn=false;
            compturn=false;
            won();
        }

        if(userTurn){
            turn.setText("Your Turn");
            hold.setEnabled(true);
            roll.setEnabled(true);

        } else if(compturn){
//            turn.setText("Computer's turn");
//            hold.setEnabled(false);
//            roll.setEnabled(false);
            Log.d(TAG, "updateUI: going into comp Turn");
            computerTurn();
        }

    }

    private void computerTurn() {

        Log.d(TAG, "computerTurn: ------------inside computerTurn----------");

        int i =0 ;
        while(i<3){
            makeRoll();
            i++;
        }


//        int i =0;
//        while(i<3){
//            Handler h = new Handler();
//            h.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    Log.d(TAG, "run: comps turn will be rolled");
//                    turn.setText("Computer's turn");
//                    hold.setEnabled(false);
//                    roll.setEnabled(false);
//                    makeRoll();
//                }
//            },3000);
//            i++;
//        }

        Log.d(TAG, "computerTurn: this is taking place");
        compTotalScore += compCurrentScore;
        Log.d(TAG, "computerTurn: comp total score is "+compTotalScore);
        userTurn=true;
        compturn=false;
        compCurrentScore=0;
        updateUI();

    }


    private void won() {

        if(userTotalScore>=50){
            roll.setEnabled(false);
            hold.setEnabled(false);
            Animation anim = AnimationUtils.loadAnimation(this, R.anim.won);
            turn.setText("You Won");
            turn.startAnimation(anim);
        }

        else if(compTotalScore>=50){
            roll.setEnabled(false);
            hold.setEnabled(false);
            Animation anim = AnimationUtils.loadAnimation(this, R.anim.won);
            turn.setText("Computer Won");
            turn.startAnimation(anim);
        }

    }

    private void makeRoll() {

        Random random = new Random();
        dicenum = random.nextInt(6)+1;
//        dice.setImageResource(dice_images[dicenum-1]);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.rotate_dice);
        dice.startAnimation(animation);
        dice.setImageResource(dice_images[dicenum-1]);


        if(dicenum == 1){
            if(userTurn){
            userCurrentScore = 0;
            userTurn = false;
            compturn=true;
            updateUI();
            }
            else {
                Log.d(TAG, "makeRoll: comp got 1 ");
                compCurrentScore=0;
                compturn=false;
                userTurn=true;
                updateUI();
            }

        }
        else {
            if(userTurn)
                userCurrentScore += dicenum;
            else{
                Log.d(TAG, "makeRoll: comp current roll is "+dicenum);
                compCurrentScore += dicenum;}
        }

    }
}
