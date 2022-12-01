package com.ayham.sudokupuzzle;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.Button;


// Board Boxes = [ 1, 2, 3
//                 4, 5, 6
//                 7, 8, 9
//                  ]
// Odd color, even other color.

public class Cell {
    private Button button;
    private int value;

    public int getValue() {

        return this.value;
    }

    public void setValue(int value) {

        this.value = value;
    }

    public Button getButton() {

        return button;
    }

    public void setButton(Button button) {

        this.button = button;
    }

    // Color flag: 0 even box while 1 means odd box.
    public Cell(int initialValue, int colorFlag, Context THIS) {
        /* Pass the current state/context. */
        this.button = new Button(THIS);
        this.value = initialValue;

        if (colorFlag == 1) {
            button.setBackgroundTintList(THIS.getResources().getColorStateList(R.color.colorGray));
        }
        // Space
        if (value == 0) {
            this.button.setText("");
            this.button.setTextColor(Color.BLUE);
            // For better User Experience for the cells that need to be filled
            button.setBackgroundTintList(THIS.getResources().getColorStateList(R.color.colorWhite));
            this.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (getValue() == 9) {
                        setValue(0);
                    }
                    setValue(getValue() + 1);
                    button.setText(String.valueOf(getValue()));
                    if (Sudoku.isCompleted(Sudoku.gridCells)) {
                        if (Sudoku.isValidSudoku(Sudoku.gridCells))
                            MainActivity.setStatusMessage("Great Job! You solved it.");
                        else
                            MainActivity.setStatusMessage("There is a repeated digit!");
                    } else MainActivity.setStatusMessage("");
                }
            });
        } else {
            this.button.setText(this.value + "");
            this.button.setClickable(false);
        }
    }
}
