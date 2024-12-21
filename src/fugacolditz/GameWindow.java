package fugacolditz;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.concurrent.Semaphore;

public class GameWindow extends JFrame {
    private Matrix matrix;
    private BoardPanel boardPanel;
    private JLabel statusLabel;
    private JLabel inventoryLabel;
    
    public GameWindow() {
        setTitle("Fuga de Colditz");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        ((JPanel)getContentPane()).setBorder(new EmptyBorder(10, 10, 10, 10));
        getContentPane().setBackground(new Color(245, 245, 245));

        // Panel superior con estilo moderno
        JPanel topPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        statusLabel = new JLabel("Movimientos restantes: 30");
        inventoryLabel = new JLabel("Inventario: ningún objeto");
        topPanel.add(statusLabel);
        topPanel.add(inventoryLabel);
        add(topPanel, BorderLayout.NORTH);
        
        // Panel del tablero
        boardPanel = new BoardPanel();
        add(boardPanel, BorderLayout.CENTER);
        
        // Panel de controles
        JPanel controlPanel = new JPanel();
        JButton upButton = new JButton("↑");
        JButton downButton = new JButton("↓");
        JButton leftButton = new JButton("←");
        JButton rightButton = new JButton("→");
        
        controlPanel.add(leftButton);
        controlPanel.add(downButton);
        controlPanel.add(upButton);
        controlPanel.add(rightButton);
        add(controlPanel, BorderLayout.SOUTH);
        
        // Añadir key listeners
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                handleKeyPress(e.getKeyCode());
            }
        });
        
        // Configurar botones
        upButton.addActionListener(e -> handleKeyPress(KeyEvent.VK_W));
        downButton.addActionListener(e -> handleKeyPress(KeyEvent.VK_S));
        leftButton.addActionListener(e -> handleKeyPress(KeyEvent.VK_A));
        rightButton.addActionListener(e -> handleKeyPress(KeyEvent.VK_D));
        
        setFocusable(true);
        pack();
        setLocationRelativeTo(null);
    }
    
    public void setMatrix(Matrix matrix) {
        this.matrix = matrix;
        boardPanel.setMatrix(matrix);
        updateStatus();
    }
    
    private void handleKeyPress(int keyCode) {
        if (matrix == null || matrix.lose || matrix.character.counter >= 30) return;
        
        String direction = getDirectionFromKey(keyCode);
        if (!direction.isEmpty()) {
            // Desactivar los controles mientras se procesa el movimiento
            setEnabled(false);
            
            // Ejecutar el movimiento en un hilo separado
            SwingWorker<Void, Void> worker = new SwingWorker<>() {
                @Override
                protected Void doInBackground() throws Exception {
                    matrix.character.move(direction, matrix);
                    return null;
                }
                
                @Override
                protected void done() {
                    updateGameState();
                    setEnabled(true);
                    requestFocus(); // Recuperar el foco para los controles de teclado
                }
            };
            worker.execute();
        }
    }

    private String getDirectionFromKey(int keyCode) {
        switch (keyCode) {
            case KeyEvent.VK_W: return "W";
            case KeyEvent.VK_S: return "S";
            case KeyEvent.VK_A: return "A";
            case KeyEvent.VK_D: return "D";
            default: return "";
        }
    }

    private void updateGameState() {
        updateStatus();
        boardPanel.repaint();
            
        if (checkWinCondition()) {
            showGameEndDialog(true);
        } else if (matrix.lose) {
            showGameEndDialog(false);
        } else if (matrix.character.counter >= 30) {
            showExceededMovesDialog();
        }
    }

    private boolean checkWinCondition() {
        return matrix.character.pliers && 
               matrix.character.passport && 
               matrix.character.uniform;
    }

    private void showGameEndDialog(boolean won) {
        String message = won ? "¡HAS GANADO!" : "¡HAS PERDIDO!";
        String title = won ? "Victoria" : "Derrota";
        int messageType = won ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE;
        
        SwingUtilities.invokeLater(() -> {
            int option = JOptionPane.showConfirmDialog(this, 
                message + "\n¿Quieres jugar de nuevo?", 
                title, 
                JOptionPane.YES_NO_OPTION, 
                messageType);
            if (option == JOptionPane.YES_OPTION) {
                restartGame();
            } else {
                System.exit(0);
            }
        });
    }
    
    private void showExceededMovesDialog() {
        SwingUtilities.invokeLater(() -> {
            int option = JOptionPane.showConfirmDialog(this, 
                "Has excedido el límite de 30 movimientos.\nFin del juego.\n¿Quieres jugar de nuevo?", 
                "¡Has perdido!", 
                JOptionPane.YES_NO_OPTION, 
                JOptionPane.ERROR_MESSAGE);
            if (option == JOptionPane.YES_OPTION) {
                restartGame();
            } else {
                System.exit(0);
            }
        });
    }

    private void restartGame() {
        String[] options = {"Fácil", "Medio", "Difícil"};
        int difficulty = JOptionPane.showOptionDialog(this, 
            "Selecciona el nivel de dificultad", 
            "Fuga de Colditz", 
            JOptionPane.DEFAULT_OPTION, 
            JOptionPane.QUESTION_MESSAGE, 
            null, 
            options, 
            options[0]);
        
        Semaphore guardSemaphore = new Semaphore(1, true);
        Matrix newMatrix = new Matrix();
        
        Guard[] guards = new Guard[3];
        for (int i = 0; i < guards.length; i++) {
            guards[i] = new Guard(-1, -1, guardSemaphore, newMatrix, difficulty);
        }
        
        Tool passport = new Tool("Passport");
        newMatrix.addTool(passport);
        Tool pliers = new Tool("Pliers");
        newMatrix.addTool(pliers);
        Tool uniform = new Tool("Uniform");
        newMatrix.addTool(uniform);
        
        int numGuards = difficulty == 0 ? 1 : difficulty == 1 ? 2 : 3;
        for (int i = 0; i < numGuards; i++) {
            newMatrix.addGuard(guards[i]);
            guards[i].start();
        }
        
        newMatrix.addCharacter(this);
        setMatrix(newMatrix);
    }

    private void updateStatus() {
        statusLabel.setText("Movimientos restantes: " + (30 - matrix.character.counter));
        String inventory = "Inventario: ";
        if (matrix.character.passport) inventory += "Pasaporte ";
        if (matrix.character.pliers) inventory += "Alicates ";
        if (matrix.character.uniform) inventory += "Uniforme ";
        if (!matrix.character.passport && !matrix.character.pliers && !matrix.character.uniform) {
            inventory += "ningún objeto";
        }
        inventoryLabel.setText(inventory);
    }
}
