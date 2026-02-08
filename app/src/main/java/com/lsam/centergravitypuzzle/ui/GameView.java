package com.lsam.centergravitypuzzle.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;

import com.lsam.centergravitypuzzle.core.Board;

public class GameView extends View {

    public interface Listener {
        void onTapCell(int cx, int cy);
    }

    private Listener listener;
    private Board board;

    private final Paint blockPaint = new Paint();
    private final Paint gridPaint = new Paint();

    public GameView(Context context) {
        super(context);
        gridPaint.setARGB(255, 80, 80, 80);
    }

    public void bind(Board board, Listener l) {
        this.board = board;
        this.listener = l;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (board == null) return;

        int w = getWidth();
        int h = getHeight();
        int size = Math.min(w, h);
        int cell = size / Board.SIZE;

        canvas.drawARGB(255, 0, 0, 0);

        // blocks
        for (int y = 0; y < Board.SIZE; y++) {
            for (int x = 0; x < Board.SIZE; x++) {
                int v = board.get(x, y);
                if (v == Board.EMPTY) continue;

                int c = 60 + v * 40;
                if (c > 220) c = 220;
                blockPaint.setARGB(255, c, c, c);

                canvas.drawRect(
                    x * cell,
                    y * cell,
                    (x + 1) * cell,
                    (y + 1) * cell,
                    blockPaint
                );
            }
        }

        // grid
        for (int i = 0; i <= Board.SIZE; i++) {
            canvas.drawLine(i * cell, 0, i * cell, size, gridPaint);
            canvas.drawLine(0, i * cell, size, i * cell, gridPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        if (e.getAction() != MotionEvent.ACTION_DOWN) return true;

        int size = Math.min(getWidth(), getHeight());
        int cellSize = size / Board.SIZE;

        int cx = (int)(e.getX() / cellSize);
        int cy = (int)(e.getY() / cellSize);

        if (cx < 0 || cx >= Board.SIZE || cy < 0 || cy >= Board.SIZE) return true;

        if (listener != null) listener.onTapCell(cx, cy);
        return true;
    }
}