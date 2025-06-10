package estudo.parte1;

public class Bispo extends PecaXadrez {
    public Bispo(Cor cor){
        super(cor);
    }

     @Override
    public String mover(){
        return "Bispo " + cor + " se move em diagonal";
    }

     @Override
    public String toString(){
        return "Bispo " + cor;
    }
}
