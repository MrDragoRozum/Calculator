package com.example.mycalculator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
    int firstNumber;
    int secondNumber;

    private ArrayList<String> mathSigns;
    private Random random;

    private TextView textViewExample;
    private TextView textViewCorrect;
    private TextView textViewWrong;
    private EditText editTextAnswer;
    private Button buttonAnswer;
    private Button buttonCreate;
    private Button buttonChooseMathSings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

        buttonChooseMathSings.setOnClickListener(l -> {
            registerForContextMenu(buttonChooseMathSings);
            openContextMenu(buttonChooseMathSings);
        });

        buttonAnswer.setOnClickListener(l -> {
            String answerString = editTextAnswer.getText().toString();
            Integer answerUser = checker(answerString);
            if (answerUser != -1 && repeat != -1) {
                checker(answerUser, textViewWrong, textViewCorrect);
            }
        });

        buttonCreate.setOnClickListener(l -> checker(repeat));
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

    private String example(int first, int second, int sign) {
        firstNumber = random.nextInt(first) + 1;
        secondNumber = random.nextInt(second) + 1;
        String signString = mathSigns.get(sign);

        multiplication(sign, firstNumber, secondNumber);
        plus(sign, firstNumber, secondNumber);
        minus(sign, firstNumber, secondNumber);
        division(sign, firstNumber, secondNumber, first, second);

        return firstNumber + signString + secondNumber + " = ?";
    }

    private void multiplication(int sign, int firstNumber, int secondNumber) {
        if (sign == MENU_MULTIPLICATION) {
            decided = firstNumber * secondNumber;
        }
    }

    private void plus(int sign, int firstNumber, int secondNumber) {
        if (sign == MENU_PLUS) {
            decided = firstNumber + secondNumber;
        }
    }

    private void minus(int sign, int firstNumber, int secondNumber) {
        if (sign == MENU_MINUS) {
            if (firstNumber < secondNumber) {
                int order = this.firstNumber;
                this.firstNumber = this.secondNumber;
                this.secondNumber = order;
            }
            decided = this.firstNumber - this.secondNumber;
        }
    }

    private void division(int sign, int firstNumber, int secondNumber, int first, int second) {
        if (sign == MENU_DIVISION) {
            boolean tmp;
            do {
                if (!(firstNumber < secondNumber || firstNumber % secondNumber != 0)) {
                    decided = this.firstNumber / this.secondNumber;
                    tmp = false;
                } else {
                    firstNumber = random.nextInt(first) + 1;
                    secondNumber = random.nextInt(second) + 1;
                    this.firstNumber = firstNumber;
                    this.secondNumber = secondNumber;
                    tmp = true;
                }
            } while (tmp);
        }
    }

    private void checker(int repeat) {
        if (!(repeat == -1)) {
            textViewExample.setText(example(99, 99, repeat));
        } else {
            Toast.makeText(this,
                    R.string.warningForUser,
                    Toast.LENGTH_SHORT).show();
        }

    }

    private Integer checker(String number) {
        if (!number.equals("")) {
            return Integer.valueOf(number);
        } else {
            Toast.makeText(this,
                    R.string.warningForUser,
                    Toast.LENGTH_SHORT).show();
            return -1;
        }
    }

    private void checker(Integer answerUser, TextView wrong, TextView correct) {
        if (answerUser.equals(decided)) {
            correct.setVisibility(View.VISIBLE);
            wrong.setVisibility(View.GONE);
        } else {
            correct.setVisibility(View.GONE);
            wrong.setVisibility(View.VISIBLE);
        }
    }

    private void initView() {
        textViewCorrect = findViewById(R.id.textViewCorrect);
        textViewExample = findViewById(R.id.textViewExample);
        textViewWrong = findViewById(R.id.textViewWrong);

        editTextAnswer = findViewById(R.id.editTextAnswer);
        editTextAnswer = findViewById(R.id.editTextAnswer);

        buttonAnswer = findViewById(R.id.buttonAnswer);
        buttonCreate = findViewById(R.id.buttonCreate);
        buttonChooseMathSings = findViewById(R.id.buttonChooseMathSings);

        random = new Random();
        mathSigns = new ArrayList<>(4);
        Collections.addAll(mathSigns, "+", "-", ":", "×");
    }
}