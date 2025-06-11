package estudo.parte22;

import javax.swing.SwingUtilities;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.BorderFactory;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;

public class SwingPaintDemo2 {
   
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI(); 
            }
        });
    }

    private static void createAndShowGUI() {
        System.out.println("Created GUI on EDT? "+
        SwingUtilities.isEventDispatchThread());
        JFrame f = new JFrame("Swing Paint Demo");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.add(new MyPanel());
        f.pack();
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }
}

class MyPanel extends JPanel {

    public MyPanel() {
        setBorder(BorderFactory.createLineBorder(Color.black));
    }

    public Dimension getPreferredSize() {
        return new Dimension(250,200);
    }

    public void paintComponent(Graphics g) {
      /*   super.paintComponent(g);       

        // Draw Text
        g.drawString("This is my custom Panel!",10,20);*/
        //agora colocando o texto no centro
        super.paintComponent(g);

    String text = "This is my custom Panel!";
    
    // Obtem as dimensões do painel
    int panelWidth = getWidth();
    int panelHeight = getHeight();

    // Obtem o tamanho do texto
    FontMetrics fm = g.getFontMetrics();
    int textWidth = fm.stringWidth(text);
    int textHeight = fm.getAscent(); // Altura da linha de base até o topo da letra

    // Calcula coordenadas para centralizar
    int x = (panelWidth - textWidth) / 2;
    int y = (panelHeight + textHeight) / 2;

    g.drawString(text, x, y);
    }  
}