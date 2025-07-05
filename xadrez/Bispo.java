package xadrez;

import java.awt.Color;
import java.awt.Image;

public class Bispo extends Peca{
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
