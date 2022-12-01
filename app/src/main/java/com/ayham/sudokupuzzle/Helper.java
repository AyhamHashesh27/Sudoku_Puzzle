package com.ayham.sudokupuzzle;

/* Helper Methods */
public class Helper {

    /* Max is excluded */
    public int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

}
