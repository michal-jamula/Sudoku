package org.example;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;

public class SudokuGUI {
    private SudokuLogic sudoku;
    private final JPanel sudokuPanel = new JPanel(new GridLayout(9, 9));
    private JFrame frame;

    public static void main(String[] args) {
        new SudokuGUI().go();
    }


    public void go() {
        frame = new JFrame("MJ Sudoku");
        sudoku = new SudokuLogic(50);
        JMenuBar menuBar = createMenuBar();

        createNewSudokuPanel(this.sudoku, sudokuPanel);

        JButton checkButton = new JButton("Check if solved");
        checkButton.addActionListener(l -> {
            if (checkSudokuBoardIfSolved()) {
                JOptionPane.showMessageDialog(frame, "Congratulations");
            }});

        frame.getContentPane().add(BorderLayout.NORTH, menuBar);
        frame.getContentPane().add(BorderLayout.CENTER, sudokuPanel);
        frame.getContentPane().add(BorderLayout.SOUTH, checkButton);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400,400);
        frame.setVisible(true);
    }

    //Creates a new sudoku panel, to be used in the frame content pane
    private void createNewSudokuPanel(SudokuLogic sudokuLogic, JPanel sudokuPanel) {
        sudokuPanel.removeAll();
        // Create a 9x9 grid of JTextFields
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                JTextField textField = createSingleTextBox(sudokuLogic, col, row);

                if (textField.getText().equals("0"))
                    textField.setText("0");

                sudokuPanel.add(textField);
            }
        }
    }

    //Creates and fills a single JTextField, to be used in the sudokuPanel
    private JTextField createSingleTextBox(SudokuLogic sudokuLogic, int col, int row) {
        Integer[][] board = sudokuLogic.getBoard();
        JTextField textField = new JTextField();
        textField.setHorizontalAlignment(JTextField.CENTER);
        textField.setFont(new Font("Serif",Font.BOLD,30));

        resetSingleJTextBoxBorders(textField, col, row);

        if (!board[row][col].toString().equals("0")) {
            textField.setBackground(Color.green);
            textField.setForeground(Color.gray);
            textField.setEditable(false);
            textField.setFocusable(false);
        }

        textField.setText(board[row][col].toString());
        return textField;
    }

    //used to create the borders in the sudokuPanel -- makes it more readable
    private void resetSingleJTextBoxBorders(JTextField textField, int col, int row) {
        textField.setBorder(new MatteBorder(0,0,0,0, Color.BLACK));
        if (col == 3 || col == 6) {
            textField.setBorder(new MatteBorder(0,2,0,0, Color.BLACK));
        }

        if (row == 3 || row == 6) {
            textField.setBorder(new MatteBorder(2,0,0,0, Color.BLACK));
        }

        if ((row == 3 && col == 3) || (row == 6 && col == 3) || (row == 3 && col == 6) || (row == 6 && col == 6)) {
            textField.setBorder(new MatteBorder(2,2,0,0, Color.BLACK));
        }
    }

    //checks the sudokuPanel GUI and validates the board
    private boolean checkSudokuBoardIfSolved() {
        boolean safe = true;
        int count;

        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                count = 9*row + col;

                JTextField square = (JTextField)sudokuPanel.getComponent(count);

                try {
                    Integer.parseInt(square.getText());
                } catch (NumberFormatException exception) {
                    JOptionPane.showMessageDialog(frame, "Unable to convert to number, check your board please");
                    return false;
                }

                //If there's an issue
                if (!checkNumberIsValidInAll(row, col, Integer.parseInt(square.getText())) ) {
                    safe = false;
                    this.resetSingleJTextBoxBorders(square, col, row);
                    if (!(Integer.parseInt(square.getText()) == 0))
                        square.setBorder(new CompoundBorder(new MatteBorder(4,4,4,4, Color.RED), square.getBorder()));
                } //else no issue, set the borders to normal so the red border doesn't stack
                else {
                    resetSingleJTextBoxBorders(square, col, row);
                }
            }
        }
        return safe;
    }

    //Checks whether number is valid in its row, column and square
    private boolean checkNumberIsValidInAll(int row, int col, int number) {
        int rowStart = row - row % 3;
        int colStart = col - col % 3;
        int topLeftIndex = rowStart * 9 + colStart;
        int count = 0;

        try {
            if (1 > number || number > 9)
                return false;

            //column check
            for (int i = 0; i < 9; i++) {
                JTextField field = (JTextField) sudokuPanel.getComponent(i * 9 + col);
                if (Integer.parseInt(field.getText()) == number)
                    count++;
            }

            //row check
            for (int i = 0; i < 9; i++) {
                JTextField field = (JTextField) sudokuPanel.getComponent(row * 9 + i);
                if (Integer.parseInt(field.getText()) == number)
                    count++;
            }

            //square check
            for (int i = topLeftIndex; i < topLeftIndex + 20; i += 9) {
                for (int j = i; j < i + 3; j++) {
                    JTextField field = (JTextField) sudokuPanel.getComponent(j);
                    if (String.valueOf(number).equals(field.getText())) {
                        count++;
                    }
                }
            }
            //If the square is not a number, it'll be highlighted along with the accommodating column and row
        } catch (NumberFormatException exception) {
            return false;
        }
        return count == 3;
    }


    //File menu option
    private JMenu createFileMenu() {
        JMenu fileMenu = new JMenu("File");
        JMenuItem newGameJMenuOption = new JMenuItem("New Game");
        newGameJMenuOption.addActionListener(actionEvent -> {
                        int percentage = getPercentageFromUser();
                        sudoku = new SudokuLogic(percentage);
                        createNewSudokuPanel(sudoku, sudokuPanel);
                        SwingUtilities.updateComponentTreeUI(sudokuPanel);
                });
        fileMenu.add(newGameJMenuOption);
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(action -> System.exit(1));
        fileMenu.add(exitItem);
        return fileMenu;
    }

    //Top navigation bar
    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(createFileMenu());
        return menuBar;
    }

    //JOptionPane used to get the game difficulty from user
    public static int getPercentageFromUser() {
        while (true) {
            try {
                String input = JOptionPane.showInputDialog(null, "Enter a number between 1-100. \nThe number determines how difficult your game will be. \nSuggested number range: 40(very hard) -  80(easy).");

                // If the user clicks Cancel or closes the dialog, return a default value
                if (input == null) {
                    return 55;
                }

                int difficultyNumber = Integer.parseInt(input);

                if (difficultyNumber >= 0 && difficultyNumber <= 100) {
                    return difficultyNumber;
                } else {
                    // Display an error message for an invalid input
                    JOptionPane.showMessageDialog(null, "Please enter a valid difficulty number (0-100).", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException e) {
                // Display an error message for non-numeric input
                JOptionPane.showMessageDialog(null, "Please enter a numeric value.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}