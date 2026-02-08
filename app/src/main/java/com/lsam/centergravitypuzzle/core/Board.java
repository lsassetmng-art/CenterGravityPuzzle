package com.lsam.centergravitypuzzle.core;

import java.util.Random;

/**
 * 9x9 盤面の正本データ構造
 */
public class Board {

    public static final int SIZE = 9;
    public static final int EMPTY = -1;

    private final int[][] cells = new int[SIZE][SIZE];

    public Board(Random random, int blockTypes) {
        for (int y = 0; y < SIZE; y++) {
            for (int x = 0; x < SIZE; x++) {
                cells[y][x] = random.nextInt(blockTypes);
            }
        }
    }

    public int get(int x, int y) {
        return cells[y][x];
    }

    public void set(int x, int y, int value) {
        cells[y][x] = value;
    }

    public boolean isEmpty(int x, int y) {
        return cells[y][x] == EMPTY;
    }
}
