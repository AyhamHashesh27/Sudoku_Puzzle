package com.ayham.sudokupuzzle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;

import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity {
    /* Layout Declaration */
    TableLayout tableLayout;
    LinearLayout mainLinearLayout, timerLinearLayout, buttonsLinearLayout;
    ScrollView scrollView;

    /* Widgets Declaration */
    // Buttons
    Button resetTimeButton, startStopTimeButton, resetGameButton, anotherChallengeButton;
    // Text Views
    static TextView resultMessageTextView;
    TextView timerTextView, timeTextView, gameSettingsTextView;
    Spinner levelsSpinner;

    /* Timer Declaration*/
    Timer timer;
    TimerTask timerTask;
    Double time = 0.0;
    boolean isTimerStarted = false;

    /* Sudoku Board Declaration*/
    static char board[][];

    /* Objects Declaration*/
    Sudoku sudoku;
    Helper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /* Generate Sudoku Board*/
        sudoku = new Sudoku(9, 2);
        sudoku.fillValues();
        board = sudoku.getSudokuBoard();

        Sudoku.gridCells = new Cell[Constants.ROWS][Constants.COLS];
        tableLayout = new TableLayout(this);
        /* To make the table layout fit to the screen */

        tableLayout.setShrinkAllColumns(true);
        tableLayout.setStretchAllColumns(true);
        settleTheBoard(board);

        /* Layouts Design*/
        mainLinearLayout = new LinearLayout(this);
        timerLinearLayout = new LinearLayout(this);
        buttonsLinearLayout = new LinearLayout(this);
        scrollView = new ScrollView(this);
        // Layouts settings
        mainLinearLayout.setOrientation(LinearLayout.VERTICAL);
        timerLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
        buttonsLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
        timerLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        mainLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        buttonsLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        /* Timer Layout Organization */
        timer = new Timer();
        resetTimeButton = new Button(this);
        resetTimeButton.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
        resetTimeButton.setText(getString(R.string.reset));
        resetTimeButton.setTextColor(Color.WHITE);
        resetTimeButton.setBackgroundTintList(this.getResources().getColorStateList(R.color.colorOrange));

        startStopTimeButton = new Button(this);
        startStopTimeButton.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
        startStopTimeButton.setText(getString(R.string.start));
        startStopTimeButton.setBackgroundTintList(this.getResources().getColorStateList(R.color.colorGreen));
        startStopTimeButton.setTextColor(Color.WHITE);
//        startStopTimeButton.setTypeface(null, Typeface.BOLD);

        timerTextView = new TextView(this);
        timerTextView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
        timerTextView.setText(getString(R.string.timer));
        timerTextView.setTextSize(24);
        timerTextView.setGravity(Gravity.CENTER);

        timeTextView = new TextView(this);
        timeTextView.setText(formatTime(0, 0, 0));
        timeTextView.setTextSize(32);
        timeTextView.setGravity(Gravity.CENTER);

        timerLinearLayout.addView(resetTimeButton);
        timerLinearLayout.addView(timerTextView);
        timerLinearLayout.addView(startStopTimeButton);

        /* Game settings */
        gameSettingsTextView = new TextView(this);
        gameSettingsTextView.setText(getString(R.string.game_board_settings));
        gameSettingsTextView.setTextAppearance(R.style.TextAppearance_AppCompat_Widget_ActionMode_Subtitle);

        resetGameButton = new Button(this);
        resetGameButton.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
        resetGameButton.setText(getString(R.string.reset));

        anotherChallengeButton = new Button(this);
        anotherChallengeButton.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
        anotherChallengeButton.setText(getString(R.string.change));

        String[] levels = {getString(R.string.easy), getString(R.string.medium), getString(R.string.hard)};
        levelsSpinner = new Spinner(this);
        levelsSpinner.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
        ArrayAdapter<String> objGenderArr = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, levels);
        levelsSpinner.setAdapter(objGenderArr);
        levelsSpinner.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);

        buttonsLinearLayout.addView(resetGameButton);
        buttonsLinearLayout.addView(anotherChallengeButton);
        buttonsLinearLayout.addView(levelsSpinner);

        // Main Design Organization
        resultMessageTextView = new TextView(this);
        resultMessageTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        resultMessageTextView.setTextAppearance(R.style.TextAppearance_AppCompat_SearchResult_Title);

        mainLinearLayout.addView(timerLinearLayout);
        mainLinearLayout.addView(timeTextView);
        mainLinearLayout.addView(tableLayout);
        mainLinearLayout.addView(gameSettingsTextView);
        mainLinearLayout.addView(buttonsLinearLayout);
        mainLinearLayout.addView(resultMessageTextView);

        scrollView.addView(mainLinearLayout);
        setContentView(scrollView);


        startStopTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startStopTapped();
            }
        });

        resetTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetTapped(true);
            }
        });

        anotherChallengeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                helper = new Helper();
                int missingCellsCount;
                if (levelsSpinner.getSelectedItem().toString() == getString(R.string.easy))
                    missingCellsCount = helper.getRandomNumber(1, 5);
                else if (levelsSpinner.getSelectedItem().toString() == getString(R.string.medium))
                    missingCellsCount = helper.getRandomNumber(5, 10);
                else
                    missingCellsCount = helper.getRandomNumber(10, 21);
                sudoku = new Sudoku(9, missingCellsCount);
                sudoku.fillValues();
                board = sudoku.getSudokuBoard();
                tableLayout.removeAllViews();
                setStatusMessage("");
                settleTheBoard(board);
                resetTapped(false);
            }
        });
        resetGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tableLayout.removeAllViews();
                setStatusMessage("");
                settleTheBoard(board);
                resetTapped(false);
            }
        });
    }

    private void settleTheBoard(char[][] board) {
        for (int row = 0; row < Constants.ROWS; row++) {
            TableRow tableRow = new TableRow(this);
            for (int col = 0; col < Constants.COLS; col++) {
                Sudoku.gridCells[row][col] = new Cell(board[row][col] == ' ' ? 0 : board[row][col] - '0', (row / Constants.BOX_DIVISOR + col / Constants.BOX_DIVISOR) % 2, this);
                tableRow.addView(Sudoku.gridCells[row][col].getButton());
            }
            tableLayout.addView(tableRow);
        }

    }

    public static void setStatusMessage(String msg) {
        resultMessageTextView.setText(msg);
    }

    public void resetTapped(boolean confirmationDialog) {
        if (confirmationDialog) {
            AlertDialog.Builder resetAlert = new AlertDialog.Builder(this);
            resetAlert.setTitle(getString(R.string.reset_timer));
            resetAlert.setMessage(getString(R.string.reset_timer_confirmation));
            resetAlert.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    resetTimer();
                }
            });

            resetAlert.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    // Do nothing.
                }
            });
            resetAlert.show();
        } else {
            resetTimer();
        }

    }

    private void resetTimer() {
        if (timerTask != null) {
            startStopTimeButton.setText(getString(R.string.start));
            startStopTimeButton.setBackgroundTintList(getResources().getColorStateList(R.color.colorGreen));
            timerTask.cancel();
            time = 0.0;
            isTimerStarted = false;
            timeTextView.setText(formatTime(0, 0, 0));

        }
    }

    public void startStopTapped() {
        if (isTimerStarted) {
            isTimerStarted = false;
            startStopTimeButton.setText(getString(R.string.start));
            startStopTimeButton.setBackgroundTintList(this.getResources().getColorStateList(R.color.colorGreen));
            timerTask.cancel();

        } else {
            isTimerStarted = true;
            startStopTimeButton.setText(getString(R.string.stop));
            startStopTimeButton.setBackgroundTintList(this.getResources().getColorStateList(R.color.colorRed));
            startTimer();
        }
    }

    private void startTimer() {
        timerTask = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        time++;
                        timeTextView.setText(getTimerText());
                    }
                });
            }
        };
        timer.scheduleAtFixedRate(timerTask, 0, 1000);

    }

    private String getTimerText() {
        int rounded = (int) Math.round(time);
        int seconds, minutes, hours, totalDaySeconds = 24 * 60 * 60;
        seconds = ((rounded % totalDaySeconds) % 3600) % 60;
        minutes = ((rounded % totalDaySeconds) % 3600) / 60;
        hours = ((rounded % totalDaySeconds) / 3600);
        return formatTime(seconds, minutes, hours);
    }

    private String formatTime(int seconds, int minutes, int hours) {
        return String.format("%02d", hours) + " : " + String.format("%02d", minutes) + " : " + String.format("%02d", seconds);
    }
}