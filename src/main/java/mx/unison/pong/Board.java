package mx.unison.pong;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Ellipse2D;

public class Board extends JComponent implements KeyListener {

    public enum GameState {
        RUNNING, PAUSED, STOPPED
    }

    private GameState state = GameState.RUNNING;

    private Dimension preferredSize = null;
    private Ellipse2D.Double ball;
    private Paddle paddle;
    private Timer gameTimer;

    private double ballX;
    private double ballY;
    private double velocityX = 4.0;
    private double velocityY = -4.0;

    public static final int MAX_X = 640;
    public static final int MAX_Y = 480;
    public static final int BALL_SIZE = 20;

    private final Font theFont = new Font("SansSerif", Font.BOLD, 24);

    public Board() {
        setOpaque(true);
        setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, Color.BLACK));
        
        ballX = 310;
        ballY = 230;
        ball = new Ellipse2D.Double(ballX, ballY, BALL_SIZE, BALL_SIZE);
        paddle = new Paddle();

        gameTimer = new Timer(20, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (state == GameState.RUNNING) {
                    updateGameLogic();
                }
                repaint();
            }
        });
        gameTimer.start();
    }

    private void updateGameLogic() {
        ballX += velocityX;
        ballY += velocityY;

        ball.x = ballX;
        ball.y = ballY;

        if (paddle.intersects(ball)) {
            velocityY *= -1;
            ballY = paddle.getPaddle().getMinY() - BALL_SIZE;
            ball.y = ballY;
        }

        if (ballX <= 0) {
            velocityX *= -1;
            ballX = 0;
        } else if (ballX + BALL_SIZE >= MAX_X) {
            velocityX *= -1;
            ballX = MAX_X - BALL_SIZE;
        }

        if (ballY <= 0) {
            velocityY *= -1;
            ballY = 0;
        } else if (ballY + BALL_SIZE >= MAX_Y) {
            resetBall();
        }

        ball.x = ballX;
        ball.y = ballY;
    }

    private void resetBall() {
        ballX = 310;
        ballY = 230;
        velocityX = 4.0;
        velocityY = -4.0;
    }

    public void stopGame() {
        state = GameState.STOPPED;
        if (gameTimer != null) {
            gameTimer.stop();
        }
    }

    @Override
    protected void paintComponent(Graphics graphicsContext) {
        super.paintComponent(graphicsContext);
        Graphics2D g = (Graphics2D) graphicsContext;
        if (isOpaque()) {
            g.setColor(getBackground());
            g.fillRect(0, 0, getWidth(), getHeight());
            g.setColor(getForeground());
        }

        if (state == GameState.PAUSED) {
            g.setFont(theFont);
            g.setColor(Color.BLUE);
            g.drawString("Paused", 20, 30);
        }

        g.setStroke(new BasicStroke(5.0f));
        g.setColor(Color.RED);
        g.fill(ball);
        g.setColor(Color.BLACK);
        g.fill(paddle.getPaddle());
    }

    @Override
    public Dimension getPreferredSize() {
        if (preferredSize == null) {
            return new Dimension(MAX_X, MAX_Y);
        } else {
            return super.getPreferredSize();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                if (state == GameState.RUNNING) {
                    paddle.moveLeft();
                }
                break;
            case KeyEvent.VK_RIGHT:
                if (state == GameState.RUNNING) {
                    paddle.moveRight();
                }
                break;
            case KeyEvent.VK_SPACE:
                state = GameState.PAUSED;
                break;
            case KeyEvent.VK_R:
                state = GameState.RUNNING;
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}
}