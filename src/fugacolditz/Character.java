package fugacolditz;

import fugacolditz.minigames.*;
import javax.swing.*;
import java.util.concurrent.ExecutionException;
import java.util.Map;

public class Character {
    private static final Map<String, int[]> DIRECTIONS = Map.of(
        "W", new int[]{-1, 0},
        "A", new int[]{0, -1},
        "S", new int[]{1, 0},
        "D", new int[]{0, 1}
    );

    Position position;
    boolean passport;
    boolean pliers;
    boolean uniform;
    int counter;
    private JFrame parentFrame;

    public Character(Position pos, JFrame parentFrame) {
        this.pliers = false;
        this.passport = false;
        this.uniform = false;
        this.position = pos;
        this.counter = 0;
        this.parentFrame = parentFrame;
    }

    public void move(String direction, Matrix matrix) {
        int[] dir = DIRECTIONS.get(direction.toUpperCase());
        if (dir != null) {
            try {
                move(dir[0], dir[1], matrix);
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println("Movimiento inválido");
            }
        }
    }

    private void move(int dx, int dy, Matrix matrix) {
        int newX = position.x() + dx;
        int newY = position.y() + dy;

        if (!matrix.isValidPosition(newX, newY)) {
            return;
        }

        String cell = matrix.board[newX][newY];
        processMove(newX, newY, cell, matrix);
    }

    private void processMove(int newX, int newY, String cell, Matrix matrix) {
        switch (cell) {
            case "P", "A", "U" -> {
                if (collectTool(newX, newY, matrix, cell)) {
                    updatePosition(newX, newY, matrix);
                }
            }
            case "X" -> updatePosition(newX, newY, matrix);
            case "G" -> {
                updatePosition(newX, newY, matrix);
                matrix.lose = true;
            }
        }
    }

    private void updatePosition(int newX, int newY, Matrix matrix) {
        matrix.board[position.x()][position.y()] = "X";
        matrix.board[newX][newY] = "O";
        this.position = new Position(newX, newY);
        this.counter++;
    }

    private boolean collectTool(int x, int y, Matrix matrix, String toolType) {
        try {
            boolean won = false;
            
            SwingUtilities.invokeAndWait(() -> {
                MiniGame game = null;
                try {
                    switch (toolType) {
                        case "P":
                            game = new RockPaperScissors(parentFrame);
                            break;
                        case "A":
                            game = new CoinFlip(parentFrame);
                            break;
                        case "U":
                            game = new DiceGame(parentFrame);  // Ya está usando DiceGame
                            break;
                    }
                    
                    if (game != null) {
                        boolean result = game.showGame().get();
                        if (result) {
                            updateToolStatus(matrix, toolType, x, y);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            
            return won;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private void updateToolStatus(Matrix matrix, String toolType, int x, int y) {
        int toolIndex = toolType.equals("P") ? 0 : toolType.equals("A") ? 1 : 2;
        
        // Actualizar tablero
        for (Position p : matrix.tools.get(toolIndex).toolPositions) {
            matrix.board[p.x()][p.y()] = "X";
        }
        
        // Actualizar inventario
        switch (toolType) {
            case "P": 
                this.passport = true;
                break;
            case "A": 
                this.pliers = true;
                break;
            case "U": 
                this.uniform = true;
                break;
        }
    }
}


