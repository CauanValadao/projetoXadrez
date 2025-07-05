package xadrez;

import java.awt.Color;
import java.awt.Image;

public class Rei extends Peca{
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
