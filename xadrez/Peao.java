package xadrez;

import java.awt.Color;
import java.awt.Image;

public class Peao extends Peca{
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
