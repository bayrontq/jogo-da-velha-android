package com.bayron.jogodavelha.widgets;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.support.v4.content.ContextCompat;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import com.bayron.jogodavelha.R;
import com.bayron.jogodavelha.interfaces.OnGameAction;

public class BlackboardView extends View {

    private final int[][] tabuleiro = {
            {0,0,0},
            {0,0,0},
            {0,0,0}
    };
    private boolean vezdox = true;
    private OnGameAction onGameAction;

    public BlackboardView(Context context, OnGameAction onGameAction) {
        super(context);
        setBackgroundResource(R.drawable.blackboard);
        this.onGameAction = onGameAction;
    }

    @Override
    public boolean onTouchEvent(MotionEvent me) {
        if (me.getAction() == MotionEvent.ACTION_UP) {
            float x = me.getX();
            float y = me.getY();

            // Tamanho Da tela
            WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            int width = size.x;
            int height = size.y;

            // Acha o lugar
            for (int n = 0; n < 3; n++) {
                for (int m = 0; m < 3; m++) {
                    if (x > m * (width / 3)
                            && x < (m + 1) * (width / 3)
                            && y > n * (height / 3)
                            && y < (n + 1) * (height / 3)) {
                        if (tabuleiro[m][n] == 0) {
                            // Operador ternário: if (vezdox)tabuleiro[m][n] = 1; else tabuleiro[m][n] = 2;
                            tabuleiro[m][n] = (vezdox?1:2);
                            vezdox =! vezdox;
                        }
                    }
                }
            }
            invalidate();
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas c) {
        super.onDraw(c);

        // Tamanho Da tela
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;

        // Define a pintura
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setStyle(Paint.Style.STROKE);
        p.setColor(ContextCompat.getColor(getContext(), android.R.color.white));

        //Linhas Verticais
        c.drawLine(width / 3, 0, width / 3,height,p);
        c.drawLine((width / 3) * 2, 0, (width / 3) * 2, height, p);
        //Linhas Horizontais
        c.drawLine(0, height / 3, width, height / 3, p);
        c.drawLine(0, (height / 3) * 2, width, (height / 3) * 2, p);

        p.setColor(ContextCompat.getColor(getContext(), R.color._FFFF00));
        for (int n = 0; n < 3; n++) {
            for (int m = 0; m < 3; m++) {
                if (tabuleiro[m][n] == 2) {
                    c.drawCircle(((width / 3) / 2) + (m * (width / 3)), ((height / 3) / 2) + (n * (height / 3)), ((width / 3) / 2), p);
                }
            }
        }

        p.setColor(ContextCompat.getColor(getContext(), R.color._8A2BE2));
        for (int n = 0; n < 3; n++) {
            for (int m = 0; m < 3; m++) {
                if (tabuleiro[m][n] == 1) {
                    c.drawLine(
                            m * (width / 3),
                            n * (height / 3),
                            (m + 1) * (width / 3),
                            (n + 1) * (height / 3),
                            p);
                    c.drawLine(
                            m * (width/3),
                            (n + 1) * (height / 3),
                            (m + 1) * (width / 3),
                            n * (height / 3),
                            p);
                }
            }
        }

        //Seta cor e espessura da linha
        p.setColor(ContextCompat.getColor(getContext(), R.color._B22222));
        p.setStrokeWidth(4f);

        // Verifica se há ganhador

        //Linhas Horizontais
        if (tabuleiro[0][0] != 0) {
            if (tabuleiro[0][0] == tabuleiro[1][0] && tabuleiro[0][0] == tabuleiro[2][0]) {
                c.drawLine(
                        0,
                        ((height / 3) / 2),
                        width,
                        ((height / 3) / 2),
                        p);
                fimDeJogo(0);
            }
        }
        if (tabuleiro[0][1] != 0) {
            if (tabuleiro[0][1] == tabuleiro[1][1] && tabuleiro[0][1] == tabuleiro[2][1]) {
                c.drawLine(
                        0,
                        ((height / 3) / 2) + (height / 3),
                        width,
                        ((height / 3) / 2) + (height / 3),
                        p);
                fimDeJogo(0);
            }
        }
        if (tabuleiro[0][2] != 0) {
            if (tabuleiro[0][2] == tabuleiro[1][2] && tabuleiro[0][2] == tabuleiro[2][2]) {
                c.drawLine(
                        0,
                        ((height / 3) / 2) + ((height / 3) * 2),
                        width,
                        ((height / 3) / 2) + ((height / 3) * 2),
                        p);
                fimDeJogo(0);
            }
        }

        //Linhas Verticais
        if (tabuleiro[0][0] != 0) {
            if (tabuleiro[0][0] == tabuleiro[0][1] && tabuleiro[0][0] == tabuleiro[0][2]) {
                c.drawLine((width / 3) / 2, 0, (width / 3) / 2, height, p);
                fimDeJogo(0);
            }
        }
        if (tabuleiro[1][0] != 0) {
            if (tabuleiro[1][0] == tabuleiro[1][1] && tabuleiro[1][0] == tabuleiro[1][2]) {
                c.drawLine(
                        ((width / 3) / 2) + (width / 3),
                        0,
                        ((width / 3) / 2) + (width / 3),
                        height,
                        p);
                fimDeJogo(0);
            }
        }
        if (tabuleiro[2][0] != 0) {
            if (tabuleiro[2][0] == tabuleiro[2][1] && tabuleiro[2][0] == tabuleiro[2][2]) {
                c.drawLine(
                        ((width / 3) / 2) + ((width / 3) * 2),
                        0,
                        ((width / 3) / 2) + ((width / 3) * 2),
                        height,
                        p);
                fimDeJogo(0);
            }
        }

        //Linhas Diagonais
        if (tabuleiro[0][0] != 0) {
            if (tabuleiro[0][0] == tabuleiro[1][1] && tabuleiro[0][0] == tabuleiro[2][2]) {
                c.drawLine(0, 0, width, height, p);
                fimDeJogo(0);
            }
        }
        if (tabuleiro[2][0] != 0) {
            if (tabuleiro[2][0] == tabuleiro[1][1] && tabuleiro[2][0] == tabuleiro[0][2]) {
                c.drawLine(0, height, width, 0, p);
                fimDeJogo(0);
            }
        }

        // Verifica se não houve ganhador
        for (int i = 0, m = 0; m < 3; m++) {
            for (int n = 0; n < 3; n++) {
                if (tabuleiro[m][n] != 0) {
                    i++;
                }
            }
            if (i == 9)
                fimDeJogo(1);
        }
    }

    public void fimDeJogo(int i) {
        String msg;
        if (i == 0) {
            if (vezdox) {
                msg = "O Ganhou!";
            } else {
                msg = "X Ganhou!";
            }
        } else {
            msg = "Velha!";
        }

        new AlertDialog.Builder(getContext())
                .setTitle("Fim de Jogo!")
                .setMessage(msg + " Clique em OK para jogar novamente")
                .setCancelable(false)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (onGameAction != null) {
                            onGameAction.onNewGame();
                        }
                    }
                })
                .show();
    }
}
