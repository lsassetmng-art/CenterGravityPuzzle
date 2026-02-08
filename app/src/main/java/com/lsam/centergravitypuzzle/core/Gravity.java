package com.lsam.centergravitypuzzle.core;

/**
 * 中心重力処理
 * - 上エリア(y<4)    → 下
 * - 下エリア(y>4)    → 上
 * - 左エリア(x<4)    → 右
 * - 右エリア(x>4)    → 左
 * - 中心線(x=4 or y=4)は停止
 */
public class Gravity {

    public static void apply(Board board) {
        boolean moved;
        do {
            moved = false;

            for (int y = 0; y < Board.SIZE; y++) {
                for (int x = 0; x < Board.SIZE; x++) {
                    int v = board.get(x, y);
                    if (v == Board.EMPTY) continue;

                    int dx = 0;
                    int dy = 0;

                    if (y < 4) dy = 1;
                    else if (y > 4) dy = -1;
                    else if (x < 4) dx = 1;
                    else if (x > 4) dx = -1;
                    else continue; // 中心セル

                    int nx = x + dx;
                    int ny = y + dy;

                    if (board.isEmpty(nx, ny)) {
                        board.set(nx, ny, v);
                        board.set(x, y, Board.EMPTY);
                        moved = true;
                    }
                }
            }

        } while (moved);
    }
}
