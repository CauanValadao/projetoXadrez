package xadrez;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

public abstract class Peca {
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
