package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

//Support class to help with the creation and validation of a board
public class SudokuLogic {
    private Integer[][] board = new Integer[][] {};
    private final Integer[][] solvedBoard = new Integer[][] {
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

    public Integer[][] getBoard() {
        return board;
    }

    public SudokuLogic(int percentage) {
        this.fillSquareWithRandom(0,0);
        this.fillSquareWithRandom(3,3);
        this.fillSquareWithRandom(6,6);
        if (fillNumbers()) {
            System.out.println("Sudoku is valid");
            System.out.println("Printing answer: ");
            this.printBoardToConsole(solvedBoard);
            randomlyHideNumbers(percentage);

        } else {
            System.out.println("Sudoku is invalid");
            System.err.println("Sudoku creation failed. Exit program");
            System.exit(0);
        }
    }

    //Fills a 3x3 square with random numbers, makes sure they don't repeat within the squares but doesn't check the columns or rows
    private void fillSquareWithRandom(int row, int col) {
        Random rand = new Random();
        List<Integer> availableNumbers = new ArrayList<>(List.of(1,2,3,4,5,6,7,8,9));

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                Integer randNumber = availableNumbers.get(rand.nextInt(availableNumbers.size()));
                solvedBoard[row+i][col+j] = randNumber;
                availableNumbers.remove(randNumber);
            }
        }

    }

    //Recursive method to fill the board
    private boolean fillNumbers() {
        //Loop over both arrays
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                //Generate number if field is 0
                if (solvedBoard[row][col] == 0) {
                    for (int num = 1; num <= 9; num++) {
                        if (isNumberValidInAll(row, col, num)){
                            solvedBoard[row][col] = num;

                            //true if number is placed successfully, false if otherwise -- this enables backtracking
                            if (fillNumbers()) {
                                return true;
                            } else {
                                solvedBoard[row][col] = 0;
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


    private boolean isNumberValidInAll(int row,int col,int num) {
        return (numberOccurringInRow(row, num) == 0) &&
                (numberOccurringInCol(col, num) == 0) &&
                (numberOccurringInSquare(row,col,num) == 0);
    }

    //returns how many times the number occurs in a given row
    private int numberOccurringInRow(int row, int num) {
        int count = 0;
        for (int j = 0; j<9; j++)
            if (solvedBoard[row][j] == num)
                count++;
        return count;
    }

    //returns how many times the number occurs in a given column
    private int numberOccurringInCol(int col, int num) {
        int count = 0;
        for (int i = 0; i<9; i++)
            if (solvedBoard[i][col] == num)
                count ++;
        return count;
    }

    //returns how many times the number occurs in a given 3x3 square
    private int numberOccurringInSquare(int row, int col, int num) {
        int rowStart = row - row % 3;
        int colStart = col - col % 3;
        int count = 0;

        for(int i = rowStart; i < rowStart + 3; i++) {
            for(int j = colStart; j < colStart + 3; j++) {
                if (solvedBoard[i][j] == num) {
                    count ++;
                }
            }
        }
        return count;
    }

    public void printBoardToConsole(Integer[][] board) {
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

    private void randomlyHideNumbers(int percentage) {
        Random rand = new Random();

        // Create a copy of the solved board
        this.board = new Integer[9][9];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (rand.nextDouble(0, 100) < percentage){
                    this.board[i][j] = solvedBoard[i][j];
                } else {
                    this.board[i][j] = 0;
                }
            }
        }
    }
}