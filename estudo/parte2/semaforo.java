package estudo.parte2;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class semaforo {
    
    public static void main(String[] args){
        SwingUtilities.invokeLater(new Runnable() {
            public void run(){
                createAndShowGUI();
            }
        });
    }

    private static void createAndShowGUI(){
        System.out.println("Created GUI on EDT? " + SwingUtilities.isEventDispatchThread());
        JFrame frame = new JFrame("semaforo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new MySemaforo());
        frame.pack();
        frame.setVisible(true);
    }
}

class MySemaforo extends JPanel{

    private Light[] luzes;
    private Light selectedSquare = null;

    public MySemaforo(){
        setBorder(BorderFactory.createLineBorder(Color.black));

        luzes = new Light[3];
        luzes[0] = new Light(20, 20, 20, Color.GREEN);
        luzes[1] = new Light(20, 60, 20, Color.YELLOW);
        luzes[2] = new Light(20, 100, 20, Color.RED);

        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e){
                for(Light luz: luzes){
                    if(luz.contains(e.getX(), e.getY())){
                        selectedSquare = luz;
                        repaint();
                        break;
                    }
                }
            }
        });
    }

    public Dimension getPreferredSize(){
        return new Dimension(300, 400);
    }

    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        for(Light luz: luzes){
            if(luz == selectedSquare)
                luz.draw(g, true);
            else
                luz.draw(g, false);
        }
              
    }

}

class Light {
    int x, y, raio;
    Color corBase;
    
    public Light(int x, int y, int raio, Color corBase){
        this.x = x;
        this.y = y;
        if (raio > 0) this.raio = raio;
        else this.raio = 20;
        this.corBase = corBase;
    }

    public boolean contains(int x, int y) {
    int cx = this.x + raio;
    int cy = this.y + raio;
    int dx = x - cx;
    int dy = y - cy;
    return dx * dx + dy * dy <= raio * raio;
}
    public void draw(Graphics g, boolean isOn){
        if(isOn)
            g.setColor(corBase);
        else
            g.setColor(corBase.darker());
        g.fillOval(this.x, this.y, this.raio*2, this.raio*2);
        g.setColor(Color.BLACK);
        g.drawOval(this.x, this.y, this.raio*2, this.raio*2);
    }
}
