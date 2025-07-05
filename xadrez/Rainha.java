package xadrez;

import java.awt.Color;
import java.awt.Image;

public class Rainha extends Peca{
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
