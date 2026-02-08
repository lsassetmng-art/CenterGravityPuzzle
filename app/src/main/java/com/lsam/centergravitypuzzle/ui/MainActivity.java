package com.lsam.centergravitypuzzle.ui;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lsam.centergravitypuzzle.core.TurnEngine;

public class MainActivity extends Activity {

    private TurnEngine engine;
    private TextView status;
    private GameView view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        engine = new TurnEngine(0);

        status = new TextView(this);
        Button reset = new Button(this);
        reset.setText("RESET");

        view = new GameView(this);
        view.bind(engine.getBoard(), (x, y) -> {
            engine.applyTurn(x, y);
            view.invalidate();
            refresh();
        });

        reset.setOnClickListener(v -> {
            engine.reset();
            view.invalidate();
            refresh();
        });

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.addView(status);
        root.addView(reset);
        root.addView(view,
            new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                0, 1f
            )
        );

        setContentView(root);
        refresh();
    }

    private void refresh() {
        String mode = (engine.getTurn() < 10) ? "Tutorial" : "Normal";
        String s = "Turn: " + engine.getTurn()
                 + " / Mode: " + mode
                 + " / State: " + engine.getState();
        status.setText(s);
    }
}