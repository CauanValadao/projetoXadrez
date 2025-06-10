package estudo.parte1;

public abstract class PecaXadrez {
    protected Cor cor;

    public PecaXadrez(Cor cor) {
        this.cor = cor;
    }

    public abstract String mover();
}
