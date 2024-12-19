package fugacolditz.minigames;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class DiceGame extends MiniGame {
    private JLabel diceLabel;
    private JPanel buttonPanel;
    private Timer animationTimer;
    private static final Color BUTTON_HOVER = new Color(230, 240, 255);
    private static final Color BUTTON_PRESSED = new Color(200, 220, 255);
    
    public DiceGame(JFrame parent) {
        super(parent, "Juego de Dados");
        
        setLayout(new BorderLayout(20, 20)); // Aumentado el espaciado
        getContentPane().setBackground(new Color(245, 245, 250));
        
        // Panel de instrucciones con más espacio
        JLabel instructionLabel = createStyledLabel("<html><div style='text-align: center; width: 300px;'>" +
            "¡Haz tu predicción!<br>¿El dado mostrará un número mayor que 3?</div></html>", 20);
        JPanel instructionPanel = createStyledPanel(instructionLabel);
        instructionPanel.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));
        add(instructionPanel, BorderLayout.NORTH);
        
        // Panel del dado mejorado y más grande
        diceLabel = new JLabel("?", SwingConstants.CENTER);
        diceLabel.setFont(new Font("Arial", Font.BOLD, 96)); // Aumentado tamaño de fuente
        diceLabel.setPreferredSize(new Dimension(150, 150)); // Tamaño fijo para el dado
        diceLabel.setForeground(new Color(70, 70, 70));
        diceLabel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 3),
            BorderFactory.createEmptyBorder(25, 25, 25, 25)
        ));
        diceLabel.setBackground(Color.WHITE);
        diceLabel.setOpaque(true);
        
        JPanel dicePanel = createStyledPanel(diceLabel);
        dicePanel.setBorder(BorderFactory.createEmptyBorder(20, 60, 20, 60));
        add(dicePanel, BorderLayout.CENTER);
        
        // Botones más grandes y con mejor disposición
        buttonPanel = new JPanel(new GridLayout(2, 1, 15, 15)); // Cambiado a disposición vertical
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));
        
        addGameButton("Mayor que 3 (4, 5, 6)", true);
        addGameButton("Menor o igual a 3 (1, 2, 3)", false);
        
        add(buttonPanel, BorderLayout.SOUTH);
        
        setSize(500, 500); // Ventana más grande
        setLocationRelativeTo(parent);
    }
    
    private void addGameButton(String text, boolean higherPrediction) {
        JButton button = new JButton(text);
        styleButton(button);
        button.addActionListener(e -> {
            disableButtons();
            rollDice(higherPrediction);
        });
        button.addMouseListener(new ButtonHoverEffect(button));
        buttonPanel.add(button);
    }
    
    private void styleButton(JButton button) {
        button.setFont(new Font("Arial", Font.BOLD, 16)); // Fuente más grande
        button.setForeground(new Color(50, 50, 50));
        button.setBackground(Color.WHITE);
        button.setPreferredSize(new Dimension(200, 60)); // Tamaño fijo para botones
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 2),
            BorderFactory.createEmptyBorder(15, 25, 15, 25)
        ));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
    
    private void rollDice(boolean higherPrediction) {
        int[] sequence = generateRandomSequence();
        animationTimer = new Timer(120, null);
        final int[] rolls = {0};
        
        animationTimer.addActionListener(e -> {
            if (rolls[0] < sequence.length - 1) {
                updateDiceDisplay(sequence[rolls[0]]);
                rolls[0]++;
            } else {
                animationTimer.stop();
                int finalResult = sequence[sequence.length - 1];
                updateDiceDisplay(finalResult);
                boolean won = (finalResult > 3 && higherPrediction) || 
                            (finalResult <= 3 && !higherPrediction);
                showResult(won);
            }
        });
        animationTimer.start();
    }

    private void updateDiceDisplay(int number) {
        diceLabel.setText(String.valueOf(number));
        diceLabel.setFont(new Font("Arial", Font.BOLD, 96)); // Mantener tamaño grande durante la animación
        Color diceColor = number > 3 ? new Color(46, 125, 50) : new Color(211, 47, 47);
        diceLabel.setForeground(diceColor);
    }

    private int[] generateRandomSequence() {
        int[] sequence = new int[12];
        for (int i = 0; i < sequence.length - 1; i++) {
            sequence[i] = (int)(Math.random() * 6) + 1;
        }
        sequence[sequence.length - 1] = (int)(Math.random() * 6) + 1; // Resultado final
        return sequence;
    }
    
    private void disableButtons() {
        for (Component comp : buttonPanel.getComponents()) {
            if (comp instanceof JButton) {
                comp.setEnabled(false);
            }
        }
    }
    
    private JLabel createStyledLabel(String text, int fontSize) {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, fontSize));
        label.setForeground(new Color(50, 50, 50));
        return label;
    }
    
    private JPanel createStyledPanel(JComponent component) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.add(component, BorderLayout.CENTER);
        return panel;
    }
    
    private class ButtonHoverEffect extends MouseAdapter {
        private final JButton button;
        
        public ButtonHoverEffect(JButton button) {
            this.button = button;
        }
        
        @Override
        public void mouseEntered(MouseEvent e) {
            if (button.isEnabled()) {
                button.setBackground(BUTTON_HOVER);
            }
        }
        
        @Override
        public void mouseExited(MouseEvent e) {
            if (button.isEnabled()) {
                button.setBackground(Color.WHITE);
            }
        }
        
        @Override
        public void mousePressed(MouseEvent e) {
            if (button.isEnabled()) {
                button.setBackground(BUTTON_PRESSED);
            }
        }
    }
}
