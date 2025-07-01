package xadrez;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

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

class PainelTabuleiro extends JPanel{

    private ControladorDeJogo controlador;
    private JButton botaoReiniciar;

    public PainelTabuleiro(){
        setBorder(BorderFactory.createLineBorder(Color.BLACK));

        this.setLayout(null); 

        MouseAdapter mouseAdapter = new MouseAdapter(){
            

            @Override
            public void mousePressed(MouseEvent e){
               int lado = controlador.getLado();
               int coluna = e.getX() / lado;
               int linha = e.getY() / lado;
               controlador.gerenciarClique(linha, coluna);
            }
            
        };

        addMouseListener(mouseAdapter);

        this.botaoReiniciar = new JButton("Reiniciar Jogo");
        this.botaoReiniciar.setFocusable(false);
        this.botaoReiniciar.setVisible(false);
        this.botaoReiniciar.addActionListener(e -> {
            controlador.reiniciarJogo();
            repaint(); 
        });
        this.add(botaoReiniciar);

    }

    public void controlaBotao(boolean mostrar){
        this.botaoReiniciar.setVisible(mostrar);
    }

    public void setControlador(ControladorDeJogo controlador) {
        this.controlador = controlador;
        int lado = controlador.getLado();
        setPreferredSize(new Dimension(8 * lado, 8 * lado));

        int larguraBotao = 200;
        int alturaBotao = 50;
        int xBotao = (8 * lado - larguraBotao) / 2;
        int yBotao = (8 * lado - alturaBotao) / 2;
        this.botaoReiniciar.setBounds(xBotao, yBotao, larguraBotao, alturaBotao);
    }

    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g); // Limpa a tela (essencial!)

    // Adicionamos uma verificação de segurança: só desenha se o controlador já existir.
    if (this.controlador != null) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Casa casa = controlador.getCasa(i, j);
                if (casa != null) {
                    casa.desenhaCasa(g);
                    if (casa.peca != null)
                        casa.peca.desenhaPeca(g);
                }
            }
        }
        for(Casa casa : controlador.getMovimentosPossiveis()){
            g.setColor(Color.BLACK);
            g.fillOval(casa.getColuna()*controlador.getLado() + (controlador.getLado() - 16)/2, casa.getLinha()*controlador.getLado() + (controlador.getLado() - 16)/2, 16, 16);
            g.setColor(Color.RED);
            g.drawOval(casa.getColuna()*controlador.getLado() + (controlador.getLado() - 16)/2, casa.getLinha()*controlador.getLado() + (controlador.getLado() - 16)/2, 16, 16);
        }

        if(controlador.getResultado() != null){
                g.setColor(new Color(0, 0, 0, 150));
                g.fillRect(0, 0, getWidth(), getHeight());

                Font fonteResultado = new Font("Arial", Font.BOLD, 32);
                g.setFont(fonteResultado);
                g.setColor(Color.WHITE);

                String mensagem;
                if (controlador.getResultado() == Color.CYAN) {
                    mensagem = "Empate!";
                } 
                else {
                    String vencedor = (controlador.getResultado() == Color.WHITE) ? "Brancas" : "Pretas";
                    mensagem = "Xeque-mate! " + vencedor + " venceram!";
                }
                
                int larguraTextoResultado = g.getFontMetrics().stringWidth(mensagem);
                g.drawString(mensagem, (getWidth() - larguraTextoResultado) / 2, botaoReiniciar.getY() - 30);
        }
    }

    }

}

class ControladorDeJogo{
    private Casa[][] tabuleiro;
    private PainelTabuleiro painel;
    private Color vezDoJogador;
    private Casa casaSelecionada = null;
    private final int lado;
    private Map<String, Image> imagens;
    private List<Casa> movimentosPossiveis = new ArrayList<>();
    private List<Peca> pecasBrancas = new ArrayList<>();
    private List<Peca> pecasPretas = new ArrayList<>();
    private Peao acabouDeAndarDuas = null;
    private Color resultado = null;

    public ControladorDeJogo(PainelTabuleiro painel, int lado){
        this.lado = lado;
        this.painel = painel;

        this.vezDoJogador = Color.WHITE;

        tabuleiro = new Casa[8][8];

        carregaImagens();

        posicionaPecas();

        for(int j = 0; j < 8; j++){
            pecasBrancas.add(tabuleiro[7][j].peca);
            pecasBrancas.add(tabuleiro[6][j].peca);
            pecasPretas.add(tabuleiro[0][j].peca);
            pecasPretas.add(tabuleiro[1][j].peca);
        }
    }
    
    private void posicionaPecas(){
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                Color corCasa = ((i + j) % 2 == 0) ? Color.WHITE : Color.BLACK;
                tabuleiro[i][j] = new Casa(corCasa, i, j, this.lado, null);      
            }
        }

        for(int j = 0; j < 8; j++){
            tabuleiro[6][j].peca = new Peao(imagens.get("B_PEAO"), Color.WHITE, 6, j, this.lado, this.lado, this.lado);
            tabuleiro[1][j].peca = new Peao(imagens.get("P_PEAO"), Color.BLACK, 1, j, this.lado, this.lado, this.lado);

            switch (j){
                case 0:
                case 7:
                    tabuleiro[7][j].peca = new Torre(imagens.get("B_TORRE"), Color.WHITE, 7, j, this.lado, this.lado, this.lado);
                    tabuleiro[0][j].peca = new Torre(imagens.get("P_TORRE"), Color.BLACK, 0, j, this.lado, this.lado, this.lado);
                    break;
                case 1:
                case 6:
                    tabuleiro[7][j].peca = new Cavalo(imagens.get("B_CAVALO"), Color.WHITE, 7, j, this.lado, this.lado, this.lado);
                    tabuleiro[0][j].peca = new Cavalo(imagens.get("P_CAVALO"), Color.BLACK, 0, j, this.lado, this.lado, this.lado);
                    break;
                case 2:
                case 5:
                    tabuleiro[7][j].peca = new Bispo(imagens.get("B_BISPO"), Color.WHITE, 7, j, this.lado, this.lado, this.lado);
                    tabuleiro[0][j].peca = new Bispo(imagens.get("P_BISPO"), Color.BLACK, 0, j, this.lado, this.lado, this.lado);
                    break;
                case 3:
                    tabuleiro[7][j].peca = new Rainha(imagens.get("B_RAINHA"), Color.WHITE, 7, j, this.lado, this.lado, this.lado);
                    tabuleiro[0][j].peca = new Rainha(imagens.get("P_RAINHA"), Color.BLACK, 0, j, this.lado, this.lado, this.lado);
                    break;
                case 4:
                    tabuleiro[7][j].peca = new Rei(imagens.get("B_REI"), Color.WHITE, 7, j, this.lado, this.lado, this.lado);
                    tabuleiro[0][j].peca = new Rei(imagens.get("P_REI"), Color.BLACK, 0, j, this.lado, this.lado, this.lado);
                    break;
                default:
                    break;
            }
        }

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

    public void gerenciarClique(int linha, int coluna){
        if(this.resultado == null){
            if(casaSelecionada != null){
                if(movimentosPossiveis.contains(tabuleiro[linha][coluna])){

                    if(tabuleiro[linha][coluna].peca != null){
                        if(vezDoJogador == Color.WHITE) pecasPretas.remove(tabuleiro[linha][coluna].peca);
                        else pecasBrancas.remove(tabuleiro[linha][coluna].peca);
                    }

                    if(casaSelecionada.peca instanceof Rei && linha == casaSelecionada.getLinha() && Math.abs(coluna - casaSelecionada.getColuna()) == 2){
                        if(coluna == 6){
                            tabuleiro[linha][7].peca.moverPara(linha, 5);
                            tabuleiro[linha][5].peca = tabuleiro[linha][7].peca;
                            tabuleiro[linha][7].peca = null;
                        }
                        else{
                            tabuleiro[linha][0].peca.moverPara(linha, 3);
                            tabuleiro[linha][3].peca = tabuleiro[linha][0].peca;
                            tabuleiro[linha][0].peca = null;
                        }
                    }

                    if(casaSelecionada.peca instanceof Peao && coluna != casaSelecionada.getColuna() && tabuleiro[linha][coluna].peca == null){
                        tabuleiro[acabouDeAndarDuas.linha][acabouDeAndarDuas.coluna].peca = null;
                    }
                    
                    this.acabouDeAndarDuas = null;

                    if(casaSelecionada.peca instanceof Peao && Math.abs(linha - casaSelecionada.getLinha()) == 2){
                        this.acabouDeAndarDuas = (Peao)casaSelecionada.peca;
                    }


                    casaSelecionada.peca.moverPara(linha, coluna);
                    tabuleiro[linha][coluna].peca = casaSelecionada.peca;
                    casaSelecionada.peca = null;


                    vezDoJogador = (vezDoJogador == Color.WHITE) ? Color.BLACK : Color.WHITE;
                }
                movimentosPossiveis.clear();
                casaSelecionada = null;

                monitoraXequeMate(vezDoJogador);
            }


            else if(tabuleiro[linha][coluna].peca != null && tabuleiro[linha][coluna].peca.cor == vezDoJogador){
            movimentosPossiveis.clear();
            casaSelecionada = tabuleiro[linha][coluna];
            
            movimentosPossiveis = geraMovimentosLegais(casaSelecionada.peca);

            movimentosPossiveis = filtraMovimentos(casaSelecionada, movimentosPossiveis);
        
        }
        }

        this.painel.repaint();
    }

    public void monitoraXequeMate(Color jogador){
        Color acabouDeJogar = (jogador == Color.WHITE) ? Color.BLACK : Color.WHITE;

        if(estaEmXeque(acabouDeJogar)){
             System.out.println("fim de jogo");
             resultado = acabouDeJogar;
            painel.controlaBotao(true);
        }

        List<Peca> pecasJogador = (jogador == Color.WHITE) ? pecasBrancas : pecasPretas;
        
        if(!existeMovimentoLegal(pecasJogador)){
            if(estaEmXeque(jogador)){
                 System.out.println("fim de jogo");
                 resultado = jogador;
            }
            else{
                 System.out.println("empate");
                 resultado = Color.CYAN;
            }
            painel.controlaBotao(true);
        }
    }

    public boolean existeMovimentoLegal(List<Peca> pecas){
        List<Casa> movimentoTeste = new ArrayList<>();
        for(Peca peca : pecas){
            movimentoTeste = geraMovimentosLegais(peca);
            movimentoTeste = filtraMovimentos(tabuleiro[peca.linha][peca.coluna], movimentoTeste);
            if(!movimentoTeste.isEmpty()) return true;
        }
        return false;
    }

    public List<Casa> geraMovimentosLegais(Peca peca){
        List<Casa> movimentosLegais = new ArrayList<>();
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                if(peca.podeIr(i, j, this)) movimentosLegais.add(tabuleiro[i][j]);
            }
        }
        return movimentosLegais;
    }

    public int getLado(){
        return this.lado;
    }

    public Casa getCasa(int linha, int coluna) {
    if (linha >= 0 && linha < 8 && coluna >= 0 && coluna < 8) {
        return this.tabuleiro[linha][coluna];
    }
    return null;
}

    public Casa[][] getTabuleiro(){
        return this.tabuleiro;
    }

    public List<Casa> getMovimentosPossiveis() {
        return this.movimentosPossiveis;
}

    public Color getResultado(){
        return this.resultado;
    }

    public boolean estaEmXeque(Color jogador){
        List<Peca> pecasInimigas = (jogador == Color.WHITE) ? pecasPretas : pecasBrancas;
        List<Peca> pecasAliadas = (jogador == Color.WHITE) ? pecasBrancas : pecasPretas;
        Peca rei = encontrarReiNaLista(pecasAliadas);

        for(Peca pecaInimiga : pecasInimigas)
            if(pecaInimiga.podeIr(rei.linha,rei.coluna, this)) return true;
        return false;
    }

    public Rei encontrarReiNaLista(List<Peca> listaDePecas){
        for(Peca peca : listaDePecas)
            if(peca instanceof Rei) return (Rei) peca;
        return null;
    }

    public boolean casaEstaEmAtaque(int linha, int coluna, Color jogador){
        List<Peca> pecasAtacantes = (jogador == Color.WHITE) ? pecasPretas : pecasBrancas;

        for(Peca pecaAtacante : pecasAtacantes)
            if(pecaAtacante.podeIr(linha, coluna, this)) return true;
        return false;
    }

    public Peao getAcabouDeAndarDuas(){
        return this.acabouDeAndarDuas;
    }
    
    public void setAcabouDeAndarDuas(Peao acabouDeAndarDuas){
        this.acabouDeAndarDuas = acabouDeAndarDuas;
    }

    public void reiniciarJogo(){
        
        casaSelecionada = null;
        acabouDeAndarDuas = null;
        resultado = null;
        movimentosPossiveis.clear();
        pecasBrancas.clear();
        pecasPretas.clear();
        vezDoJogador = Color.WHITE;
        
        posicionaPecas();

        for(int j = 0; j < 8; j++){
            pecasBrancas.add(tabuleiro[7][j].peca);
            pecasBrancas.add(tabuleiro[6][j].peca);
            pecasPretas.add(tabuleiro[0][j].peca);
            pecasPretas.add(tabuleiro[1][j].peca);
        }

        this.painel.controlaBotao(false);
        this.painel.repaint();
    }

    public List<Casa> filtraMovimentos(Casa casaOrigem, List<Casa> movimentosGerados){
        List<Casa> movimentosQueSalvam = new ArrayList<>();

        Peca pecaMovida = casaOrigem.peca;
        boolean primeiraJogadaOriginal;

        for(Casa casaDestino : movimentosGerados){

            Peca pecaCapturada = casaDestino.peca;

            casaOrigem.peca = null;
            casaDestino.peca = pecaMovida;
            primeiraJogadaOriginal = pecaMovida.getPrimeiraJogada();

            pecaMovida.moverPara(casaDestino.getLinha(), casaDestino.getColuna());

            if(pecaCapturada != null){
                if(vezDoJogador == Color.BLACK) pecasBrancas.remove(pecaCapturada);
                else pecasPretas.remove(pecaCapturada);
            }

            if(!estaEmXeque(vezDoJogador)) movimentosQueSalvam.add(casaDestino);
                    
            if(pecaCapturada != null){
                if(vezDoJogador == Color.BLACK) pecasBrancas.add(pecaCapturada);
                else pecasPretas.add(pecaCapturada);
            }

            casaOrigem.peca = pecaMovida;
            casaDestino.peca = pecaCapturada;
            pecaMovida.moverPara(casaOrigem.getLinha(), casaOrigem.getColuna());
            pecaMovida.setPrimeiraJogada(primeiraJogadaOriginal);
        }
        return movimentosQueSalvam;
    }
}

class Casa{
    private final Color cor;
    protected Peca peca = null;
    private final int linha, coluna, lado;

    public Casa(Color cor, int linhaCasa, int colunaCasa, int lado, Peca pecaCasa){
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

    public int getLinha(){
        return this.linha;
    }
    public int getColuna(){
        return this.coluna;
    }

}

abstract class Peca{
    protected Image imagem;
    protected final Color cor;
    protected int linha, coluna, lado;
    private boolean primeiraJogada = true;

    public Peca(Image imagemPeca, Color corPeca, int linhaPeca, int colunaPeca, int altura, int largura, int lado){
        this.cor = corPeca;
        this.linha = linhaPeca;
        this.coluna = colunaPeca;
        this.lado = lado;
        if(imagemPeca != null)
        this.imagem = imagemPeca.getScaledInstance(largura, altura, Image.SCALE_SMOOTH);
    }

    public abstract boolean podeIr(int linha, int coluna, ControladorDeJogo controlador);

    public void moverPara(int linha, int coluna){
        this.linha = linha;
        this.coluna = coluna;
        primeiraJogada = false;
    }

    public boolean getPrimeiraJogada(){
        return this.primeiraJogada;
    }

    public void setPrimeiraJogada(boolean primeiraJogada){
        this.primeiraJogada = primeiraJogada;
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
    public boolean podeIr(int linha, int coluna, ControladorDeJogo controlador){
        Casa[][] tabuleiro = controlador.getTabuleiro();
        if(tabuleiro[linha][coluna].peca != null && tabuleiro[linha][coluna].peca.cor == this.cor) return false;

        if(this.getPrimeiraJogada() && linha == this.linha && tabuleiro[linha][coluna].peca == null && Math.abs(this.coluna - coluna) == 2 && !controlador.casaEstaEmAtaque(linha, coluna, cor) && !controlador.estaEmXeque(cor)){
            if(this.coluna - coluna == 2){
                if(tabuleiro[linha][1].peca == null && !controlador.casaEstaEmAtaque(linha,1, cor) && tabuleiro[linha][2].peca == null && !controlador.casaEstaEmAtaque(linha, 2, cor) && tabuleiro[linha][3].peca == null && 
                !controlador.casaEstaEmAtaque(linha, 3, cor) && tabuleiro[linha][0].peca != null && tabuleiro[linha][0].peca instanceof Torre && tabuleiro[linha][0].peca.getPrimeiraJogada()) return true;
                return false;
            }
            else if(tabuleiro[linha][5].peca == null && !controlador.casaEstaEmAtaque(linha, 5, cor) && tabuleiro[linha][6].peca == null && !controlador.casaEstaEmAtaque(linha, 6, cor) && 
            tabuleiro[linha][7].peca != null && tabuleiro[linha][7].peca instanceof Torre && tabuleiro[linha][7].peca.getPrimeiraJogada()) return true;
            return false;
        }

        int deltaLinha = Math.abs(this.linha - linha);
        int deltaColuna = Math.abs(this.coluna - coluna);

        return (deltaLinha <= 1 && deltaColuna <= 1);
    }
}

class Peao extends Peca{
    private int direcao;
    public Peao(Image imagemPeca, Color corPeca, int linhaPeca, int colunaPeca, int altura, int largura, int lado){
        super(imagemPeca, corPeca, linhaPeca, colunaPeca, altura, largura, lado);
        if(this.cor == Color.BLACK) this.direcao = 1;
        else this.direcao = -1;
    }

    @Override
    public boolean podeIr(int linha, int coluna, ControladorDeJogo controlador){
        Casa[][] tabuleiro = controlador.getTabuleiro();

        if(tabuleiro[linha][coluna].peca != null && tabuleiro[linha][coluna].peca.cor == this.cor) return false;

        if(tabuleiro[linha][coluna].peca != null && Math.abs(this.coluna - coluna) == 1 && this.linha + this.direcao == linha) return true;

        if(tabuleiro[linha][coluna].peca == null && this.coluna == coluna && (this.linha + this.direcao == linha || (this.getPrimeiraJogada() && this.linha  + this.direcao*2 == linha && tabuleiro[this.linha + direcao][coluna].peca == null))) return true;
        
        int linhaP = (this.cor == Color.WHITE) ? 3 : 4;

        if(tabuleiro[linha][coluna].peca == null && linha == this.linha + this.direcao && this.linha == linhaP && Math.abs(this.coluna - coluna) == 1 && tabuleiro[this.linha][coluna].peca != null && tabuleiro[this.linha][coluna].peca instanceof Peao && ((Peao)tabuleiro[this.linha][coluna].peca) == controlador.getAcabouDeAndarDuas()) return true;

        return false;
    }
}

class Torre extends Peca{
    public Torre(Image imagemPeca, Color corPeca, int linhaPeca, int colunaPeca, int altura, int largura, int lado){
        super(imagemPeca, corPeca, linhaPeca, colunaPeca, altura, largura, lado);
    }

    @Override
    public boolean podeIr(int linha, int coluna, ControladorDeJogo controlador){
        Casa[][] tabuleiro = controlador.getTabuleiro();

        if(tabuleiro[linha][coluna].peca != null && tabuleiro[linha][coluna].peca.cor == this.cor) return false;
        int direcao;

        if(linha == this.linha){
            if(coluna > this.coluna) direcao = 1;
            else direcao = -1;
            
            for(int i = this.coluna + direcao; i != coluna; i += direcao)
                if(tabuleiro[linha][i].peca != null) return false;
            
            return true;
        }

        else if(coluna == this.coluna){
            if(linha > this.linha) direcao = 1;
            else direcao = -1;

            for(int i = this.linha + direcao; i != linha; i += direcao)
                if(tabuleiro[i][coluna].peca != null) return false;
            
            return true;
        }

        return false;
    }
}

class Cavalo extends Peca{
    public Cavalo(Image imagemPeca, Color corPeca, int linhaPeca, int colunaPeca, int altura, int largura, int lado){
        super(imagemPeca, corPeca, linhaPeca, colunaPeca, altura, largura, lado);
    }

    @Override
    public boolean podeIr(int linha, int coluna, ControladorDeJogo controlador){
        Casa[][] tabuleiro = controlador.getTabuleiro();

        if(tabuleiro[linha][coluna].peca != null && tabuleiro[linha][coluna].peca.cor == this.cor) return false;

        int deltaLinha = Math.abs(this.linha - linha);
        int deltaColuna = Math.abs(this.coluna - coluna);

        return (deltaLinha == 2 && deltaColuna == 1) || (deltaLinha == 1 && deltaColuna == 2);
    }
}

class Bispo extends Peca{
    public Bispo(Image imagemPeca, Color corPeca, int linhaPeca, int colunaPeca, int altura, int largura, int lado){
        super(imagemPeca, corPeca, linhaPeca, colunaPeca, altura, largura, lado);
    }

    public boolean podeIr(int linha, int coluna, ControladorDeJogo controlador){
        Casa[][] tabuleiro = controlador.getTabuleiro();

        if(tabuleiro[linha][coluna].peca != null && tabuleiro[linha][coluna].peca.cor == this.cor || Math.abs(this.linha - linha) != Math.abs(this.coluna - coluna)) return false;

        int caminhoLinha;
        int caminhoColuna;

        if(linha > this.linha) caminhoLinha = 1;
        else caminhoLinha = -1;
        if(coluna > this.coluna) caminhoColuna = 1;
        else caminhoColuna = -1;

        for(int i = this.linha + caminhoLinha, j = this.coluna + caminhoColuna; i != linha && j != coluna; i+=caminhoLinha, j+=caminhoColuna)
            if(tabuleiro[i][j].peca != null) return false;
        
        return true;
    }
}

class Rainha extends Peca{
    public Rainha(Image imagemPeca, Color corPeca, int linhaPeca, int colunaPeca, int altura, int largura, int lado){
        super(imagemPeca, corPeca, linhaPeca, colunaPeca, altura, largura, lado);
    }

    public boolean podeIr(int linha, int coluna, ControladorDeJogo controlador){
        Casa[][] tabuleiro = controlador.getTabuleiro();

        if(tabuleiro[linha][coluna].peca != null && tabuleiro[linha][coluna].peca.cor == this.cor) return false;

        Torre torreVirtual = new Torre(null, this.cor, this.linha, this.coluna, 0, 0, this.lado);
        Bispo bispoVirtual = new Bispo(null, this.cor, this.linha, this.coluna, 0, 0, this.lado);

        return (torreVirtual.podeIr(linha, coluna, controlador) || bispoVirtual.podeIr(linha, coluna, controlador));
    }
}

/*
 *  OBJETIVOS:
 *      5.promocao de peça
 */