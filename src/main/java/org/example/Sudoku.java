package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Sudoku {


    private Integer[][] board = new Integer[][] {
            {0,0,0, 0,0,0, 0,0,0},
            {0,0,0, 0,0,0, 0,0,0},
            {0,0,0, 0,0,0, 0,0,0},

            {0,0,0, 0,0,0, 0,0,0},
            {0,0,0, 0,0,0, 0,0,0},
            {0,0,0, 0,0,0, 0,0,0},

            {0,0,0, 0,0,0, 0,0,0},
            {0,0,0, 0,0,0, 0,0,0},
            {0,0,0, 0,0,0, 0,0,0},
    };



    public Sudoku() {
        this.fillSquareWithRandom(0,0);
        this.fillSquareWithRandom(3,3);
        this.fillSquareWithRandom(6,6);
        if (fillNumbers()) {
            System.out.println("Sudoku is valid");
        } else {
            System.out.println("Sudoku is invalid");
        }
    }

    //Fills a 3x3 square with random numbers, makes sure they dont repeat within the squares but doesnt check the columns or rows
    private void fillSquareWithRandom(int x, int y) {
        Random rand = new Random();
        List<Integer> availableNumbers = new ArrayList<>(List.of(1,2,3,4,5,6,7,8,9));



        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                Integer randNumber = availableNumbers.get(rand.nextInt(availableNumbers.size()));
                board[x+i][y+j] = randNumber;
                availableNumbers.remove(randNumber);
            }
        }

    }

    //Recursive method to fill the rest of the board
    private boolean fillNumbers() {
        //Loop over both arrays
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {

                //Generate number if field is 0
                if (board[row][col] == 0) {
                    for (int num = 1; num <= 9; num++) {
                        if (isNumberValidInAll(row, col, num)){
                            board[row][col] = num;

                            //true if number is placed successfully, false if otherwise -- this enables backtracking
                            if (fillNumbers()) {
                                return true;
                            } else {
                                board[row][col] = 0;
                            }
                        }
                    }
                    //False if board is unsolvable
                    return false;
                }
            }
        }
        return true;
    }

    //Makes sure the number doesnt repeat itself in the square, row and column
    private boolean isNumberValidInAll(int x,int y,int num) {
        return (numberOccurringInRow(x, num) == 0) &&
                (numberOccurringInCol(y, num) == 0) &&
                (numberOccurringInSquare(x,y,num) == 0);
    }

    //returns how many times the number occurs in the row
    private int numberOccurringInRow(int x, int num) {
        int count = 0;
        for (int j = 0; j<9; j++)
            if (board[x][j] == num)
                count++;
        return count;
    }

    //returns how many times the number occurs in the column
    private int numberOccurringInCol(int y, int num) {
        int count = 0;
        for (int i = 0; i<9; i++)
            if (board[i][y] == num)
                count ++;
        return count;
    }

    //returns how many times the number occurs in the 3x3 square
    private int numberOccurringInSquare(int x, int y, int num) {
        int rowStart = x - x % 3;
        int colStart = y - y % 3;
        int count = 0;

        for(int i = rowStart; i < rowStart + 3; i++) {
            for(int j = colStart; j < colStart + 3; j++) {
                if (board[i][j] == num) {
                    count ++;
                }
            }
        }
        return count;
    }

    public void printboard() {
        for (int i = 0; i < 9; i++) {
            if ( i % 3 == 0 && i != 0) {
                System.out.println("- - - - - - - - - - -");
            }
            for (int j = 0; j < 9; j++) {
                if ( j % 3 == 0 && j != 0) {
                    System.out.print("| ");
                }
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
    }

    //Makes sure each number doesn't repeat itself in row, column and square
    public boolean checkBoardIsSafe() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                boolean safe = (numberOccurringInSquare(i,j,board[i][j]) == 1) &&
                        (numberOccurringInCol(j, board[i][j]) == 1) &&
                        (numberOccurringInRow(i, board[i][j]) == 1);

                if (safe == false) {
                    System.out.printf("Number is unsafe at index: %s,%s\n",i, j);
                }
                return safe;
            }
        }
        return true;
    }

}