package estudo.parte2;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.net.URL;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

public class TabuleiroPeca {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
    private static void createAndShowGUI(){
        System.out.println("Created GUI on EDT? " + SwingUtilities.isEventDispatchThread());
        JFrame frame = new JFrame("TabuleiroPeca");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.add(new PainelTabuleiro1());
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}

class PainelTabuleiro1 extends JPanel{
    private boolean clique = false;

    Casa[][] tabuleiro;
    Casa casaSelecionada = null;

    public PainelTabuleiro1(){
        setBorder(BorderFactory.createLineBorder(Color.BLACK));

        tabuleiro = new Casa[8][8];

        int decida = 0;
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                if((i + j) % 2 == 0) decida = 0;
                else decida = 1;
                tabuleiro[i][j] = new Casa(decida, null, j, i, 64);
            }
        }
        //pegando a imagem
        URL urlDaImagem = TabuleiroPeca.class.getResource("images/b_rei.png");
            if(urlDaImagem == null){
                System.out.println("Erro: Não foi possível encontrar a imagem.");
                this.add(new JLabel("Erro ao carregar imagem!"));
            }
            else{
                try {
                    Image imagemOriginal = ImageIO.read(urlDaImagem);
                    tabuleiro[1][3].peca = new Rei(imagemOriginal, 3, 1, 64, 64);

                } catch (IOException e) {
                    e.printStackTrace();
                    this.add(new JLabel("Erro ao ler o arquivo de imagem!"));
            }
        }
        //imagem pega e posta na peca.
        //logica de clicar
        MouseAdapter mouseAdapter = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e){
                for(int i = 0; i < 8; i++){
                    for(int j = 0; j < 8; j++){
                        if(tabuleiro[i][j].contem(e.getX(), e.getY())){
                            if(!clique && tabuleiro[i][j].peca != null){
                                clique = true;
                                casaSelecionada = tabuleiro[i][j];
                            }
                            else if(clique && casaSelecionada != null){
                                if(casaSelecionada.peca.podeIr(j, i)){
                                    casaSelecionada.peca.moverPara(j, i);
                                    Peca temp = tabuleiro[i][j].peca;
                                    tabuleiro[i][j].peca = casaSelecionada.peca;
                                    casaSelecionada.peca = temp;
                                }
                                casaSelecionada = null;
                                clique = false;
                            }
                            repaint();
                        }
                    }
                }
            }
        };

        addMouseListener(mouseAdapter);
        setPreferredSize(new Dimension(8*64, 8*64));

    }
    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                tabuleiro[i][j].desenhaCasa(g);
                if(tabuleiro[i][j].peca != null)
                    tabuleiro[i][j].peca.desenhaPeca(g);
            }
        }
        if(casaSelecionada != null){
            g.setColor(Color.RED);
            g.drawRect(casaSelecionada.x * 64, casaSelecionada.y * 64, 64, 64);
            for(int i = 0; i < 8; i++){
                for(int j = 0; j < 8; j++){
                    if(casaSelecionada.peca.podeIr(j, i)){
                        g.setColor(Color.BLACK);
                        g.fillOval(j*64 + 24, i*64 + 24, 16, 16);
                        g.setColor(Color.RED);
                        g.drawOval(j*64 + 24, i*64 + 24, 16, 16);
                    }
                }
            }
        }

    }
}


class Casa{
    int cor;
    Peca peca = null;
    int x, y, lado;


    public Casa(int cor, Peca peca, int x, int y, int lado){
        this.cor = cor;
        this.peca = peca;
        this.x = x;
        this.y = y;
        this.lado = lado;
    }

    public boolean contem(int pontoX, int pontoY){
        if(pontoX < this.x*lado || pontoX > (this.x+1)*lado || pontoY < this.y*lado || pontoY > (this.y+1)*lado) return false;
        return true;
    }

    public void desenhaCasa(Graphics g){
        if(cor == 0) g.setColor(Color.LIGHT_GRAY);
        else g.setColor(Color.WHITE);
        g.fillRect(x*lado, y*lado, lado, lado);
        g.setColor(Color.BLACK);
        g.drawRect(x*lado, y*lado, lado, lado);
    }
}



abstract class Peca{
    protected int x, y, largura, altura;
    protected Image imagem;

    public Peca(Image imagemOriginal, int x, int y, int largura, int altura){
        this.x = x;
        this.y = y;
        this.largura = largura;
        this.altura = altura;
        this.imagem = imagemOriginal.getScaledInstance(this.largura, this.altura, Image.SCALE_SMOOTH);
    }

    public abstract boolean podeIr(int x, int y);
    
    public void moverPara(int x, int y){
        this.x = x;
        this.y = y;
    }

    public void desenhaPeca(Graphics g){
        g.drawImage(imagem, x*64, y*64, null);
    }

}

class Rei extends Peca{
    public Rei(Image imagemPeca, int x, int y, int largura, int altura){
        super(imagemPeca, x, y, largura, altura);
    }
    
    @Override
    public boolean podeIr(int x, int y){
        if(Math.abs(x - this.x) <= 1 && Math.abs(y - this.y) <= 1 && (x != this.x || y !=this.y)) return true;
        return false;
    }
}


