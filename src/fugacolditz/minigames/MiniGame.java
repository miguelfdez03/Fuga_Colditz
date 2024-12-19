package fugacolditz.minigames;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.CompletableFuture;

public abstract class MiniGame extends JDialog {
    protected CompletableFuture<Boolean> result;
    protected JLabel resultLabel;
    
    public MiniGame(JFrame parent, String title) {
        super(parent, title, true);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setResizable(false);
        result = new CompletableFuture<>();
        
        // Configuración básica del diálogo
        setLayout(new BorderLayout(10, 10));
        getRootPane().setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        getContentPane().setBackground(new Color(245, 245, 245));
        
        // Label para mostrar resultado
        resultLabel = new JLabel("", SwingConstants.CENTER);
        resultLabel.setFont(new Font("Arial", Font.BOLD, 24));
        add(resultLabel, BorderLayout.SOUTH);
        resultLabel.setVisible(false);
    }

    public CompletableFuture<Boolean> showGame() {
        setLocationRelativeTo(getParent());
        setVisible(true);
        return result;
    }
    
    protected void showResult(boolean won) {
        resultLabel.setVisible(true);
        resultLabel.setText(won ? "¡Victoria!" : "¡Derrota!");
        resultLabel.setForeground(won ? new Color(46, 125, 50) : new Color(211, 47, 47));
        
        // Timer para cerrar el diálogo después de mostrar el resultado
        Timer timer = new Timer(1500, e -> {
            result.complete(won);
            dispose();
        });
        timer.setRepeats(false);
        timer.start();
    }
}
