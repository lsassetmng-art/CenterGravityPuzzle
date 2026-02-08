package com.lsam.centergravitypuzzle.core;

import java.util.Random;

public class TurnEngine {

    public enum State { PLAYING, GAME_OVER }

    private final Board board = new Board();
    private final Random random;
    private int turn = 0;
    private State state = State.PLAYING;

    public TurnEngine(long seed) {
        this.random = new Random(seed);
        reset();
    }

    public void reset() {
        turn = 0;
        state = State.PLAYING;
        board.clear();
        board.fillRandom(random, 3);
        Gravity.apply(board);
    }

    public Board getBoard() {
        return board;
    }

    public int getTurn() {
        return turn;
    }

    public State getState() {
        return state;
    }

    public int getBlockTypes() {
        return (turn < 10) ? 3 : 5;
    }

    // =====================================================
    // 正式仕様：受け取り型（外周から1ブロック飛来）
    // =====================================================
    public void applyTurn(int tx, int ty) {
        if (state == State.GAME_OVER) return;

        int x = -1, y = -1;

        // タップ位置から方向を決定
        if (ty < 3) {                 // 上
            x = clamp(tx);
            y = 0;
        } else if (ty > 5) {          // 下
            x = clamp(tx);
            y = Board.SIZE - 1;
        } else if (tx < 3) {          // 左
            x = 0;
            y = clamp(ty);
        } else if (tx > 5) {          // 右
            x = Board.SIZE - 1;
            y = clamp(ty);
        } else {
            // 中央エリアは操作対象外
            return;
        }

        // GameOver 正本条件：投入マスが埋まっている
        if (!board.isEmpty(x, y)) {
            state = State.GAME_OVER;
            return;
        }

        // 投入
        board.set(x, y, random.nextInt(getBlockTypes()));

        // 中心重力
        Gravity.apply(board);

        // 消去 → 再重力（連鎖）
        while (MatchResolver.resolve(board)) {
            Gravity.apply(board);
        }

        turn++;
    }

    private int clamp(int v) {
        if (v < 0) return 0;
        if (v >= Board.SIZE) return Board.SIZE - 1;
        return v;
    }
}