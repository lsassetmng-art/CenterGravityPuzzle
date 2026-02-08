package com.lsam.centergravitypuzzle.core;

import java.util.HashSet;
import java.util.Set;

public class MatchResolver {

    public static boolean resolve(Board board) {
        Set<Long> remove = new HashSet<>();

        // horizontal
        for (int y = 0; y < Board.SIZE; y++) {
            int run = 1;
            for (int x = 1; x <= Board.SIZE; x++) {
                int prev = board.get(x - 1, y);
                int curr = (x < Board.SIZE) ? board.get(x, y) : Board.EMPTY;
                if (prev != Board.EMPTY && prev == curr) {
                    run++;
                } else {
                    if (prev != Board.EMPTY && run >= 3) {
                        for (int i = 0; i < run; i++) {
                            remove.add(key(x - 1 - i, y));
                        }
                    }
                    run = 1;
                }
            }
        }

        // vertical
        for (int x = 0; x < Board.SIZE; x++) {
            int run = 1;
            for (int y = 1; y <= Board.SIZE; y++) {
                int prev = board.get(x, y - 1);
                int curr = (y < Board.SIZE) ? board.get(x, y) : Board.EMPTY;
                if (prev != Board.EMPTY && prev == curr) {
                    run++;
                } else {
                    if (prev != Board.EMPTY && run >= 3) {
                        for (int i = 0; i < run; i++) {
                            remove.add(key(x, y - 1 - i));
                        }
                    }
                    run = 1;
                }
            }
        }

        if (remove.isEmpty()) return false;

        for (long k : remove) {
            int x = (int)(k >>> 32);
            int y = (int)k;
            board.set(x, y, Board.EMPTY);
        }
        return true;
    }

    private static long key(int x, int y) {
        return (((long)x) << 32) | (y & 0xffffffffL);
    }
}