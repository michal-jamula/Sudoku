package org.example;

public class Main {


    public static void main(String[] args) {

        Sudoku sudoku = new Sudoku();


        System.out.println("The board: ");
        sudoku.printboard();

        boolean safe = sudoku.checkBoardIsSafe();

        System.out.println("Is the board safe? : " + safe);

    }




}