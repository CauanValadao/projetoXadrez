package xadrez;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class JogoXadrez{
    
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }

    private static void createAndShowGUI(){
        System.out.println("Created GUI on EDT "+ SwingUtilities.isEventDispatchThread());
        JFrame frame = new JFrame("Jogo de Xadrez");
        PainelTabuleiro painel = new PainelTabuleiro();
        ControladorDeJogo controlador = new ControladorDeJogo(painel, 64);
        painel.setControlador(controlador);
        frame.add(painel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
