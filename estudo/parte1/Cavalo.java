package estudo.parte1;

public class Cavalo extends PecaXadrez implements Especial{
    
    public Cavalo(Cor cor){
        super(cor);
    }

    @Override
    public String mover() {
        return "Cavalo " + cor + " se move em 'L'";
    }

    @Override
    public String toString(){
        return "Cavalo " + cor;
    }

    @Override
    public String habilidadeEspecial(){
        return "Cavalo pode pular outras peças.";
    }

}
