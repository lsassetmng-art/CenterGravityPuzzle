package com.lsam.centergravitypuzzle.core;

import java.util.Random;

public class Board {

    public static final int SIZE = 9;
    public static final int EMPTY = -1;

    private final int[][] cells = new int[SIZE][SIZE];

    public Board() {
        clear();
    }

    public void clear() {
        for (int y = 0; y < SIZE; y++) {
            for (int x = 0; x < SIZE; x++) {
                cells[y][x] = EMPTY;
            }
        }
    }

    public void fillRandom(Random random, int types) {
        for (int y = 0; y < SIZE; y++) {
            for (int x = 0; x < SIZE; x++) {
                cells[y][x] = random.nextInt(types);
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

    public boolean inBounds(int x, int y) {
        return x >= 0 && x < SIZE && y >= 0 && y < SIZE;
    }
}