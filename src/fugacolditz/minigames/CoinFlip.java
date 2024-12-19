package fugacolditz.minigames;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class CoinFlip extends MiniGame {
    private JLabel coinLabel;
    private Timer flipTimer;
    private int flipCount;
    
    public CoinFlip(JFrame parent) {
        super(parent, "Cara o Cruz");
        
        setLayout(new BorderLayout(10, 10));
        
        // Etiqueta de instrucciones
        JLabel instructionLabel = new JLabel("Elige tu apuesta:", SwingConstants.CENTER);
        instructionLabel.setFont(new Font("Arial", Font.BOLD, 14));
        add(instructionLabel, BorderLayout.NORTH);
        
        // Panel de la moneda
        coinLabel = new JLabel("cara", SwingConstants.CENTER);
        coinLabel.setFont(new Font("Arial", Font.PLAIN, 48));
        add(coinLabel, BorderLayout.CENTER);
        
        // Panel de botones
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 5, 5));
        JButton headsButton = createChoiceButton("Cara", 1);
        JButton tailsButton = createChoiceButton("Cruz", 2);
        buttonPanel.add(headsButton);
        buttonPanel.add(tailsButton);
        
        // Panel inferior con resultado
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(buttonPanel, BorderLayout.NORTH);
        bottomPanel.add(resultLabel, BorderLayout.SOUTH);
        
        add(bottomPanel, BorderLayout.SOUTH);
        
        pack();
        setSize(250, 200);
    }
    
    private JButton createChoiceButton(String text, int choice) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.addActionListener(e -> {
            disableButtons();
            flipCoin(choice);
        });
        return button;
    }
    
    private void flipCoin(int playerChoice) {
        flipCount = 0;
        int computerChoice = (int)(Math.random() * 2 + 1);
        
        flipTimer = new Timer(100, (ActionEvent e) -> {
            flipCount++;
            coinLabel.setText(flipCount % 2 == 0 ? "cara" : "cruz");
            
            if (flipCount >= 10) {
                flipTimer.stop();
                boolean won = (playerChoice == computerChoice);
                coinLabel.setText(computerChoice == 1 ? "cara" : "cruz");
                showResult(won);
            }
        });
        flipTimer.start();
    }
    
    private void disableButtons() {
        for (Component comp : getContentPane().getComponents()) {
            if (comp instanceof JPanel) {
                for (Component button : ((JPanel) comp).getComponents()) {
                    if (button instanceof JButton) {
                        button.setEnabled(false);
                    }
                }
            }
        }
    }
}
