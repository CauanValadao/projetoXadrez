package estudo.parte1;

public class MainXadrez {
    public static void main(String[] args){
    PecaXadrez[] pecas = new PecaXadrez[4];
    pecas[0] = new Cavalo(Cor.BRANCA);
    pecas[1] = new Cavalo(Cor.PRETA);
    pecas[2] = new Bispo(Cor.BRANCA);
    pecas[3] = new Bispo(Cor.PRETA);

    for(int i = 0; i < 4; i++){
        System.err.println(pecas[i].mover());

        if(pecas[i] instanceof Especial){
            Especial especial = (Especial)pecas[i];
            System.out.println(especial.habilidadeEspecial());
        }
    }

    

    }
}
