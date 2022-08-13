package com.example.mycalculator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    public final int MENU_PLUS = 0;
    public final int MENU_MINUS = 1;
    public final int MENU_DIVISION = 2;
    public final int MENU_MULTIPLICATION = 3;

    private int decided = 0;
    private int repeat = -1;

    private ArrayList<String> mathSigns;
    private Random random;

    private TextView textViewExample;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView textViewCorrect = findViewById(R.id.textViewCorrect);
        TextView textViewWrong = findViewById(R.id.textViewWrong);
        textViewExample = findViewById(R.id.textViewExample);
        EditText editTextAnswer = findViewById(R.id.editTextAnswer);
        Button buttonAnswer = findViewById(R.id.buttonAnswer);
        Button buttonCreate = findViewById(R.id.buttonCreate);
        Button buttonChooseMathSings = findViewById(R.id.buttonChooseMathSings);

        random = new Random();
        mathSigns = new ArrayList<>(4);
        Collections.addAll(mathSigns, "+", "-", ":", "×");

        buttonChooseMathSings.setOnClickListener(l -> {
            registerForContextMenu(buttonChooseMathSings);
            openContextMenu(buttonChooseMathSings);
        });

        buttonAnswer.setOnClickListener(l -> {
            String answerString = editTextAnswer.getText().toString();
            if(answerString.equals("")) {
                String text = getString(R.string.waringForUserNull);
                waring(this, text, Toast.LENGTH_LONG);
                return;
            }
            Integer answerUser = Integer.valueOf(answerString);

            if(answerUser.equals(decided)) {
                textViewCorrect.setVisibility(View.VISIBLE);
                textViewWrong.setVisibility(View.GONE);
            } else {
                textViewCorrect.setVisibility(View.GONE);
                textViewWrong.setVisibility(View.VISIBLE);
            }
        });

        buttonCreate.setOnClickListener(l -> {
            if(repeat == -1) {
                String text = getString(R.string.waringForUser);
                waring(this, text, Toast.LENGTH_LONG);
                return;
            }
            textViewExample.setText(example(99, 99, repeat));
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId() == R.id.buttonChooseMathSings) {
            menu.setHeaderTitle("Math signs");
            menu.add(menu.NONE, MENU_MULTIPLICATION, menu.NONE, "\"" + mathSigns.get(3) + "\"" + " multiplication");
            menu.add(menu.NONE, MENU_DIVISION, menu.NONE, "\"" + mathSigns.get(2) + "\"" + " division");
            menu.add(menu.NONE, MENU_MINUS, menu.NONE, "\"" + mathSigns.get(1) + "\"" + " subtraction");
            menu.add(menu.NONE, MENU_PLUS, menu.NONE, "\"" + mathSigns.get(0) + "\"" + " addition");
        }
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case MENU_MULTIPLICATION:
                textViewExample.setText(example(99, 99, MENU_MULTIPLICATION));
                repeat = 3;
            break;

            case MENU_PLUS:
                textViewExample.setText(example(99, 99, MENU_PLUS));
                repeat = 0;
            break;

            case MENU_DIVISION:
                textViewExample.setText(example(99, 99, MENU_DIVISION));
                repeat = 2;
            break;

            case MENU_MINUS:
                textViewExample.setText(example(99, 99, MENU_MINUS));
                repeat = 1;
            break;
        }
        return super.onContextItemSelected(item);
    }

    @NonNull
    private String example(int first, int second, int sign) {
        int firstNumber = random.nextInt(first)+1;
        int secondNumber = random.nextInt(second)+1;
        String signString = mathSigns.get(sign);

        {
            if (sign == MENU_MULTIPLICATION) {
                decided = firstNumber * secondNumber;
            } else if(sign == MENU_PLUS) {
                decided = firstNumber + secondNumber;
            }
        }

        if(sign == MENU_MINUS) {
            if(firstNumber < secondNumber) {
                int order = firstNumber;
                firstNumber = secondNumber;
                secondNumber = order;
            }
            decided = firstNumber - secondNumber;
        } else if(sign == MENU_DIVISION) {
            boolean tmp;
            do {
                if (!(firstNumber < secondNumber || firstNumber % secondNumber != 0)) {
                    decided = firstNumber/secondNumber;
                    tmp = false;
                } else {
                    firstNumber = random.nextInt(first)+1;
                    secondNumber = random.nextInt(second)+1;
                    tmp = true;
                }
            } while(tmp);
        }
        return firstNumber + signString + secondNumber + " = ?";
    }

    private void waring(Context context, String text, int time) {
        Toast toast = Toast.makeText(this, text, time);
        toast.show();
    }
}