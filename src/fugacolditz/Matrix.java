package fugacolditz;

import java.util.ArrayList;
import javax.swing.JFrame;
import java.util.Arrays;

public class Matrix {
    private static final int BOARD_SIZE = 10;
    public final String[][] board = new String[BOARD_SIZE][BOARD_SIZE];
    public final ArrayList<Tool> tools;
    public Character character;
    public Guard guard;
    public boolean lose;

    public Matrix() {
        this.lose = false;
        tools = new ArrayList<>();
        for (String[] row : board) {
            Arrays.fill(row, "X");
        }
    }

    // Método optimizado para verificar posiciones
    public boolean isValidPosition(int x, int y) {
        return x >= 0 && x < BOARD_SIZE && y >= 0 && y < BOARD_SIZE;
    }

    public boolean isEmptyPosition(int x, int y) {
        return isValidPosition(x, y) && board[x][y].equals("X");
    }

    public String printBoard() {
        StringBuilder output = new StringBuilder();
        for (String[] row : board) {
            for (String cell : row) {
                output.append(" ").append(cell).append(" ");
            }
            output.append("\n");
        }
        return output.toString();
    }

    public void addTool(Tool tool) {
        boolean valid = false;
        while (!valid) {
            int x = (int) (Math.random() * 10);
            int y = (int) (Math.random() * 10);
            if (isValidToolPosition(tool, x, y)) {
                placeTool(tool, x, y);
                valid = true;
            }
        }
    }

    private boolean isValidToolPosition(Tool tool, int x, int y) {
        switch (tool.type) {
            case "Uniform":
                return isEmptyPosition(x, y) && isEmptyPosition(x + 1, y) && isEmptyPosition(x - 1, y);
            case "Pliers":
                return isEmptyPosition(x, y) && (isEmptyPosition(x - 1, y) || isEmptyPosition(x + 1, y) || 
                       isEmptyPosition(x, y - 1) || isEmptyPosition(x, y + 1));
            case "Passport":
                return isEmptyPosition(x, y);
            default:
                return false;
        }
    }

    private void placeTool(Tool tool, int x, int y) {
        switch (tool.type) {
            case "Uniform":
                board[x][y] = "U";
                board[x + 1][y] = "U";
                board[x - 1][y] = "U";
                tool.toolPositions.add(new Position(x, y));
                tool.toolPositions.add(new Position(x + 1, y));
                tool.toolPositions.add(new Position(x - 1, y));
                break;
            case "Pliers":
                board[x][y] = "A";
                if (isEmptyPosition(x - 1, y)) {
                    board[x - 1][y] = "A";
                    tool.toolPositions.add(new Position(x - 1, y));
                } else if (isEmptyPosition(x + 1, y)) {
                    board[x + 1][y] = "A";
                    tool.toolPositions.add(new Position(x + 1, y));
                } else if (isEmptyPosition(x, y - 1)) {
                    board[x][y - 1] = "A";
                    tool.toolPositions.add(new Position(x, y - 1));
                } else if (isEmptyPosition(x, y + 1)) {
                    board[x][y + 1] = "A";
                    tool.toolPositions.add(new Position(x, y + 1));
                }
                tool.toolPositions.add(new Position(x, y));
                break;
            case "Passport":
                board[x][y] = "P";
                tool.toolPositions.add(new Position(x, y));
                break;
        }
        tools.add(tool);
    }

    public void addCharacter(JFrame parentFrame) {
        boolean valid = false;
        while (!valid) {
            int x = (int) (Math.random() * 10);
            int y = (int) (Math.random() * 10);
            if (isEmptyPosition(x, y)) {
                Position pos = new Position(x, y);
                character = new Character(pos, parentFrame);
                board[x][y] = "O";
                valid = true;
            }
        }
    }

    public void addGuard(Guard guard) {
        boolean valid = false;
        while (!valid) {
            int x = (int) (Math.random() * 10);
            int y = (int) (Math.random() * 10);
            if (isEmptyPosition(x, y)) {
                guard.setPosition(x, y);
                board[x][y] = "G";
                valid = true;
            }
        }
    }
}
