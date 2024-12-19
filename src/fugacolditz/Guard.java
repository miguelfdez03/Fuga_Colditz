package fugacolditz;

import java.util.concurrent.Semaphore;
import java.util.Random;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class Guard extends Thread {
    private static final int[][] DIRECTIONS = {{-1,0}, {1,0}, {0,-1}, {0,1}};
    private static final Random RANDOM = new Random();
    private static final int MOVE_DELAY = 1000;
    
    private int x;
    private int y;
    private final Semaphore semaphore;
    private final Matrix matrix;

    public Guard(int x, int y, Semaphore semaphore, Matrix matrix) {
        this.x = x;
        this.y = y;
        this.semaphore = semaphore;
        this.matrix = matrix;
    }

    @Override
    public void run() {
        try {
            while (!matrix.lose) {
                semaphore.acquire();
                moveGuard();
                semaphore.release();
                Thread.sleep(MOVE_DELAY);
            }
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }

    private void moveGuard() {
        for (int attempt = 0; attempt < DIRECTIONS.length; attempt++) {
            int[] direction = DIRECTIONS[RANDOM.nextInt(DIRECTIONS.length)];
            if (tryMove(direction[0], direction[1])) {
                return;
            }
        }
    }

    private boolean tryMove(int dx, int dy) {
        int newX = this.x + dx;
        int newY = this.y + dy;
        
        if (!matrix.isValidPosition(newX, newY) || !isValidMove(newX, newY)) {
            return false;
        }

        if (matrix.board[newX][newY].equals("O")) {
            handleCatchPlayer(newX, newY);
            return true;
        }

        updatePosition(newX, newY);
        return true;
    }

    private void handleCatchPlayer(int newX, int newY) {
        matrix.board[newX][newY] = "G";
        matrix.board[this.x][this.y] = "X";
        this.x = newX;
        this.y = newY;
        matrix.lose = true;
        
        // Mostrar mensaje de derrota antes de cerrar
        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(null,
                "¡Un guardia te ha atrapado!\nFin del juego",
                "¡Has perdido!",
                JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        });
    }

    private void updatePosition(int newX, int newY) {
        matrix.board[newX][newY] = "G";
        matrix.board[this.x][this.y] = "X";
        this.x = newX;
        this.y = newY;
    }

    private boolean isValidMove(int x, int y) {
        return !(matrix.board[x][y].equalsIgnoreCase("U") || matrix.board[x][y].equalsIgnoreCase("A")
                || matrix.board[x][y].equalsIgnoreCase("P") || matrix.board[x][y].equalsIgnoreCase("G"));
    }
    
    // Add public getters
    public int getX() {
        return x;
    }
    
    public int getY() {
        return y;
    }
    
    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
