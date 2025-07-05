package xadrez;

import java.awt.Color;
import java.awt.Graphics;

public class Casa {
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
