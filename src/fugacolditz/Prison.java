package fugacolditz;

import javax.swing.*;
import java.util.concurrent.Semaphore;

public class Prison {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            String[] options = {"Fácil", "Medio", "Difícil"};
            int difficulty = JOptionPane.showOptionDialog(null, 
                "Selecciona el nivel de dificultad", 
                "Fuga de Colditz", 
                JOptionPane.DEFAULT_OPTION, 
                JOptionPane.QUESTION_MESSAGE, 
                null, 
                options, 
                options[0]);
            
            Semaphore guardSemaphore = new Semaphore(1, true);
            Matrix matrix = new Matrix();
            
            // Optimizar creación de guardias usando un array
            Guard[] guards = new Guard[3];
            for (int i = 0; i < guards.length; i++) {
                guards[i] = new Guard(-1, -1, guardSemaphore, matrix);
            }
            
            // Inicializar tablero
            Tool passport = new Tool("Passport");
            matrix.addTool(passport);
            Tool pliers = new Tool("Pliers");
            matrix.addTool(pliers);
            Tool uniform = new Tool("Uniform");
            matrix.addTool(uniform);
            
            // Configurar dificultad
            int numGuards = difficulty == 0 ? 1 : difficulty == 1 ? 2 : 3;
            for (int i = 0; i < numGuards; i++) {
                matrix.addGuard(guards[i]);
                guards[i].start();
            }
            
            GameWindow gameWindow = new GameWindow();
            matrix.addCharacter(gameWindow); // Pasar la ventana como parámetro
            gameWindow.setMatrix(matrix);
            gameWindow.setVisible(true);
        });
    }
}
