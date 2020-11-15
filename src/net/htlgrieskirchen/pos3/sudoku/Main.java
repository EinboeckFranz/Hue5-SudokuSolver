package net.htlgrieskirchen.pos3.sudoku;


import java.io.File;

public class Main {
    public static void main(String[] args) {
        SudokuSolver ss = new SudokuSolver();
        int[][] input = ss.readSudoku(new File("1_sudoku_level1.csv"));
        System.out.println(">--- ORIGINAL ---");
        SudokuSolver.printSudoku(input);
        int[][] output = ss.solveSudoku(input);
        System.out.println(">--- SOLUTION ---");
        SudokuSolver.printSudoku(output);
        System.out.println(">----------------");
        System.out.println("SOLVED    = " + ss.checkSudoku(output));
        System.out.println(">----------------");
        System.out.println("Sequential: " + ss.benchmarkNormal() + "ms\n" +
                           "Parallel: " + ss.benchmarkParallel() + "ms");
    }
}
