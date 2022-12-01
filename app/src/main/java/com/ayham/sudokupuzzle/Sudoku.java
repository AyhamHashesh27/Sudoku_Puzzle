package com.ayham.sudokupuzzle;

/* Reference: https://www.geeksforgeeks.org/program-sudoku-generator/ */

import java.lang.*;
import java.util.HashSet;
import java.util.Set;


public class Sudoku {
    int[] mat[];
    int N; // number of columns/rows.
    int SRN; // square root of N
    int K; // No. Of missing digits

    static Cell[][] gridCells;

    // Constructor
    Sudoku(int N, int K) {
        this.N = N;
        this.K = K;

        // Compute square root of N
        Double SRNd = Math.sqrt(N);
        SRN = SRNd.intValue();

        mat = new int[N][N];
    }

    // Sudoku Generator
    public void fillValues() {
        // Fill the diagonal of SRN x SRN matrices
        fillDiagonal();

        // Fill remaining blocks
        fillRemaining(0, SRN);

        // Remove Randomly K digits to make game
        removeKDigits();
    }

    // Fill the diagonal SRN number of SRN x SRN matrices
    void fillDiagonal() {

        for (int i = 0; i < N; i = i + SRN)

            // for diagonal box, start coordinates->i==j
            fillBox(i, i);
    }

    // Returns false if given 3 x 3 block contains num.
    boolean unUsedInBox(int rowStart, int colStart, int num) {
        for (int i = 0; i < SRN; i++)
            for (int j = 0; j < SRN; j++)
                if (mat[rowStart + i][colStart + j] == num)
                    return false;

        return true;
    }

    // Fill a 3 x 3 matrix.
    void fillBox(int row, int col) {
        int num;
        for (int i = 0; i < SRN; i++) {
            for (int j = 0; j < SRN; j++) {
                do {
                    num = randomGenerator(N);
                }
                while (!unUsedInBox(row, col, num));

                mat[row + i][col + j] = num;
            }
        }
    }

    // Random generator
    int randomGenerator(int num) {
        return (int) Math.floor((Math.random() * num + 1));
    }

    // Check if safe to put in cell
    boolean CheckIfSafe(int i, int j, int num) {
        return (unUsedInRow(i, num) &&
                unUsedInCol(j, num) &&
                unUsedInBox(i - i % SRN, j - j % SRN, num));
    }

    // check in the row for existence
    boolean unUsedInRow(int i, int num) {
        for (int j = 0; j < N; j++)
            if (mat[i][j] == num)
                return false;
        return true;
    }

    // check in the row for existence
    boolean unUsedInCol(int j, int num) {
        for (int i = 0; i < N; i++)
            if (mat[i][j] == num)
                return false;
        return true;
    }

    // A recursive function to fill remaining
    // matrix
    boolean fillRemaining(int i, int j) {
        //  System.out.println(i+" "+j);
        if (j >= N && i < N - 1) {
            i = i + 1;
            j = 0;
        }
        if (i >= N && j >= N)
            return true;

        if (i < SRN) {
            if (j < SRN)
                j = SRN;
        } else if (i < N - SRN) {
            if (j == (int) (i / SRN) * SRN)
                j = j + SRN;
        } else {
            if (j == N - SRN) {
                i = i + 1;
                j = 0;
                if (i >= N)
                    return true;
            }
        }

        for (int num = 1; num <= N; num++) {
            if (CheckIfSafe(i, j, num)) {
                mat[i][j] = num;
                if (fillRemaining(i, j + 1))
                    return true;

                mat[i][j] = 0;
            }
        }
        return false;
    }

    // Remove the K no. of digits to
    // complete game
    public void removeKDigits() {
        int count = K;
        while (count != 0) {
            int cellId = randomGenerator(N * N) - 1;

            // System.out.println(cellId);
            // extract coordinates i  and j
            int i = (cellId / N);
            int j = cellId % 9;
            if (j != 0)
                j = j - 1;

            // System.out.println(i+" "+j);
            if (mat[i][j] != 0) {
                count--;
                mat[i][j] = 0;
            }
        }
    }

    // Print sudoku
    public void printSudoku() {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++)
                System.out.print(mat[i][j] + " ");
            System.out.println();
        }
        System.out.println();
    }

    public char[][] getSudokuBoard() {
        char board[][] = new char[Constants.ROWS][Constants.COLS];
        for (int row = 0; row < Constants.ROWS; row++)
            for (int col = 0; col < Constants.COLS; col++)
                board[row][col] = numToCharMapping(this.mat[row][col]);
        return board;
    }

    /* To make it suit my implementation */
    private char numToCharMapping(int num) {
        if (num == 1) return '1';
        if (num == 2) return '2';
        if (num == 3) return '3';
        if (num == 4) return '4';
        if (num == 5) return '5';
        if (num == 6) return '6';
        if (num == 7) return '7';
        if (num == 8) return '8';
        if (num == 9) return '9';
        return ' ';
    }

    public static boolean isCompleted(Cell[][] grid) {
        for (int row = 0; row < Constants.ROWS; row++)
            for (int col = 0; col < Constants.COLS; col++)
                if (grid[row][col].getValue() == 0)
                    return false;
        return true;
    }

    public static boolean isValidSudoku(Cell[][] grid) {
        Set<String> seen = new HashSet<>();
        for (int row = 0; row < Constants.ROWS; ++row)
            for (int col = 0; col < Constants.COLS; ++col) {
                if (grid[row][col].getValue() == 0)
                    continue;
                final char c = (char) grid[row][col].getValue();
                if (!seen.add(c + "@row" + row) ||
                        !seen.add(c + "@col" + col) ||
                        !seen.add(c + "@box" + row / 3 + col / 3))
                    return false;
            }

        return true;
    }

}