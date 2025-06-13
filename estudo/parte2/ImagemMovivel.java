package estudo.parte2;

import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;


public class ImagemMovivel {
    
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
        
        /*JPanel painel = new JPanel();

        URL urlDaImagem = Imagempeca.class.getResource("images/chess.png");

        if (urlDaImagem == null) {
            System.err.println("Erro: Não foi possível encontrar a imagem.");
            System.err.println("Verifique se a pasta 'images' está no mesmo local que o arquivo .java");
            painel.add(new JLabel("Erro ao carregar imagem!"));
        } else {
            ImageIcon iconePeca = new ImageIcon(urlDaImagem);
            JLabel labelDaPeca = new JLabel(iconePeca);
            painel.add(labelDaPeca);
        }*/

        

        frame.add(new PainelDeDesenho());
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

}

    class PainelDeDesenho extends JPanel{
        int offsetX;
        int offsetY;

        private ImagemM imagem;
        private ImagemM pecaSelecionada = null;

        public PainelDeDesenho(){
            setBorder(BorderFactory.createLineBorder(Color.BLACK));
            
            URL urlDaImagem = ImagemMovivel.class.getResource("images/chess.png");
            if(urlDaImagem == null){
                System.out.println("Erro: Não foi possível encontrar a imagem.");
                this.add(new JLabel("Erro ao carregar imagem!"));
            }
            else{
                /*ImageIcon imagemIcon = new ImageIcon(urlDaImagem);
                imagem = new ImagemM(imagemIcon.getImage(), 50, 50, 64, 64);*/
                try {
                    Image imagemOriginal = ImageIO.read(urlDaImagem);
                    imagem = new ImagemM(imagemOriginal, 50, 50, 64, 64);

                } catch (IOException e) {
                    e.printStackTrace();
                    this.add(new JLabel("Erro ao ler o arquivo de imagem!"));
            }
        }

            MouseAdapter mouseAdapter = new MouseAdapter() {

                @Override
                public void mousePressed(MouseEvent e){
                    if(imagem != null && imagem.contains(e.getX(), e.getY())){
                        pecaSelecionada = imagem;
                        offsetX = e.getX() - imagem.x;
                        offsetY = e.getY() - imagem.y;
                    }
                }

                @Override
                public void mouseReleased(MouseEvent e){
                    pecaSelecionada = null;
                }

                @Override
                public void mouseDragged(MouseEvent e){
                    if(pecaSelecionada != null){
                        imagem.moverImagem(e.getX() - offsetX, e.getY() - offsetY);
                        repaint();
                    }
                    
                }
                
            };
            addMouseListener(mouseAdapter);
            addMouseMotionListener(mouseAdapter);

            setPreferredSize(new Dimension(500, 500));
        }
        @Override
            protected void paintComponent(Graphics g){
                super.paintComponent(g);
                if(imagem != null)
                    imagem.desenhar(g);
            }
    }

    class ImagemM{
        Image imagem;
        int x, y;
        int largura, altura;

        public ImagemM(Image imagemOriginal, int x, int y, int larguraDesejada, int alturaDesejada){
            this.x = x;
            this.y = y;
            this.largura = larguraDesejada;
            this.altura = alturaDesejada;
            this.imagem = imagemOriginal.getScaledInstance(this.largura, this.altura, Image.SCALE_SMOOTH);
        }

        public void moverImagem(int x, int y){
            this.x = x;
            this.y = y;
        }

        public void desenhar(Graphics g){
            g.drawImage(imagem, x, y, null);
        }

        public boolean contains(int pontoX, int pontoY){
            return (pontoX >= x && pontoX <= x + largura &&
                    pontoY >= y && pontoY <= y + altura);
        }
    } 



