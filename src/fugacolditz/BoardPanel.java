package fugacolditz;

import javax.swing.*;
import java.awt.*;

public class BoardPanel extends JPanel {
    private Matrix matrix;
    private static final int CELL_SIZE = 60;
    private static final Color EMPTY_COLOR = new Color(220, 220, 220);
    private static final Color WALL_COLOR = new Color(100, 100, 100);
    private static final Font CELL_FONT = new Font("Arial", Font.BOLD, 20);
    
    public BoardPanel() {
        setPreferredSize(new Dimension(10 * CELL_SIZE, 10 * CELL_SIZE));
        setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));
    }
    
    public void setMatrix(Matrix matrix) {
        this.matrix = matrix;
        repaint();
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (matrix == null) return;
        
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Dibujar fondo
        g2d.setColor(new Color(240, 240, 240));
        g2d.fillRect(0, 0, getWidth(), getHeight());
        
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                int x = j * CELL_SIZE;
                int y = i * CELL_SIZE;
                
                // Dibujar celda con efecto 3D
                g2d.setColor(EMPTY_COLOR);
                g2d.fillRoundRect(x + 2, y + 2, CELL_SIZE - 4, CELL_SIZE - 4, 10, 10);
                g2d.setColor(Color.DARK_GRAY);
                g2d.drawRoundRect(x + 2, y + 2, CELL_SIZE - 4, CELL_SIZE - 4, 10, 10);
                
                // Dibujar contenido
                switch (matrix.board[i][j]) {
                    case "O": // Jugador
                        drawPlayer(g2d, x, y);
                        break;
                    case "G": // Guardia
                        drawGuard(g2d, x, y);
                        break;
                    case "P": // Pasaporte
                        drawItem(g2d, x, y, new Color(46, 125, 50), "P");
                        break;
                    case "A": // Alicates
                        drawItem(g2d, x, y, new Color(251, 192, 45), "A");
                        break;
                    case "U": // Uniforme
                        drawItem(g2d, x, y, new Color(230, 81, 0), "U");
                        break;
                }
            }
        }
    }

    private void drawPlayer(Graphics2D g2d, int x, int y) {
        // Dibujar silueta del prisionero
        int padding = 8;
        
        // Color base y sombra
        g2d.setColor(new Color(30, 136, 229));
        g2d.fillRoundRect(x + padding, y + padding, CELL_SIZE - 2*padding, CELL_SIZE - 2*padding, 10, 10);
        
        // Detalles del personaje en blanco
        g2d.setColor(Color.WHITE);
        // Cabeza
        g2d.fillOval(x + CELL_SIZE/2 - 8, y + padding + 5, 16, 16);
        // Cuerpo
        g2d.fillRect(x + CELL_SIZE/2 - 6, y + padding + 18, 12, 20);
        // Brazos
        g2d.fillRect(x + CELL_SIZE/2 - 15, y + padding + 22, 30, 4);
        // Piernas
        g2d.fillRect(x + CELL_SIZE/2 - 10, y + padding + 35, 6, 12);
        g2d.fillRect(x + CELL_SIZE/2 + 4, y + padding + 35, 6, 12);
        
        // Borde
        g2d.setColor(new Color(21, 101, 192));
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRoundRect(x + padding, y + padding, CELL_SIZE - 2*padding, CELL_SIZE - 2*padding, 10, 10);
    }

    private void drawGuard(Graphics2D g2d, int x, int y) {
        // Dibujar silueta del guardia
        int padding = 8;
        
        // Color base y sombra
        g2d.setColor(new Color(211, 47, 47));
        g2d.fillRoundRect(x + padding, y + padding, CELL_SIZE - 2*padding, CELL_SIZE - 2*padding, 10, 10);
        
        // Detalles del guardia
        g2d.setColor(Color.WHITE);
        // Gorra
        g2d.fillRect(x + CELL_SIZE/2 - 12, y + padding + 5, 24, 6);
        g2d.fillRect(x + CELL_SIZE/2 - 8, y + padding + 2, 16, 8);
        // Cabeza
        g2d.fillOval(x + CELL_SIZE/2 - 8, y + padding + 8, 16, 16);
        // Cuerpo
        g2d.fillRect(x + CELL_SIZE/2 - 7, y + padding + 21, 14, 22);
        // Brazos
        g2d.fillRect(x + CELL_SIZE/2 - 15, y + padding + 25, 30, 6);
        // Piernas
        g2d.fillRect(x + CELL_SIZE/2 - 10, y + padding + 40, 7, 10);
        g2d.fillRect(x + CELL_SIZE/2 + 3, y + padding + 40, 7, 10);
        
        // Borde
        g2d.setColor(new Color(183, 28, 28));
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRoundRect(x + padding, y + padding, CELL_SIZE - 2*padding, CELL_SIZE - 2*padding, 10, 10);
    }

    private void drawItem(Graphics2D g2d, int x, int y, Color color, String text) {
        // Dibujar fondo del item
        g2d.setColor(color);
        g2d.fillRoundRect(x + 15, y + 15, CELL_SIZE - 30, CELL_SIZE - 30, 8, 8);
        
        // Dibujar borde
        g2d.setColor(color.darker());
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRoundRect(x + 15, y + 15, CELL_SIZE - 30, CELL_SIZE - 30, 8, 8);
        
        // Dibujar texto
        g2d.setColor(Color.WHITE);
        g2d.setFont(CELL_FONT);
        FontMetrics fm = g2d.getFontMetrics();
        int textX = x + (CELL_SIZE - fm.stringWidth(text)) / 2;
        int textY = y + (CELL_SIZE + fm.getAscent() - fm.getDescent()) / 2;
        g2d.drawString(text, textX, textY);
    }
}
