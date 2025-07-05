package xadrez;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class PainelTabuleiro extends JPanel{
   private ControladorDeJogo controlador;
    private JButton botaoReiniciar;

    public PainelTabuleiro(){
        setBorder(BorderFactory.createLineBorder(Color.BLACK));

        this.setLayout(null); 

        MouseAdapter mouseAdapter = new MouseAdapter(){
            

            @Override
            public void mousePressed(MouseEvent e){
               int lado = controlador.getLado();
               int coluna = e.getX() / lado;
               int linha = e.getY() / lado;
               controlador.gerenciarClique(linha, coluna);
            }
            
        };

        addMouseListener(mouseAdapter);

        this.botaoReiniciar = new JButton("Reiniciar Jogo");
        this.botaoReiniciar.setFocusable(false);
        this.botaoReiniciar.setVisible(false);
        this.botaoReiniciar.addActionListener(e -> {
            controlador.reiniciarJogo();
            repaint(); 
        });
        this.add(botaoReiniciar);

    }

    public void controlaBotao(boolean mostrar){
        this.botaoReiniciar.setVisible(mostrar);
    }

    public void setControlador(ControladorDeJogo controlador) {
        this.controlador = controlador;
        int lado = controlador.getLado();
        setPreferredSize(new Dimension(8 * lado, 8 * lado));

        int larguraBotao = 200;
        int alturaBotao = 50;
        int xBotao = (8 * lado - larguraBotao) / 2;
        int yBotao = (8 * lado - alturaBotao) / 2;
        this.botaoReiniciar.setBounds(xBotao, yBotao, larguraBotao, alturaBotao);
    }

    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g); // Limpa a tela (essencial!)

    // Adicionamos uma verificação de segurança: só desenha se o controlador já existir.
    if (this.controlador != null) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Casa casa = controlador.getCasa(i, j);
                if (casa != null) {
                    casa.desenhaCasa(g);
                    if (casa.peca != null)
                        casa.peca.desenhaPeca(g);
                }
            }
        }
        for(Casa casa : controlador.getMovimentosPossiveis()){
            g.setColor(Color.BLACK);
            g.fillOval(casa.getColuna()*controlador.getLado() + (controlador.getLado() - 16)/2, casa.getLinha()*controlador.getLado() + (controlador.getLado() - 16)/2, 16, 16);
            g.setColor(Color.RED);
            g.drawOval(casa.getColuna()*controlador.getLado() + (controlador.getLado() - 16)/2, casa.getLinha()*controlador.getLado() + (controlador.getLado() - 16)/2, 16, 16);
        }

        if(this.controlador.getPromocao()){
            g.setColor(Color.WHITE);
            g.fillRect(controlador.getPromocaoColuna()*controlador.getLado(), controlador.getPromocaoLinha()*controlador.getLado(), controlador.getLado(), controlador.getLado()*4);
            if(controlador.getPromocaoLinha() == 0){
                g.drawImage(controlador.getImagem("B_RAINHA").getScaledInstance(controlador.getLado(), controlador.getLado(), Image.SCALE_SMOOTH), controlador.getPromocaoColuna()*controlador.getLado(), (controlador.getPromocaoLinha())*controlador.getLado(), null);
                g.drawImage(controlador.getImagem("B_CAVALO").getScaledInstance(controlador.getLado(), controlador.getLado(), Image.SCALE_SMOOTH), controlador.getPromocaoColuna()*controlador.getLado(), (controlador.getPromocaoLinha()+1)*controlador.getLado(), null);
                g.drawImage(controlador.getImagem("B_TORRE").getScaledInstance(controlador.getLado(), controlador.getLado(), Image.SCALE_SMOOTH), controlador.getPromocaoColuna()*controlador.getLado(), (controlador.getPromocaoLinha()+2)*controlador.getLado(), null);
                g.drawImage(controlador.getImagem("B_BISPO").getScaledInstance(controlador.getLado(), controlador.getLado(), Image.SCALE_SMOOTH), controlador.getPromocaoColuna()*controlador.getLado(), (controlador.getPromocaoLinha()+3)*controlador.getLado(), null);
            }
            else{
                g.drawImage(controlador.getImagem("P_RAINHA").getScaledInstance(controlador.getLado(), controlador.getLado(), Image.SCALE_SMOOTH), controlador.getPromocaoColuna()*controlador.getLado(), (controlador.getPromocaoLinha())*controlador.getLado(), null);
                g.drawImage(controlador.getImagem("P_CAVALO").getScaledInstance(controlador.getLado(), controlador.getLado(), Image.SCALE_SMOOTH), controlador.getPromocaoColuna()*controlador.getLado(), (controlador.getPromocaoLinha()+1)*controlador.getLado(), null);
                g.drawImage(controlador.getImagem("P_TORRE").getScaledInstance(controlador.getLado(), controlador.getLado(), Image.SCALE_SMOOTH), controlador.getPromocaoColuna()*controlador.getLado(), (controlador.getPromocaoLinha()+2)*controlador.getLado(), null);
                g.drawImage(controlador.getImagem("P_BISPO").getScaledInstance(controlador.getLado(), controlador.getLado(), Image.SCALE_SMOOTH), controlador.getPromocaoColuna()*controlador.getLado(), (controlador.getPromocaoLinha()+3)*controlador.getLado(), null);
            }
        }

        if(controlador.getResultado() != null){
                g.setColor(new Color(0, 0, 0, 150));
                g.fillRect(0, 0, getWidth(), getHeight());

                Font fonteResultado = new Font("Arial", Font.BOLD, 32);
                g.setFont(fonteResultado);
                g.setColor(Color.WHITE);

                String mensagem;
                if (controlador.getResultado() == Color.CYAN) {
                    mensagem = "Empate!";
                } 
                else {
                    String vencedor = (controlador.getResultado() == Color.WHITE) ? "Brancas" : "Pretas";
                    mensagem = "Xeque-mate! " + vencedor + " venceram!";
                }
                
                int larguraTextoResultado = g.getFontMetrics().stringWidth(mensagem);
                g.drawString(mensagem, (getWidth() - larguraTextoResultado) / 2, botaoReiniciar.getY() - 30);
        }
    }

    }

}