
package com.shuja1497.wordstack;

import android.content.res.AssetManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

public class MainActivity extends AppCompatActivity {

    private static final int WORD_LENGTH = 5;
    public static final int LIGHT_BLUE = Color.rgb(176, 200, 255);
    public static final int LIGHT_GREEN = Color.rgb(200, 255, 200);
    private ArrayList<String> words = new ArrayList<>();
    private Random random = new Random();
    private StackedLayout stackedLayout;
    private String word1, word2;
    private View word1LinearLayout, word2LinearLayout;
    private Stack<LetterTile> placed_tiles_stack;

    public static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AssetManager assetManager = getAssets();
        try {
            InputStream inputStream = assetManager.open("words.txt");
            BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
            String line = null;
            while((line = in.readLine()) != null) {
                String word = line.trim();
                if(word.length()<=WORD_LENGTH){
                    words.add(word);
                }
            }
        } catch (IOException e) {
            Toast toast = Toast.makeText(this, "Could not load dictionary", Toast.LENGTH_LONG);
            toast.show();
        }
        LinearLayout verticalLayout = (LinearLayout) findViewById(R.id.vertical_layout);
        stackedLayout = new StackedLayout(this);
        verticalLayout.addView(stackedLayout, 3);

        word1LinearLayout = findViewById(R.id.word1);
        word1LinearLayout.setOnTouchListener(new TouchListener());
        word1LinearLayout.setOnDragListener(new DragListener());
        word2LinearLayout = findViewById(R.id.word2);
        word2LinearLayout.setOnTouchListener(new TouchListener());
        word2LinearLayout.setOnDragListener(new DragListener());
    }

    private class TouchListener implements View.OnTouchListener {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN && !stackedLayout.empty()) {
                LetterTile tile = (LetterTile) stackedLayout.peek();
                tile.moveToViewGroup((ViewGroup) v);
                if (stackedLayout.empty()) {
                    TextView messageBox = (TextView) findViewById(R.id.message_box);
                    messageBox.setText(word1 + " " + word2);
                }
                placed_tiles_stack.push(tile);
                return true;
            }
            return false;
        }
    }

    private class DragListener implements View.OnDragListener {

        public boolean onDrag(View v, DragEvent event) {
            int action = event.getAction();
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    v.setBackgroundColor(LIGHT_BLUE);
                    v.invalidate();
                    return true;
                case DragEvent.ACTION_DRAG_ENTERED:
                    v.setBackgroundColor(LIGHT_GREEN);
                    v.invalidate();
                    return true;
                case DragEvent.ACTION_DRAG_EXITED:
                    v.setBackgroundColor(LIGHT_BLUE);
                    v.invalidate();
                    return true;
                case DragEvent.ACTION_DRAG_ENDED:
                    v.setBackgroundColor(Color.WHITE);
                    v.invalidate();
                    return true;
                case DragEvent.ACTION_DROP:
                    // Dropped, reassign Tile to the target Layout
                    LetterTile tile = (LetterTile) event.getLocalState();
                    tile.moveToViewGroup((ViewGroup) v);
                    if (stackedLayout.empty()) {
                        TextView messageBox = (TextView) findViewById(R.id.message_box);
                        messageBox.setText(word1 + " " + word2);
                    }
                    //pushing
                    placed_tiles_stack.push(tile);
                    return true;
            }
            return false;
        }
    }

    public boolean onStartGame(View view) {
        TextView messageBox = (TextView) findViewById(R.id.message_box);
        messageBox.setText("Game started");


        ((ViewGroup)word1LinearLayout).removeAllViews();
        ((ViewGroup)word2LinearLayout).removeAllViews();
        stackedLayout.clear();


        placed_tiles_stack = new Stack<>();
        int wordListSize  = words.size();
        int firstWordIndex = random.nextInt(wordListSize)+1;
        int secondWordIndex = random.nextInt(wordListSize)+1;
        while (firstWordIndex==secondWordIndex)
            secondWordIndex = random.nextInt(wordListSize)+1;

        word1 = words.get(firstWordIndex);
        word2 = words.get(secondWordIndex);

        int wordCounter1 = 0;
        int wordCounter2 = 0;

        int which_word;
        String scrambledWord = "";

        while(wordCounter1!=word1.length() && wordCounter2!=word2.length())
        {
            which_word = random.nextInt(2)+1;
            switch (which_word){

                case 1:
                    scrambledWord += word1.charAt(wordCounter1);
                    wordCounter1++;
                    break;
                case 2:
                    scrambledWord += word2.charAt(wordCounter2);
                    wordCounter2++;
                    break;
            }
        }

        if (wordCounter1 == word1.length()){
            scrambledWord += word2.substring(wordCounter2, word2.length());
        }
        else{
            scrambledWord += word1.substring(wordCounter1, word1.length());

        }

        messageBox.setText(scrambledWord);

        for(int i = scrambledWord.length()-1;i >= 0; i--){
            LetterTile letterTile = new LetterTile(this, scrambledWord.charAt(i));
            stackedLayout.push(letterTile);
        }

        Log.e("onStartGame: word1 ",word1 );
        Log.e("onStartGame: word2", word2 );
        return true;
    }

    public boolean onUndo(View view) {

        LetterTile recentTile = placed_tiles_stack.pop();
        recentTile.moveToViewGroup(stackedLayout);
        return true;
    }
}
