package com.lsam.centergravitypuzzle.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;

import com.lsam.centergravitypuzzle.core.Board;

public class GameView extends View {

    // ===============================
    // Phase5 Fly-In (visual only)
    // ===============================
    private static class FlyInGhost {
        float sx, sy, ex, ey; // start -> end
        float t = 0f;          // 0..1
        int color;
        boolean active = false;
    }

    private final FlyInGhost fly = new FlyInGhost();

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

    // ===============================
    // Fly-In helpers (visual only)
    // ===============================
    private void startFlyIn(int tx, int ty) {
        if (board == null) return;

        int w = board.getWidth();
        int h = board.getHeight();

        // 盤外スタート座標（方向で決める）
        float cs = cellSize;
        float sx = 0, sy = 0;
        float ex = (tx + 0.5f) * cs;
        float ey = (ty + 0.5f) * cs;

        if (ty == 0) {               // 上から
            sx = ex; sy = -cs;
        } else if (ty == h - 1) {    // 下から
            sx = ex; sy = h * cs + cs;
        } else if (tx == 0) {        // 左から
            sx = -cs; sy = ey;
        } else if (tx == w - 1) {    // 右から
            sx = w * cs + cs; sy = ey;
        } else {
            // 中央タップ等は無視（既存仕様）
            return;
        }

        fly.sx = sx; fly.sy = sy;
        fly.ex = ex; fly.ey = ey;
        fly.t = 0f;
        fly.color = paint.getColor();
        fly.active = true;

        // 短いアニメ（~120ms）
        postInvalidateOnAnimation();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // --- 既存の盤面描画は super 側で完了している前提 ---

        // Fly-In ゴースト描画（盤面の上に重ねる）
        if (fly.active) {
            float t = fly.t;
            float x = fly.sx + (fly.ex - fly.sx) * t;
            float y = fly.sy + (fly.ey - fly.sy) * t;

            int a = (int)(255 * (1f - 0.2f * t));
            int oldA = paint.getAlpha();
            paint.setAlpha(a);

            canvas.drawCircle(x, y, cellSize * 0.45f, paint);

            paint.setAlpha(oldA);

            // 進行
            fly.t += 0.18f; // 6〜7フレーム想定
            if (fly.t >= 1f) {
                fly.active = false; // 以降は通常描画に合流
            } else {
                postInvalidateOnAnimation();
            }
        }
    }
