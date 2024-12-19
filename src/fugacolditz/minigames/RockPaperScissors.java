package fugacolditz.minigames;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class RockPaperScissors extends MiniGame {
    private JLabel computerChoiceLabel;
    private JPanel buttonPanel;
    private static final Color BUTTON_HOVER = new Color(230, 240, 255);
    private static final Color BUTTON_PRESSED = new Color(200, 220, 255);
    
    public RockPaperScissors(JFrame parent) {
        super(parent, "Piedra, Papel o Tijeras");
        
        setLayout(new BorderLayout(15, 15));
        getContentPane().setBackground(new Color(245, 245, 250));
        
        // Panel de instrucciones con tamaño ajustado
        JLabel instructionLabel = createStyledLabel("¡Elige tu jugada!", 24);
        add(createStyledPanel(instructionLabel), BorderLayout.NORTH);
        
        // Panel de elección del computador con tamaño ajustado
        computerChoiceLabel = createStyledLabel("", 20);
        computerChoiceLabel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Panel de botones con mejor diseño
        buttonPanel = new JPanel(new GridLayout(1, 3, 20, 20)); // Aumentado el espacio entre botones
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        // Usar caracteres Unicode más visibles
        addGameButton("Piedra", 1, "⬤");  // Círculo sólido para piedra
        addGameButton("Papel", 2, "◻");    // Cuadrado para papel
        addGameButton("Tijeras", 3, "✂");  // Tijeras se mantiene
        
        // Panel central mejorado
        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.setOpaque(false);
        centerPanel.add(buttonPanel, BorderLayout.CENTER);
        centerPanel.add(computerChoiceLabel, BorderLayout.SOUTH);
        
        add(centerPanel, BorderLayout.CENTER);
        add(createResultPanel(), BorderLayout.SOUTH);
        
        setSize(500, 400); // Ventana más grande
        setLocationRelativeTo(parent);
    }
    
    private void addGameButton(String text, int choice, String symbol) {
        JButton button = new JButton(createButtonContent(text, symbol));
        styleButton(button);
        button.addActionListener(e -> {
            disableAllButtons();
            playGame(choice);
        });
        button.addMouseListener(new ButtonHoverEffect(button));
        buttonPanel.add(button);
    }
    
    private String createButtonContent(String text, String symbol) {
        return "<html><center>" +
               "<span style='font-size:32px'>" + symbol + "</span><br>" +
               "<span style='font-size:18px'>" + text + "</span>" +
               "</center></html>";
    }
    
    private void styleButton(JButton button) {
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setForeground(new Color(50, 50, 50));
        button.setBackground(new Color(255, 255, 255));
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        button.setFocusPainted(false);
    }
    
    private JPanel createResultPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        resultLabel.setFont(new Font("Arial", Font.BOLD, 18));
        panel.add(resultLabel, BorderLayout.CENTER);
        return panel;
    }
    
    private void playGame(int playerChoice) {
        int computerChoice = (int)(Math.random() * 3 + 1);
        String[] options = {"⬤ Piedra", "◻ Papel", "✂ Tijeras"};
        
        // Animación de elección
        Timer animationTimer = new Timer(100, null);
        final int[] animationCount = {0};
        animationTimer.addActionListener(e -> {
            if (animationCount[0] < 10) {
                computerChoiceLabel.setText("El guardia está eligiendo... " + 
                    options[animationCount[0] % 3]);
                animationCount[0]++;
            } else {
                animationTimer.stop();
                computerChoiceLabel.setText("El guardia eligió: " + options[computerChoice - 1]);
                boolean won = (computerChoice == 1 && playerChoice == 2) || 
                            (computerChoice == 2 && playerChoice == 3) || 
                            (computerChoice == 3 && playerChoice == 1);
                showResult(won);
            }
        });
        animationTimer.start();
    }
    
    private void disableAllButtons() {
        for (Component comp : buttonPanel.getComponents()) {
            if (comp instanceof JButton) {
                comp.setEnabled(false);
            }
        }
    }
    
    // Agregar estos métodos de utilidad
    private JLabel createStyledLabel(String text, int fontSize) {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, fontSize));
        label.setForeground(new Color(50, 50, 50));
        return label;
    }

    private JPanel createStyledPanel(JComponent component) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        panel.add(component, BorderLayout.CENTER);
        return panel;
    }

    // Clase interna para efectos de hover en botones
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
        
        @Override
        public void mouseReleased(MouseEvent e) {
            if (button.isEnabled()) {
                button.setBackground(BUTTON_HOVER);
            }
        }
    }
}
