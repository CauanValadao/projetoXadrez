package estudo.parte2;

import javax.swing.SwingUtilities;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.BorderFactory;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
//import java.awt.event.MouseListener;
import java.awt.event.MouseAdapter;
//import java.awt.event.MouseMotionListener;
import java.awt.event.MouseMotionAdapter;

public class SwingPaintDemo3 {
    
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
        f.add(new MyPanel1());
        f.pack();
        f.setVisible(true);
    } 
}

class MyPanel1 extends JPanel {

    private boolean dragging = false;

    Square[] cubos;
    Square selectedSquare = null;

    public MyPanel1() {

        setBorder(BorderFactory.createLineBorder(Color.black));

        cubos = new Square[2];
        cubos[0] = new Square(50, 50, 20, 20);
        cubos[1] = new Square(100, 100, 20, 20);

        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                for(Square s: cubos){
                    if(s.contains(e.getX(),e.getY())){
                        dragging = true;
                        selectedSquare = s;
                        break;
                    }
                }
            }
             public void mouseReleased(MouseEvent e) {
                dragging = false;
                selectedSquare = null;
            }
        });
     
        addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                if (dragging && selectedSquare != null) {
                    selectedSquare.moveTo(e.getX(), e.getY());
                    repaint();
                }
            }
        });
      
    }

    

    public Dimension getPreferredSize() {
        return new Dimension(250,200);
    }
    
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);       
        g.drawString("This is my custom Panel!",10,20);
        pintarAzul(g, cubos[1]);
        pintarVermelho(g, cubos[0]);

        
    }  

    private void pintarVermelho(Graphics g, Square cubo){
        g.setColor(Color.RED);
        g.fillRect(cubo.x, cubo.y, cubo.width, cubo.height);
        g.setColor(Color.BLACK);
        g.drawRect(cubo.x, cubo.y, cubo.width, cubo.height);
    }

    private void pintarAzul(Graphics g, Square cubo){
        g.setColor(Color.BLUE);
        g.fillRect(cubo.x, cubo.y, cubo.width, cubo.height);
        g.setColor(Color.BLACK);
        g.drawRect(cubo.x, cubo.y, cubo.width, cubo.height);
    }
}

class Square {
    int x, y, width, height;

    public Square(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public boolean contains(int px, int py) {
        return (px >= x && px <= x + width && py >= y && py <= y + height);
    }

    public void moveTo(int px, int py) {
        this.x = px;
        this.y = py;
    }
}
