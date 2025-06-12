package estudo.parte2;

import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class Imagempeca {
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }

    private static void createAndShowGUI() {
        System.out.println("Created GUI on EDT? " + SwingUtilities.isEventDispatchThread());
        JFrame frame = new JFrame("Imagempeca");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        JPanel painel = new JPanel();

        URL urlDaImagem = Imagempeca.class.getResource("images/chess.png");

        if (urlDaImagem == null) {
            System.err.println("Erro: Não foi possível encontrar a imagem.");
            System.err.println("Verifique se a pasta 'images' está no mesmo local que o arquivo .java");
            painel.add(new JLabel("Erro ao carregar imagem!"));
        } else {
            ImageIcon iconePeca = new ImageIcon(urlDaImagem);
            JLabel labelDaPeca = new JLabel(iconePeca);
            painel.add(labelDaPeca);
        }

        

        frame.add(painel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
