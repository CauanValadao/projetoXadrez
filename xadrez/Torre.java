package xadrez;

import java.awt.Color;
import java.awt.Image;

public class Torre extends Peca{
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
