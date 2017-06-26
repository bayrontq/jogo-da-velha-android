package com.bayron.jogodavelha.views;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import com.bayron.jogodavelha.R;
import com.bayron.jogodavelha.interfaces.OnGameAction;

public class BoardView extends View {

    private final int[][] board = {
            {0, 0, 0},
            {0, 0, 0},
            {0, 0, 0}
    };
    private final int matrixSize = 3;
    private final int margin = 40;
    private int boardSize;
    private int squareSize;
    private int boardX;
    private int boardY;
    private boolean xTurn;
    private Paint paint;
    private OnGameAction onGameAction;

    public BoardView(Context context) {
        super(context);

        initialize(context);
    }

    public BoardView(Context context, AttributeSet attrs) {
        super(context, attrs);

        initialize(context);
    }

    public BoardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initialize(context);
    }

    @SuppressWarnings("unused")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public BoardView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        initialize(context);
    }

    private void initialize(Context context) {
        setBackgroundResource(R.drawable.splash_bg);

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        if (height >= width) {
            boardSize = width - margin;
        } else {
            boardSize = height - margin;
        }
        boardX = (width - boardSize) / 2;
        boardY = (height - boardSize) / 2;
        squareSize = boardSize / matrixSize;

        xTurn = true;

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(ContextCompat.getColor(getContext(), R.color.boardColor));
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(20f);
    }

    public void setOnGameAction(OnGameAction onGameAction) {
        this.onGameAction = onGameAction;
    }

    @Override
    public boolean onTouchEvent(MotionEvent me) {
        if (me.getAction() == MotionEvent.ACTION_UP) {
            float x = me.getX();
            float y = me.getY();

            // Find place
            for (int j = 0; j < matrixSize; j++) {
                for (int i = 0; i < matrixSize; i++) {
                    if (x > boardX + i * squareSize
                            && x < boardX + (i + 1) * squareSize
                            && y > boardY + j * squareSize
                            && y < boardY + (j + 1) * squareSize) {
                        if (board[i][j] == 0) {
                            board[i][j] = (xTurn ? 1 : 2);
                            xTurn =! xTurn;
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

        // Draw initial board
        // Vertical lines
        c.drawLine(
                boardX + squareSize,
                boardY,
                boardX + squareSize,
                boardY + boardSize,
                paint);
        c.drawLine(
                boardX + squareSize * 2,
                boardY,
                boardX + squareSize * 2,
                boardY + boardSize,
                paint);
        // Horizontal lines
        c.drawLine(
                boardX,
                boardY + squareSize,
                boardX + boardSize,
                boardY + squareSize,
                paint);
        c.drawLine(
                boardX,
                boardY + squareSize * 2,
                boardX + boardSize,
                boardY + squareSize * 2,
                paint);

        // Draw O
        for (int j = 0; j < matrixSize; j++) {
            for (int i = 0; i < matrixSize; i++) {
                if (board[i][j] == 2) {
                    c.drawCircle(
                            boardX + (squareSize / 2) + (i * squareSize),
                            boardY + (squareSize / 2) + (j * squareSize),
                            (squareSize / 2) - margin,
                            paint);
                }
            }
        }

        // Draw X
        for (int j = 0; j < matrixSize; j++) {
            for (int i = 0; i < matrixSize; i++) {
                if (board[i][j] == 1) {
                    c.drawLine(
                            boardX + margin + i * squareSize,
                            boardY + margin + j * squareSize,
                            boardX - margin + (i + 1) * squareSize,
                            boardY - margin + (j + 1) * squareSize,
                            paint);
                    c.drawLine(
                            boardX + margin + i * squareSize,
                            boardY - margin + (j + 1) * squareSize,
                            boardX - margin + (i + 1) * squareSize,
                            boardY + margin + j * squareSize,
                            paint);
                }
            }
        }

        // Check if there is a winner
        // Horizotal
        if (board[0][0] != 0) {
            if (board[0][0] == board[1][0] && board[0][0] == board[2][0]) {
                c.drawLine(
                        boardX,
                        boardY + (squareSize / 2),
                        boardX + boardSize,
                        boardY + (squareSize / 2),
                        paint);
                handleEndGame(true);
            }
        }
        if (board[0][1] != 0) {
            if (board[0][1] == board[1][1] && board[0][1] == board[2][1]) {
                c.drawLine(
                        boardX,
                        boardY + (squareSize / 2) + squareSize,
                        boardX + boardSize,
                        boardY + (squareSize / 2) + squareSize,
                        paint);
                handleEndGame(true);
            }
        }
        if (board[0][2] != 0) {
            if (board[0][2] == board[1][2] && board[0][2] == board[2][2]) {
                c.drawLine(
                        boardX,
                        boardY + (squareSize / 2) + (squareSize * 2),
                        boardX + boardSize,
                        boardY + (squareSize / 2) + (squareSize * 2),
                        paint);
                handleEndGame(true);
            }
        }

        // Vertical
        if (board[0][0] != 0) {
            if (board[0][0] == board[0][1] && board[0][0] == board[0][2]) {
                c.drawLine(
                        boardX + squareSize / 2,
                        boardY,
                        boardX + squareSize / 2,
                        boardY + boardSize,
                        paint);
                handleEndGame(true);
            }
        }
        if (board[1][0] != 0) {
            if (board[1][0] == board[1][1] && board[1][0] == board[1][2]) {
                c.drawLine(
                        boardX + (squareSize / 2) + squareSize,
                        boardY,
                        boardX + (squareSize / 2) + squareSize,
                        boardY + boardSize,
                        paint);
                handleEndGame(true);
            }
        }
        if (board[2][0] != 0) {
            if (board[2][0] == board[2][1] && board[2][0] == board[2][2]) {
                c.drawLine(
                        boardX + (squareSize / 2) + (squareSize * 2),
                        boardY,
                        boardX + (squareSize / 2) + (squareSize * 2),
                        boardY + boardSize,
                        paint);
                handleEndGame(true);
            }
        }

        // Diagonals
        if (board[0][0] != 0) {
            if (board[0][0] == board[1][1] && board[0][0] == board[2][2]) {
                c.drawLine(
                        boardX,
                        boardY,
                        boardX + boardSize,
                        boardY + boardSize,
                        paint);
                handleEndGame(true);
            }
        }
        if (board[2][0] != 0) {
            if (board[2][0] == board[1][1] && board[2][0] == board[0][2]) {
                c.drawLine(
                        boardX,
                        boardY + boardSize,
                        boardX + boardSize,
                        boardY,
                        paint);
                handleEndGame(true);
            }
        }

        // Check if there is no winner
        for (int i = 0, m = 0; m < matrixSize; m++) {
            for (int n = 0; n < matrixSize; n++) {
                if (board[m][n] != 0) {
                    i++;
                }
            }
            if (i == 9) {
                handleEndGame(false);
            }
        }
    }

    private void handleEndGame(boolean hasWinner) {
        String message;
        if (hasWinner) {
            if (xTurn) {
                message = getContext().getString(R.string.the_winner_was_the_o);
            } else {
                message = getContext().getString(R.string.the_winner_was_the_x);
            }
        } else {
            message = getContext().getString(R.string.game_tied);
        }

        new AlertDialog.Builder(getContext())
                .setMessage(message)
                .setCancelable(false)
                .setNegativeButton(R.string.exit, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (onGameAction != null) {
                            onGameAction.onExitGame();
                        }
                    }
                })
                .setPositiveButton(R.string.play_again, new DialogInterface.OnClickListener() {
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
