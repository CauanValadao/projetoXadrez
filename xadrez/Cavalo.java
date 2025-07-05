package xadrez;

import java.awt.Color;
import java.awt.Image;

public class Cavalo extends Peca{
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
