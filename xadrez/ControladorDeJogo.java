package xadrez;

import javax.imageio.ImageIO;
import javax.swing.JLabel;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.awt.Color;
import java.awt.Image;
import java.io.IOException;

public class ControladorDeJogo {
    private Casa[][] tabuleiro;
    private PainelTabuleiro painel;
    private Color vezDoJogador;
    private Casa casaSelecionada = null;
    private final int lado;
    private Map<String, Image> imagens;
    private Image imagemErro;
    private List<Casa> movimentosPossiveis = new ArrayList<>();
    private List<Peca> pecasBrancas = new ArrayList<>();
    private List<Peca> pecasPretas = new ArrayList<>();
    private Peao acabouDeAndarDuas = null;
    private Color resultado = null;
    private boolean promocao = false;
    private int promocaoColuna;
    private int promocaoLinha;

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
            tabuleiro[6][j].peca = new Peao(getImagem("B_PEAO"), Color.WHITE, 6, j, this.lado, this.lado, this.lado);
            tabuleiro[1][j].peca = new Peao(getImagem("P_PEAO"), Color.BLACK, 1, j, this.lado, this.lado, this.lado);

            switch (j){
                case 0:
                case 7:
                    tabuleiro[7][j].peca = new Torre(getImagem("B_TORRE"), Color.WHITE, 7, j, this.lado, this.lado, this.lado);
                    tabuleiro[0][j].peca = new Torre(getImagem("P_TORRE"), Color.BLACK, 0, j, this.lado, this.lado, this.lado);
                    break;
                case 1:
                case 6:
                    tabuleiro[7][j].peca = new Cavalo(getImagem("B_CAVALO"), Color.WHITE, 7, j, this.lado, this.lado, this.lado);
                    tabuleiro[0][j].peca = new Cavalo(getImagem("P_CAVALO"), Color.BLACK, 0, j, this.lado, this.lado, this.lado);
                    break;
                case 2:
                case 5:
                    tabuleiro[7][j].peca = new Bispo(getImagem("B_BISPO"), Color.WHITE, 7, j, this.lado, this.lado, this.lado);
                    tabuleiro[0][j].peca = new Bispo(getImagem("P_BISPO"), Color.BLACK, 0, j, this.lado, this.lado, this.lado);
                    break;
                case 3:
                    tabuleiro[7][j].peca = new Rainha(getImagem("B_RAINHA"), Color.WHITE, 7, j, this.lado, this.lado, this.lado);
                    tabuleiro[0][j].peca = new Rainha(getImagem("P_RAINHA"), Color.BLACK, 0, j, this.lado, this.lado, this.lado);
                    break;
                case 4:
                    tabuleiro[7][j].peca = new Rei(getImagem("B_REI"), Color.WHITE, 7, j, this.lado, this.lado, this.lado);
                    tabuleiro[0][j].peca = new Rei(getImagem("P_REI"), Color.BLACK, 0, j, this.lado, this.lado, this.lado);
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

        urlDaImagem = JogoXadrez.class.getResource("images/" + "erro.png");
        if(urlDaImagem == null){
            System.out.println("Erro: Não foi possível encontrar a imagem.");
            this.painel.add(new JLabel("Erro ao carregar imagem!"));
        }
        else{
                try {
                    imagemErro = ImageIO.read(urlDaImagem);

                } catch (IOException e) {
                        throw new RuntimeException("ERRO CRÍTICO: Falha ao carregar imagem. Verifique se os arquivos estão corretos.", e);
            }
        }
    }

    public void gerenciarClique(int linha, int coluna){
        if(this.resultado == null){
            if(this.promocao){
                if(coluna == this.promocaoColuna && (promocaoLinha == 4 && linha > 3 || promocaoLinha == 0 && linha < 4)){
                    int pecaEscolhida = Math.abs(promocaoLinha - linha);
                    if(promocaoLinha == 4) this.promocaoLinha = 7;

                    switch (pecaEscolhida) {
                        case 0:
                            if(vezDoJogador == Color.BLACK) tabuleiro[this.promocaoLinha][this.promocaoColuna].peca = new Rainha(getImagem("B_RAINHA"), Color.WHITE, this.promocaoLinha, this.promocaoColuna, this.lado, this.lado, this.lado);
                            else tabuleiro[this.promocaoLinha][this.promocaoColuna].peca = new Rainha(getImagem("P_RAINHA"), Color.BLACK, this.promocaoLinha, this.promocaoColuna, this.lado, this.lado, this.lado);
                            break;
                        case 1:
                            if(vezDoJogador == Color.BLACK) tabuleiro[this.promocaoLinha][this.promocaoColuna].peca = new Cavalo(getImagem("B_CAVALO"), Color.WHITE, this.promocaoLinha, this.promocaoColuna, this.lado, this.lado, this.lado);
                            else tabuleiro[this.promocaoLinha][this.promocaoColuna].peca = new Cavalo(getImagem("P_CAVALO"), Color.BLACK, this.promocaoLinha, this.promocaoColuna, this.lado, this.lado, this.lado);
                            break;
                        case 2:
                            if(vezDoJogador == Color.BLACK) tabuleiro[this.promocaoLinha][this.promocaoColuna].peca = new Torre(getImagem("B_TORRE"), Color.WHITE, this.promocaoLinha, this.promocaoColuna, this.lado, this.lado, this.lado);
                            else tabuleiro[this.promocaoLinha][this.promocaoColuna].peca = new Torre(getImagem("P_TORRE"), Color.BLACK, this.promocaoLinha, this.promocaoColuna, this.lado, this.lado, this.lado);
                            break;
                        case 3:
                            if(vezDoJogador == Color.BLACK) tabuleiro[this.promocaoLinha][this.promocaoColuna].peca = new Bispo(getImagem("B_BISPO"), Color.WHITE, this.promocaoLinha, this.promocaoColuna, this.lado, this.lado, this.lado);
                            else tabuleiro[this.promocaoLinha][this.promocaoColuna].peca = new Bispo(getImagem("P_BISPO"), Color.BLACK, this.promocaoLinha, this.promocaoColuna, this.lado, this.lado, this.lado);
                        default:
                            break;
                    }
                    this.promocao = false;
                }
            }
            else{
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

                        if(casaSelecionada.peca instanceof Peao && (linha == 7 || linha == 0)){
                            this.promocao = true; 
                            if(linha == 7) this.promocaoLinha = 4;
                            else this.promocaoLinha = 0;
                            this.promocaoColuna = coluna;
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

    public boolean getPromocao(){
        return this.promocao;
    }

    public int getPromocaoLinha(){
        return this.promocaoLinha;
    }

    public int getPromocaoColuna(){
        return this.promocaoColuna;
    }

    public Image getImagem(String peca){
        if(imagens.containsKey(peca.toUpperCase())) return imagens.get(peca);
        else return this.imagemErro;
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
