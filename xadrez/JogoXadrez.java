package xadrez;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

public class JogoXadrez {
    
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

        frame.add(new PainelTabuleiro());
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}

class PainelTabuleiro extends JPanel{

}

class ControladorDeJogo{
    private Casa[][] tabuleiro;
    private PainelTabuleiro painel;
    private Color vezDoJogador;
    private Casa casaSelecionada = null;
    private final int lado;
    private Map<String, Image> imagens;

    public ControladorDeJogo(PainelTabuleiro painel, int lado){
        this.lado = lado;
        this.painel = painel;

        tabuleiro = new Casa[8][8];

        carregaImagens();

        for(int i = 2; i < 6; i++){
            for(int j = 0; j < 8; j++){
                if((i + j) % 2 == 0) tabuleiro[i][j] = new Casa(Color.WHITE, j, i, lado, null);
                else tabuleiro[i][j] = new Casa(Color.BLACK, j, i, lado, null);
                
            }
        }
        //agora eu devo adicionar o resto das pecas
        //para isso devo criar pelo menos a base dos outros tipos de pecas
        

        

    }

    private void carregaImagens(){
        String[] nomes = {"p_rei", "p_rainha", "p_cavalo", 
                          "p_torre", "p_bispo", "p_peao",
                          "b_rei", "b_rainha", "b_cavalo",
                          "b_torre", "b_bispo", "b_peao"};
        this.imagens = new HashMap<>();
        URL urlDaImagem;

        for(int i = 0; i < nomes.length; i++){
            urlDaImagem = JogoXadrez.class.getResource("images/" + nomes[i] + ".png");
            if(urlDaImagem == null){
                System.out.println("Erro: Não foi possível encontrar a imagem.");
                this.painel.add(new JLabel("Erro ao carregar imagem!"));
            }
            else{
                try {
                    Image imagemOriginal = ImageIO.read(urlDaImagem);
                    imagens.put(nomes[i].toUpperCase(), imagemOriginal);

                } catch (IOException e) {
                        throw new RuntimeException("ERRO CRÍTICO: Falha ao carregar imagem. Verifique se os arquivos estão corretos.", e);
            }
        }
        }
    }
}

class Casa{
    private final Color cor;
    protected Peca peca = null;
    private final int linha, coluna, lado;

    public Casa(Color cor, int colunaCasa, int linhaCasa, int lado, Peca pecaCasa){
        this.cor = cor;
        this.peca = pecaCasa;
        this.linha = linhaCasa;
        this.coluna = colunaCasa;
        this.lado = lado;
    }

    public void desenhaCasa(Graphics g){
        if(cor == Color.BLACK) g.setColor(Color.LIGHT_GRAY);
        else g.setColor(Color.WHITE);
        g.fillRect(this.coluna*this.lado, this.linha*this.lado, this.lado, this.lado);
        g.setColor(Color.BLACK);
        g.drawRect(this.coluna*this.lado, this.linha*this.lado, this.lado, this.lado);
    }

}

abstract class Peca{
    protected Image imagem;
    protected final Color cor;
    protected int linha, coluna, lado;

    public Peca(Image imagemPeca, Color corPeca, int linhaPeca, int colunaPeca, int altura, int largura, int lado){
        this.cor = corPeca;
        this.linha = linhaPeca;
        this.coluna = colunaPeca;
        this.lado = lado;
        this.imagem = imagemPeca.getScaledInstance(largura, altura, Image.SCALE_SMOOTH);
    }

    public abstract boolean podeIr(int linha, int coluna);

    public void moverPara(int linha, int coluna){
        this.linha = linha;
        this.coluna = coluna;
    }

    public void desenhaPeca(Graphics g){
        g.drawImage(imagem, coluna*lado, linha*lado, null);
    }

    
}

class Rei extends Peca{
    public Rei(Image imagemPeca, Color corPeca, int linhaPeca, int colunaPeca, int altura, int largura, int lado){
        super(imagemPeca, corPeca, linhaPeca, colunaPeca, altura, largura, lado);
    }
    
    @Override
    public boolean podeIr(int linha, int coluna){
        if(Math.abs(coluna - this.coluna) <=1 && Math.abs(linha - this.linha) <=1 && (linha != this.linha || coluna != this.coluna)) return true;
        return false;
    }
}
