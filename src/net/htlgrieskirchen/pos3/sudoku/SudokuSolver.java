package net.htlgrieskirchen.pos3.sudoku;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/* Please enter here an answer to task four between the tags:
 * <answerTask4>
        NOT DONE
 * </answerTask4>
 */
public class SudokuSolver implements ISodukoSolver {
    @Override
    public final int[][] readSudoku(File file) {
        try {
            return Files.lines(file.toPath()).map(s -> s.split(";"))
                    .map(value -> new int[]{
                            Integer.parseInt(value[0]),
                            Integer.parseInt(value[1]),
                            Integer.parseInt(value[2]),
                            Integer.parseInt(value[3]),
                            Integer.parseInt(value[4]),
                            Integer.parseInt(value[5]),
                            Integer.parseInt(value[6]),
                            Integer.parseInt(value[7]),
                            Integer.parseInt(value[8])
                    }).toArray(int[][]::new);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    //A String Trick from LeetCode (seen before - wanted to try it)
    @Override
    public boolean checkSudoku(int[][] rawSudoku) {
        return checkRowCol(rawSudoku) && checkSubsection(rawSudoku);
    }

    @Override
    public int[][] solveSudoku(int[][] rawSudoku) {
        solveWithBacktracking(rawSudoku, 0 , 0);
        return rawSudoku;
    }

    @Override
    public int[][] solveSudokuParallel(int[][] rawSudoku) {
        // implement this method
        return rawSudoku;
    }

    public long benchmarkNormal() {
        long allTimes = 0;
        for (int i = 0; i < 10; i++) {
            long startTime = System.nanoTime();
            checkSudoku(solveSudoku(Objects.requireNonNull(readSudoku(new File("1_sudoku_level1.csv")))));
            allTimes += (System.nanoTime() - startTime);
        }
        return TimeUnit.MILLISECONDS.convert((allTimes / 10), TimeUnit.NANOSECONDS);
    }

    public long benchmarkParallel() {
        long allTimes = 0;
        for (int i = 0; i < 10; i++) {
            long startTime = System.nanoTime();
            checkSudoku(solveSudokuParallel(readSudoku(new File("1_sudoku_level1.csv"))));
            allTimes += (System.nanoTime() - startTime);
        }
        return TimeUnit.MILLISECONDS.convert((allTimes / 10), TimeUnit.NANOSECONDS);
    }

    // add helper methods here if necessary
    private boolean checkRowCol(int[][] rawSudoku) {
        Set<Integer> rowCheck = new HashSet<>();
        Set<Integer> colCheck = new HashSet<>();

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if(!rowCheck.add(rawSudoku[i][j]))
                    return false;

                if(!colCheck.add(rawSudoku[j][i]))
                    return false;
            }
            rowCheck.clear();
            colCheck.clear();
        }
        return true;
    }

    private boolean checkSubsection(int[][] rawSudoku) {
        return false;
    }

    private boolean solveWithBacktracking(int[][] rawSudoku, int row, int column) {
        if (row == 9 - 1 && column == 9)
            return true;

        if (column == 9) {
            row++;
            column = 0;
        }

        if (rawSudoku[row][column] != 0)
            return solveWithBacktracking(rawSudoku, row, column + 1);

        for (int i = 1; i < 10; i++) {
            if (numberCanBeAdded(rawSudoku, row, column, i)) {
                rawSudoku[row][column] = i;
                if(solveWithBacktracking(rawSudoku, row, column+1))
                    return true;
            }
            rawSudoku[row][column] = 0;
        }
        return false;
    }

    private boolean numberCanBeAdded(int[][] sudoku, int row, int collum, int number) {
        for (int i = 0; i < 9; i++)
            if ((sudoku[row][i] == number) || (sudoku[i][collum] == number))
                return false;

        int startRow = row - (row % 3),
            startCol = collum - (collum % 3);
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                if (sudoku[i + startRow][j + startCol] == number)
                    return false;

        return true;
    }

    static void printSudoku(int[][] rawSudoku) {
        SudokuSolver instance = new SudokuSolver();
        instance.printSudokuHeaderFooter();
        Arrays.stream(rawSudoku).forEach(sudokuRow -> {
            Arrays.stream(sudokuRow).forEach(number -> System.out.print("|" + number));
            System.out.print("|\n");
        });
        instance.printSudokuHeaderFooter();
    }

    private void printSudokuHeaderFooter() {
        System.out.print("+");
        for (int i = 0; i < 9; i++)
            System.out.print("-+");
        System.out.print("\n");
    }
}